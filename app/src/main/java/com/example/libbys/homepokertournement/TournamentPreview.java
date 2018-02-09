package com.example.libbys.homepokertournement;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.libbys.homepokertournement.ArrayAdapters.PayoutAdapter;
import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.PayOuts;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;

import java.util.ArrayList;

/**
 * Created by Libby's on 2/5/2018.
 */

public class TournamentPreview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int GETPLAYERLOADER = 10;
    private static final int GETTOURNAMENTINFO = 17;
    private static ArrayList<TournamentPlayer> players = new ArrayList<>();
    private static ArrayList<String> prizes = new ArrayList<>();
    TournamentPlayerAdapter tournamentPlayerAdapter;
    PayoutAdapter payoutAdapter;
    int startingChipCount = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentpreview);
        getSupportLoaderManager().initLoader(GETPLAYERLOADER, null, TournamentPreview.this);
        getSupportLoaderManager().initLoader(GETTOURNAMENTINFO, null, TournamentPreview.this);
        ListView playerListView = findViewById(R.id.playerintournament);
        tournamentPlayerAdapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        playerListView.setAdapter(tournamentPlayerAdapter);
        ListView payoutListView = findViewById(R.id.payoutListView);
        payoutAdapter = new PayoutAdapter(this, R.layout.playerintournamentlistview, prizes);
        payoutListView.setAdapter(payoutAdapter);
        Button addPlayer = findViewById(R.id.addPlayertournament);
        Button start = findViewById(R.id.startTournament);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long tournamentID = Long.parseLong(getIntent().getData().getLastPathSegment());
                Intent intent = new Intent(TournamentPreview.this, TournamentProgress.class);
                startActivity(intent);
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
                String[] toSelect = {PokerContract.TournamentEntry._ID, PokerContract.TournamentEntry.STARTINGCHIPS,
                        PokerContract.TournamentEntry.NUMPLAYERS, PokerContract.TournamentEntry.STARTTIME,
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
        players.clear();
        switch (loader.getId()) {
            case GETPLAYERLOADER:
                if (cursor.moveToFirst()) {
                    do {
                        TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), startingChipCount);
                        players.add(playerToAdd);
                    } while (cursor.moveToNext());
                }
                tournamentPlayerAdapter.notifyDataSetChanged();
                break;
            case GETTOURNAMENTINFO:
                cursor.moveToFirst();
                prizes.clear();
                startingChipCount = cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTINGCHIPS));
                if (players.size() != 0) {
                    for (int i = 0; i < players.size(); i++) {
                        players.get(i).setmChipCount(startingChipCount);
                    }
                }
                String playerCountString = cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.NUMPLAYERS));
                int playerCount = Integer.parseInt(playerCountString);
                int cost = cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.COST));
                double[] payoutPercent = null;
                if (playerCount < 10) {
                    payoutPercent = PayOuts.LESSTHAN30;
                } else if (playerCount < 30) {
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
                tournamentPlayerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
