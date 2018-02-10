package com.example.libbys.homepokertournement;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.libbys.homepokertournement.ArrayAdapters.PayoutAdapter;
import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.PayOuts;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayerDialog;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentTimer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.databaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This will manage individual Tournaments prior to tournament starting will allow you to add players. Once the tournament starts you can update the chip counts.
 */

public class TournamentPreview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, TournamentPlayerDialog.TournamentPlayerInterface {
    private static final int GETPLAYERLOADER = 10;
    private static final int GETTOURNAMENTINFO = 17;
    private static ArrayList<TournamentPlayer> players = new ArrayList<>();
    private static ArrayList<String> prizes = new ArrayList<>();
    TournamentPlayerAdapter tournamentPlayerAdapter;
    PayoutAdapter payoutAdapter;
    int startingChipCount = 0;
    int numberofPlayersBusted = 0;
    TextView blindstartTimeTextView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentpreview);
        blindstartTimeTextView = findViewById(R.id.blinds);
        getSupportLoaderManager().initLoader(GETPLAYERLOADER, null, TournamentPreview.this);
        final ListView playerListView = findViewById(R.id.playerintournament);
        tournamentPlayerAdapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        playerListView.setAdapter(tournamentPlayerAdapter);
        final ListView payoutListView = findViewById(R.id.payoutListView);
        payoutAdapter = new PayoutAdapter(this, R.layout.playerintournamentlistview, prizes);
        payoutListView.setAdapter(payoutAdapter);
        final Button addPlayer = findViewById(R.id.addPlayertournament);
        final Button start = findViewById(R.id.startTournament);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.roundTracker).setVisibility(View.VISIBLE);
                View rootView = findViewById(R.id.rootViewTournamentpreview);
                findViewById(R.id.subMenuForPrizes).setVisibility(View.GONE);
                start.setVisibility(View.GONE);
                addPlayer.setVisibility(View.GONE);
                TournamentTimer timer = new TournamentTimer(TournamentPreview.this, 120000, 1, 20, rootView);
                payoutListView.setVisibility(View.GONE);
                timer.start();
                playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TournamentPreview.this, PlayerListActivity.class);
                intent.setData(getIntent().getData());
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri fromPreviousActivity = getIntent().getData();
        String tournamentID = fromPreviousActivity.getLastPathSegment();
        Long id = Long.parseLong(tournamentID);
        String[] selectionArgs = {tournamentID};

        switch (i) {
            case GETPLAYERLOADER:
                Uri toquery = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_GETPLAYERBYTOURNAMENTID);
                toquery = ContentUris.withAppendedId(toquery, id);
                return new android.support.v4.content.CursorLoader(this, toquery, null, null, selectionArgs, null);
            case GETTOURNAMENTINFO:
                String[] toSelect = {PokerContract.TournamentEntry._ID, PokerContract.TournamentEntry.STARTINGCHIPS, PokerContract.TournamentEntry.STARTTIME,
                        PokerContract.TournamentEntry.COST};
                Uri query = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_TOURNAMENT);
                query = ContentUris.withAppendedId(query, id);
                return new android.support.v4.content.CursorLoader(this, query, toSelect, PokerContract.TournamentEntry._ID, selectionArgs, null);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        loader.getId();
        switch (loader.getId()) {
            case GETPLAYERLOADER:
                if (cursor.moveToFirst()) {
                    players.clear();
                    do {
                        TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), 0);
                        players.add(playerToAdd);
                    } while (cursor.moveToNext());
                }
                //this eliminates the race condition when both loaders kicked off at the same time. Since the Tournement info gets
                //set on the player we can wait initialize the loader.
                getSupportLoaderManager().initLoader(GETTOURNAMENTINFO, null, TournamentPreview.this);
                //No need to notify change as we notify both adapters they changed when done loading info from the tournament
                break;
            case GETTOURNAMENTINFO:
                cursor.moveToFirst();
                prizes.clear();
                startingChipCount = cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTINGCHIPS));
                String startTime = cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTTIME));
                if (players.size() != 0) {
                    for (int i = 0; i < players.size(); i++) {
                        players.get(i).setmChipCount(startingChipCount);
                    }
                }
                int playerCount = players.size();
                int cost = cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.COST));
                double[] payoutPercent = null;
                if (playerCount < 30) {
                    payoutPercent = PayOuts.LESSTHAN30;
                } else if (playerCount < 50) {
                    payoutPercent = PayOuts.LESSTHAN50;
                } else if (playerCount < 100) {
                    payoutPercent = PayOuts.LESSTHAN100;
                }

                int prizePool = cost * playerCount;
                for (int i = 0; i < payoutPercent.length; i++) {
                    double prize = prizePool * payoutPercent[i];
                    String stringPrize = String.format(getApplicationContext().getString(R.string.prize), prize);
                    prizes.add(stringPrize);
                }
                try {
                    Date toformat = databaseHelper.DATABASE_DATE_FORMAT.parse(startTime);
                    String formattedDate = databaseHelper.APP_DATE_FORMAT.format(toformat);
                    blindstartTimeTextView.setText(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                payoutAdapter.notifyDataSetChanged();
                tournamentPlayerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void bust(int player) {
        TournamentPlayer playertoUpdate = players.get(player);
        playertoUpdate.setmChipCount(0 - players.size() + numberofPlayersBusted);
        numberofPlayersBusted++;
        Collections.sort(players);
        tournamentPlayerAdapter.notifyDataSetChanged();

    }

    @Override
    public void updatePlayer(int player, int count) {
        players.get(player).setmChipCount(count);
        Collections.sort(players);
        tournamentPlayerAdapter.notifyDataSetChanged();
    }
}
