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

import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;

import java.util.ArrayList;

/**
 * Created by Libby's on 2/5/2018.
 */

public class TournamentPreview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int GETPLAYERLOADER = 10;
    private static final int GETSTARTINGCHIPCOUNTLOADER = 17;
    private static ArrayList<TournamentPlayer> players = new ArrayList<>();
    TournamentPlayerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentpreview);
        getSupportLoaderManager().initLoader(GETPLAYERLOADER, null, TournamentPreview.this);
        ListView listView = findViewById(R.id.playerintournament);
        adapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        listView.setAdapter(adapter);
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
        Uri uri = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_GETPLAYERBYTOURNAMENTID);
        uri = ContentUris.withAppendedId(uri, id);
        String[] selectionArgs = {tournamentID};
        return new android.support.v4.content.CursorLoader(this, uri, null, null, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        loader.getId();
        players.clear();
        switch (loader.getId()) {
            case GETPLAYERLOADER:
                if (cursor.moveToFirst()) {
                    do {
                        TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), 1500);
                        players.add(playerToAdd);


                    } while (cursor.moveToNext());
                }
                adapter.notifyDataSetChanged();
            case GETSTARTINGCHIPCOUNTLOADER:
                //TODO
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
