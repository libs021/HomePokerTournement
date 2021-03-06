package com.example.libbys.homepokertournement;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libbys.homepokertournement.Adapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.PayOuts;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayerDialog;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentTimer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer.sortByChips;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static java.lang.Boolean.TRUE;


/**
 * Keeps track of a tournament that is in progress. All info will be handed over by previous activity.
 */

public class TournamentInProgress extends AppCompatActivity implements TournamentPlayerDialog.TournamentPlayerInterface {
    private static TournamentTimer timer;
    private ArrayList<TournamentPlayer> players;
    private TournamentPlayerAdapter adapter;
    private int numberofPlayersBusted;
    private long tournAmentID;
    private ListView listView;
    private int cost;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentinprogress);
        LinearLayout rootView = findViewById(R.id.ll_roottournamentinprogress);
        if (savedInstanceState == null) {
            updateTournament(true);
            players = getIntent().getParcelableArrayListExtra("PlayersList");
            timer = new TournamentTimer(this, rootView);
            numberofPlayersBusted = 0;
            cost = getIntent().getIntExtra("Cost", 0);
        } else {
            players = savedInstanceState.getParcelableArrayList("PlayersList");
            TextView blinds = rootView.findViewById(R.id.tv_blinds);
            TextView time = rootView.findViewById(R.id.tv_timer);
            TextView round = rootView.findViewById(R.id.tv_round);
            timer.update(blinds, time, round, this);
            numberofPlayersBusted = savedInstanceState.getInt("Busted");
            cost = savedInstanceState.getInt("Cost");
        }
        tournAmentID = ContentUris.parseId(getIntent().getData());
        adapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        listView = findViewById(R.id.lv_players);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager manager = getSupportFragmentManager();
                TournamentPlayerDialog dialog = new TournamentPlayerDialog();
                Bundle args = new Bundle();
                args.putInt("PlayerToUpdate", i);
                dialog.setArguments(args);
                dialog.show(manager, "ThisDialog");
            }
        });
    }

    @Override
    public void bust(int player) {
        TournamentPlayer playertoUpdate = players.get(player);
        if (playertoUpdate.getmChipCount() > 0)
            playertoUpdate.setmChipCount(0 - players.size() + numberofPlayersBusted);
        else {
            Toast.makeText(this, "Cannot Bust an already busted player", Toast.LENGTH_SHORT).show();
            return;
        }
        numberofPlayersBusted++;
        Collections.sort(players,new sortByChips());
        if (numberofPlayersBusted == players.size() - 1) endTournament();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updatePlayer(int player, int count) {
        TournamentPlayer toUpdate = players.get(player);
        //checks to see if the player has already out.
        if (toUpdate.getmChipCount() <= 0) {
            Toast.makeText(this, "Cannot update a player that is already out.", Toast.LENGTH_SHORT).show();
        }
        toUpdate.setmChipCount(count);
        Collections.sort(players,new sortByChips());
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * @param outState Bundle in which to place your saved state.
     * @see #onSaveInstanceState(Bundle)
     * @see #onCreate
     * @see #onRestoreInstanceState(Bundle, PersistableBundle)
     * @see #onPause
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("PlayersList", players);
        outState.putInt("Busted", numberofPlayersBusted);
        outState.putInt("Cost", cost);
    }
    public void endTournament() {
        listView.setOnItemClickListener(null);
        this.setTitle("Final Standings");
        TextView round = findViewById(R.id.tv_round);
        String congrats = getString(R.string.Congrats, players.get(0).getmName());
        round.setText(congrats);
        assignPrizes();
        updateTournament(false);
    }
    //Will either start or stop the tournament based on the start boolean. Start will start the tournament. Stop will stop the tournament.
    private void updateTournament(boolean start) {
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        String updateField;
        if (start) updateField = PokerContract.TournamentEntry.STARTTIME;
        else updateField = PokerContract.TournamentEntry.ENDTIME;
        values.put(updateField,calendar.getTime().toString());
        values.put(PokerContract.TournamentEntry.ISCOMPLETE,!start);
        Uri uri = ContentUris.withAppendedId(PokerContract.TournamentEntry.CONTENT_URI,tournAmentID);
        getContentResolver().update(uri,values,PokerContract.TournamentEntry._ID,null);
    }

    private void assignPrizes() {
        int cursorBuyin;
        double cursorCashout;
        int cursorPlayed;
        double win;
        ContentValues values = new ContentValues();
        double payoutPercent[] = PayOuts.getPayoutPercentages(players.size());
        ArrayList<Double> prizes = new ArrayList<>();
        String[] querySelect = {PokerContract.PlayerEntry.CASHOUT, PokerContract.PlayerEntry.BUYIN, PokerContract.PlayerEntry.PLAYED};
        for (double aPayoutPercent : payoutPercent)
            prizes.add(aPayoutPercent * players.size() * cost);
        for (int i = 0; i < players.size(); i++) {
            values.clear();
            cursorCashout = 0;
            Long playerID = players.get(i).getmID();
            Uri uri = ContentUris.withAppendedId(PokerContract.PlayerEntry.CONTENT_URI, playerID);
            Cursor playerToUpdate = getContentResolver().query(uri, querySelect, null, null, null);
            playerToUpdate.moveToFirst();
            if (i < prizes.size()) {
                cursorCashout = playerToUpdate.getInt(playerToUpdate.getColumnIndex(PokerContract.PlayerEntry.CASHOUT)) + prizes.get(i);
                values.put(PokerContract.PlayerEntry.CASHOUT, cursorCashout);
                //updates the tournament to player table with the prize won one this tournament
                //i + one as the array starts at 0 and our winner should be number 1.
                updateTournamentToPlayer(playerID, tournAmentID, i + 1, prizes.get(i));
            }
            //Player didn't get a prize :(
            else updateTournamentToPlayer(playerID, tournAmentID, i + 1, 0);
            cursorBuyin = playerToUpdate.getInt(playerToUpdate.getColumnIndex(PokerContract.PlayerEntry.BUYIN)) + cost;
            values.put(PokerContract.PlayerEntry.BUYIN, cursorBuyin);
            cursorPlayed = playerToUpdate.getInt(playerToUpdate.getColumnIndex(PokerContract.PlayerEntry.PLAYED)) + 1;
            values.put(PokerContract.PlayerEntry.PLAYED, cursorPlayed);
            win = cursorCashout - cursorBuyin;
            values.put(PokerContract.PlayerEntry.WIN, win);
            getContentResolver().update(uri, values, null, null);
            playerToUpdate.close();
        }
    }

    private void updateTournamentToPlayer(long player, long tournament, int place, double prize) {
        ContentValues values = new ContentValues();
        values.put(PokerContract.PlayerToTournament.POSITION, place);
        values.put(PokerContract.PlayerToTournament.PRIZE, prize);
        Uri uri = PokerContract.PlayerToTournament.CONTENT_URI;
        String selection = PokerContract.PlayerToTournament.PLAYER + "=? and " + PokerContract.PlayerToTournament.TOURNAMENT + "=?";
        String[] args = {String.valueOf(player), String.valueOf(tournament)};
        getContentResolver().update(uri, values, selection, args);
    }
}
