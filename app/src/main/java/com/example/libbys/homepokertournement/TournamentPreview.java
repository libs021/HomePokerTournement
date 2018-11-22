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
import android.widget.TextView;

import com.example.libbys.homepokertournement.Adapters.PayoutAdapter;
import com.example.libbys.homepokertournement.Adapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.PayOuts;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.databaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentUris.withAppendedId;

/**
 * This will manage individual Tournaments prior to tournament starting will allow you to add players. Once the tournament starts you can update the chip counts.
 */
public class TournamentPreview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int GETPLAYERLOADER = 10;
    private static final int GETTOURNAMENTINFO = 17;
    TextView blindstartTimeTextView;
    ListView playerListView, payoutListView;
    private TournamentPlayerAdapter tournamentPlayerAdapter;
    private PayoutAdapter payoutAdapter;
    private int cost;
    private ArrayList<TournamentPlayer> players;
    private ArrayList<Double> prizes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentpreview);
        playerListView = findViewById(R.id.playerintournament);
        blindstartTimeTextView = findViewById(R.id.blinds);
        final Uri uri = getIntent().getData();
        final long id = ContentUris.parseId(uri);
        players = new ArrayList<>();
        prizes = new ArrayList<>();

        payoutListView = findViewById(R.id.payoutListView);
        payoutAdapter = new PayoutAdapter(TournamentPreview.this, R.layout.playerintournamentlistview, prizes);
        payoutListView.setAdapter(payoutAdapter);

        //Load info from saved instance if the id from the intent matches the saveddata. If not we are previewing a different tournament;
        if (savedInstanceState != null) {
            if (id != savedInstanceState.getLong("ID", 0))
                getSupportLoaderManager().initLoader(GETPLAYERLOADER, null, TournamentPreview.this);
            else {
                players = savedInstanceState.getParcelableArrayList("Players");
            }
        }
        getSupportLoaderManager().initLoader(GETPLAYERLOADER, null, TournamentPreview.this);
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
                intent.putExtra("Cost", cost);
                intent.putExtra("TournID", id);
                startActivity(intent);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri fromPreviousActivity = getIntent().getData();
        Long tournamentID = ContentUris.parseId(fromPreviousActivity);
        String[] selectionArgs = {String.valueOf(tournamentID)};

        switch (i) {
            case GETPLAYERLOADER:
                Uri toquery = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_GETPLAYERBYTOURNAMENTID);
                toquery = withAppendedId(toquery, tournamentID);
                return new android.support.v4.content.CursorLoader(this, toquery, null, null, selectionArgs, null);
            case GETTOURNAMENTINFO:
                String[] toSelect = {PokerContract.TournamentEntry.STARTINGCHIPS, PokerContract.TournamentEntry.STARTTIME,
                        PokerContract.TournamentEntry.COST};
                Uri query = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_TOURNAMENT);
                String selection = PokerContract.TournamentEntry._ID + "=?";
                query = withAppendedId(query, tournamentID);
                return new android.support.v4.content.CursorLoader(this, query, toSelect, selection, selectionArgs, null);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int startingChipCount;
        switch (loader.getId()) {
            case GETPLAYERLOADER:
                if (cursor.moveToFirst()) {
                    players.clear();
                    do {
                        int playerID = cursor.getInt(cursor.getColumnIndex(PokerContract.PlayerEntry._ID));
                        TournamentPlayer playerToAdd = new TournamentPlayer(cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME)), 0, playerID);
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
                startingChipCount = cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTINGCHIPS));
                cost = cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.COST));
                String startTime = cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTTIME));
                if (players.size() != 0) {
                    for (int i = 0; i < players.size(); i++) {
                        players.get(i).setmChipCount(startingChipCount);
                    }
                }
                int playerCount = players.size();
                int prizepool = playerCount * cost;

                //get array of prize percentages
                double payoutPercent[] = PayOuts.getPayoutPercentages(playerCount);
                //Clears the prizes list so it can be adjusted.
                prizes.clear();
                //Add the prizes to the prizes array by multiplying the total prize pool by the percentage
                for (int i = 0; i < payoutPercent.length; i++)
                    prizes.add(payoutPercent[i] * prizepool);
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
}
