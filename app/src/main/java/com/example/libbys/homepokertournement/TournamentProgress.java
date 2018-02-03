package com.example.libbys.homepokertournement;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentTimer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;

import java.util.ArrayList;

/**
 * Created by Libby's on 1/29/2018.
 */

public class TournamentProgress extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ArrayList<TournamentPlayer> players = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentprogress);
        ListView listView = findViewById(R.id.inTournament);
        TournamentPlayerAdapter adapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        listView.setAdapter(adapter);
        View rootView = findViewById(R.id.tournamentInProgressRootView);
        getSupportLoaderManager().initLoader(9, null, TournamentProgress.this);
        TournamentTimer timer = new TournamentTimer(this, 15000, 1, 20, rootView);
        timer.start();

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
        if (cursor.moveToFirst()) {
            do {
                TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), 1500);
                players.add(playerToAdd);


            } while (cursor.moveToNext());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
