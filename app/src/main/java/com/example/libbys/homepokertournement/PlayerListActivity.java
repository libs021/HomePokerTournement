package com.example.libbys.homepokertournement;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;

import com.example.libbys.homepokertournement.Adapters.RecyclerViewPlayers;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;


/**
 * Will list all of the poker players in the app and show some basic info about as of now it will just list them.
 * you can also add players to a tournament on this screen
 */
public class PlayerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerViewPlayers playerslist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);
        //List behaves different depending on weather you access the list view from a tournament or the home screen.
        //if you access via a tournament the list can add players to said tournament. If access via home screen lists acts as a list for
        //reviewing the players and there tournament activity.
        playerslist = new RecyclerViewPlayers(null,this,(getIntent().getData()==null));
        RecyclerView rv = findViewById(R.id.RV_PlayerListView);
        rv.setAdapter(playerslist);
        rv.setLayoutManager(new LinearLayoutManager(this));

        getSupportLoaderManager().initLoader(7, null, PlayerListActivity.this);
        //If there is a URI in from the previous activity we are creating a new tournament and
        // the use should be selecting the players to add to the tournament.
        if (getIntent().getData() != null) {

            addFinalizeButton();
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to startDate loading.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] toSelect = {PokerContract.PlayerEntry._ID, PokerContract.PlayerEntry.NAME,
                PokerContract.PlayerEntry.BUYIN, PokerContract.PlayerEntry.CASHOUT, PokerContract.PlayerEntry.WIN};
        return new android.support.v4.content.CursorLoader(this, PokerContract.PlayerEntry.CONTENT_URI, toSelect, null, null, null);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>

     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        playerslist.setData(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        playerslist.setData(null);

    }

    private void addFinalizeButton() {
        Button finalizeButton = findViewById(R.id.createID);
        finalizeButton.setVisibility(View.VISIBLE);
        finalizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean[] isitemPressed = playerslist.getIsPressed();
                Uri uri = getIntent().getData();
                assert uri != null;
                String ID = uri.getLastPathSegment();
                int toID = Integer.parseInt(ID);
                ContentValues values = new ContentValues();
                Cursor items = playerslist.getData();
                items.moveToFirst();
                for (int i = 0; i < items.getCount(); i++) {
                    if (isitemPressed[i]) {
                        items.moveToPosition(i);
                        int playerID = items.getInt(items.getColumnIndex(PokerContract.PlayerEntry._ID));
                        values.put(PokerContract.PlayerToTournament.TOURNAMENT, toID);
                        values.put(PokerContract.PlayerToTournament.PLAYER, playerID);
                        getContentResolver().insert(PokerContract.PlayerToTournament.CONTENT_URI, values);
                        values.clear();
                    }
                }
                finish();
            }
        });
    }
}
