package com.example.libbys.homepokertournement;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerTournamentCursorAdapter;

/**
 * Created by Libby's on 1/21/2018.
 */

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PokerTournamentCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        cursorAdapter = new PokerTournamentCursorAdapter(this, null, false);
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
        getSupportLoaderManager().initLoader(1, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] toSelect = {PokerContract.TournamentEntry._ID, PokerContract.TournamentEntry.GAME,
                PokerContract.TournamentEntry.COST, PokerContract.TournamentEntry.NUMPLAYERS, PokerContract.TournamentEntry.STARTTIME};
        return new CursorLoader(this, PokerContract.TournamentEntry.CONTENT_URI, toSelect, null, null, PokerContract.TournamentEntry.STARTTIME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.changeCursor(null);
    }
}
