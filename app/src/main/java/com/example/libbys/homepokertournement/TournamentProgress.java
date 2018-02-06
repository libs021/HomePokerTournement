package com.example.libbys.homepokertournement;

import android.content.ContentUris;
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
import android.widget.ListView;

import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayerDialog;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentTimer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;

import java.util.ArrayList;
import java.util.Collections;

/**
 Activity to manage and display information related to the current Tournament that is in progress.
 Includes a time and creates an array of players to manage the tournament
 */

public class TournamentProgress extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, TournamentPlayerDialog.TournamentPlayerInterface {
    private static final int GETPLAYERLOADER = 10;
    private static final int GETSTARTINGCHIPCOUNTLOADER = 17;
    ArrayList<TournamentPlayer> players = new ArrayList<>();
    TournamentPlayerAdapter adapter;
    TournamentTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentprogress);
        ListView listView = findViewById(R.id.inTournament);
        adapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        listView.setAdapter(adapter);
        View rootView = findViewById(R.id.tournamentInProgressRootView);
        getSupportLoaderManager().initLoader(GETPLAYERLOADER, null, TournamentProgress.this);
        timer = new TournamentTimer(this, 15000, 1, 20, rootView);
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri fromPreviousActivity = getIntent().getData();
        String tournamentID = fromPreviousActivity.getLastPathSegment();
        Long id = Long.parseLong(tournamentID);
        Uri uri = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_GETPLAYERBYTOURNAMENTID);
        uri = ContentUris.withAppendedId(uri, id);
        String[] selectionArgs = {tournamentID};
        return new android.support.v4.content.CursorLoader(this, uri, null, null, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        loader.getId();
        switch (loader.getId()) {
            case GETPLAYERLOADER:
                if (cursor.moveToFirst()) {
                    do {
                        TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), 1500);
                        players.add(playerToAdd);


                    } while (cursor.moveToNext());
                }
            case GETSTARTINGCHIPCOUNTLOADER:
                //TODO
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void bust(int playerPosition) {
        players.get(playerPosition).setmChipCount(0);
        Collections.sort(players);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void updatePlayer(int playerPosition, int count) {
        players.get(playerPosition).setmChipCount(count);
        Collections.sort(players);
        adapter.notifyDataSetChanged();

    }
}
