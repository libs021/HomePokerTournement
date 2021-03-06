package com.example.libbys.homepokertournement;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.libbys.homepokertournement.Adapters.PayoutAdapter;
import com.example.libbys.homepokertournement.Adapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.PayOuts;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.databaseHelper;
import com.example.libbys.homepokertournement.DataBaseFiles.dateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.content.ContentUris.withAppendedId;

/**
 * This will manage individual Tournaments prior to tournament starting will allow you to add players. Once the tournament starts you can update the chip counts.
 */
public class TournamentPreview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOAD_PLAYER_INFO = 17;
    private static final int LOAD_RESULTS = 10;
    private TournamentPlayerAdapter tournamentPlayerAdapter;
    private PayoutAdapter payoutAdapter;
    private ArrayList<TournamentPlayer> players;
    private ArrayList<Double> prizes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentpreview);
        ListView playerListView, payoutListView;
        TextView blindstartTimeTextView = findViewById(R.id.blinds);
        Intent intent = getIntent();
        String startTime = intent.getStringExtra(PokerContract.TournamentEntry.STARTTIME);
        try {
            Date toformat = dateUtils.DATABASE_DATE_FORMAT.parse(startTime);
            String formattedDate = dateUtils.APP_DATE_FORMAT.format(toformat);
            blindstartTimeTextView.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Uri uri = getIntent().getData();
        final long id = ContentUris.parseId(uri);
        players = new ArrayList<>();
        prizes = new ArrayList<>();

        payoutListView = findViewById(R.id.payoutListView);
        payoutAdapter = new PayoutAdapter(TournamentPreview.this, R.layout.playerintournamentlistview, prizes);
        payoutListView.setAdapter(payoutAdapter);

        //TODO Load info from saved instance if the id from the intent matches the saveddata. If not we are previewing a different tournament;
        getSupportLoaderManager().initLoader(LOAD_PLAYER_INFO,null, this);
        playerListView = findViewById(R.id.playerintournament);
        tournamentPlayerAdapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        playerListView.setAdapter(tournamentPlayerAdapter);
        final Button addPlayer = findViewById(R.id.addPlayertournament);
        final Button start = findViewById(R.id.startTournament);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TournamentPreview.this, PlayerListActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TournamentPreview.this, TournamentInProgress.class);
                intent.setData(uri);
                intent.putExtra("PlayersList", players);
                intent.putExtra("TournID", id);
                startActivity(intent);
            }
        });
    }

    private void setUpPastTournament() {
        TextView StartTime = findViewById(R.id.timer);
        Button addPlayerButton = findViewById(R.id.addPlayertournament);
        Button startTournament = findViewById(R.id.startTournament);
        StartTime.setText(R.string.Tournament_Results);
        addPlayerButton.setVisibility(View.GONE);
        startTournament.setVisibility(View.GONE);
        getSupportLoaderManager().initLoader(LOAD_RESULTS,null,this);

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri fromPreviousActivity = getIntent().getData();
        Long tournamentID = ContentUris.parseId(fromPreviousActivity);
        String[] selectionArgs = {String.valueOf(tournamentID)};
        switch (i){
            case LOAD_PLAYER_INFO:
                return loadPlayerInfo(tournamentID,selectionArgs);
                // Use case LOAD_RESULTS just needed a default case;
            default:
                return loadResults(selectionArgs);

        }

    }

    private Loader<Cursor> loadResults(String[] selectionArgs) {
        Uri toquery = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_PLAYERTOTOURNAMENT);
        String selection = PokerContract.PlayerToTournament.TOURNAMENT + "=?";
        return new android.support.v4.content.CursorLoader(this,toquery,null,selection,selectionArgs,null);


    }

    private Loader<Cursor> loadPlayerInfo(long tournamentID, String[] selectionArgs) {
        Uri toquery = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_GETPLAYERBYTOURNAMENTID);
        toquery = withAppendedId(toquery, tournamentID);
        return new android.support.v4.content.CursorLoader(this, toquery, null, null, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOAD_RESULTS:
                extractPlayerResults(cursor);
                break;
            case LOAD_PLAYER_INFO:
                extractPlayerInfor(cursor);
        }
    }

    private void extractPlayerResults(Cursor cursor) {
        //Cursor should be sorted by player ID so now we can iterate through
        Collections.sort(players,new TournamentPlayer.sortByID());
        int i =0;
        if (cursor.moveToFirst()) {
            do {
                int position = cursor.getInt(cursor.getColumnIndex(PokerContract.PlayerToTournament.POSITION));
                TournamentPlayer player = players.get(i);
                player.setmPosition(position);
                i++;
            } while (cursor.moveToNext());
        }
        Collections.sort(players, new TournamentPlayer.sortByPosition());
        tournamentPlayerAdapter.notifyDataSetChanged();
    }

    private void extractPlayerInfor(Cursor cursor) {
        Intent intent = getIntent();
        String startTime = intent.getStringExtra(PokerContract.TournamentEntry.STARTTIME);
        String currentTime = dateUtils.getCurrentDate();
        if (startTime.compareTo(currentTime)<0) setUpPastTournament();
        if (cursor.moveToFirst()) {
            int startingChipCount = intent.getIntExtra(PokerContract.TournamentEntry.STARTINGCHIPS,0);
            getPlayerInfo(cursor,startingChipCount);
            int cost = intent.getIntExtra(PokerContract.TournamentEntry.COST,0);
            if (players.size() != 0) {
                setupPrizes(cost);
                payoutAdapter.notifyDataSetChanged();
                tournamentPlayerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setupPrizes(int cost) {
        int playerCount = players.size();
        int prizepool = playerCount * cost;

        //get array of prize percentages
        double payoutPercent[] = PayOuts.getPayoutPercentages(playerCount);
        //Clears the prizes list so it can be adjusted.
        prizes.clear();
        //Add the prizes to the prizes array by multiplying the total prize pool by the percentage
        for (double aPayoutPercent : payoutPercent) prizes.add(aPayoutPercent * prizepool);

    }

    private void getPlayerInfo(Cursor cursor,int StartingChipCount) {
        players.clear();
        do {
            int playerID = cursor.getInt(cursor.getColumnIndex(PokerContract.PlayerEntry._ID));
            TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), StartingChipCount, playerID);
            players.add(playerToAdd);
        } while (cursor.moveToNext());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}
