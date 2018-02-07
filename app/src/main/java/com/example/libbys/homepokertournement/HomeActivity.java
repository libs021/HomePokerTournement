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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerTournamentCursorAdapter;
import com.example.libbys.homepokertournement.DataBaseFiles.databaseHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Home Screen for the App, Loads some buttons, as well as lists the most upcoming tournaments.
 */

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PokerTournamentCursorAdapter cursorAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        cursorAdapter = new PokerTournamentCursorAdapter(this, null, 0);
        Button addPlayerButton = findViewById(R.id.AddPlayer);
        Button viewPlayerButton = findViewById(R.id.ViewPlayer);
        Button createTournamentButton = findViewById(R.id.NewTournament);

        //Set buttons to be clickable. They all just switch activities
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });

        viewPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlayerListActivity.class);
                startActivity(intent);
            }
        });

        createTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TournamentActivity.class);
                startActivity(intent);
            }
        });
        ListView listView = findViewById(R.id.tournamentListView);
        listView.setAdapter(cursorAdapter);
        getSupportLoaderManager().initLoader(0, null, HomeActivity.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this, TournamentPreview.class);
                Uri uri = ContentUris.withAppendedId(PokerContract.TournamentEntry.CONTENT_URI, l);
                intent.setData(uri);
                startActivity(intent);
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] toSelect = {PokerContract.TournamentEntry._ID, PokerContract.TournamentEntry.GAME,
                PokerContract.TournamentEntry.COST, PokerContract.TournamentEntry.NUMPLAYERS, PokerContract.TournamentEntry.STARTTIME};
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String time = databaseHelper.DATE_FORMAT.format(today);
        String[] selectionArgs = {time};
        return new android.support.v4.content.CursorLoader(this, PokerContract.TournamentEntry.CONTENT_URI, toSelect, PokerContract.TournamentEntry.STARTTIME,
                selectionArgs, PokerContract.TournamentEntry.STARTTIME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
