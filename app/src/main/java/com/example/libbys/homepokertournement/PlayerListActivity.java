package com.example.libbys.homepokertournement;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.libbys.homepokertournement.Adapters.PokerPlayerCursorAdapter;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;


/**
 * Will list all of the poker players in the app and show some basic info about as of now it will just list them.
 * you can also add players to a tournament on this screen
 */
public class PlayerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PokerPlayerCursorAdapter cursorAdapter = new PokerPlayerCursorAdapter(this, null, 0);
    Boolean[] isItemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);
        ListView listView = findViewById(R.id.PlayerListView);
        listView.setAdapter(cursorAdapter);
        getSupportLoaderManager().initLoader(7, null, PlayerListActivity.this);
        //If there is a URI in from the previous activity we are creating a new tournament and
        // the use should be selecting the players to add to the tournament.
        if (getIntent().getData() != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO find a better way to handle show and get what items are selected
                    if (!isItemSelected[i])
                        view.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                    else view.setBackgroundColor(0x00000);
                    isItemSelected[i] = !isItemSelected[i];
                }
            });
            addFinalizeButton();

        } else listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("PlayerListActivity", "onItemClick: " + position);
                Cursor data = cursorAdapter.getCursor();
                data.moveToPosition(position);
                int playerID = data.getInt(data.getColumnIndex(PokerContract.PlayerEntry._ID));
                String playerName = data.getString(data.getColumnIndex(PokerContract.PlayerEntry.NAME));
                Uri uri = ContentUris.withAppendedId(PokerContract.BASE_CONTENT_URI, playerID);
                Intent intent = new Intent(PlayerListActivity.this, PlayerResultsActivity.class);
                intent.setData(uri);
                intent.putExtra("PLayerName", playerName);
                startActivity(intent);
            }
        });
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to startDate loading.
     */
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
        isItemSelected = new Boolean[data.getCount()];
        for (int i = 0; i < isItemSelected.length; i++) {
            isItemSelected[i] = false;
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
        isItemSelected = null;

    }

    private void addFinalizeButton() {
        Button finalizeButton = findViewById(R.id.createID);
        finalizeButton.setVisibility(View.VISIBLE);
        finalizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = getIntent().getData();
                String ID = uri.getLastPathSegment();
                int toID = Integer.parseInt(ID);
                ContentValues values = new ContentValues();
                Cursor items = cursorAdapter.getCursor();
                items.moveToFirst();
                for (int i = 0; i < cursorAdapter.getCount(); i++) {
                    if (isItemSelected[i]) {
                        int playerID = items.getInt(items.getColumnIndex(PokerContract.PlayerEntry._ID));
                        values.put(PokerContract.PlayerToTournament.TOURNAMENT, toID);
                        values.put(PokerContract.PlayerToTournament.PLAYER, playerID);
                        getContentResolver().insert(PokerContract.PlayerToTournament.CONTENT_URI, values);
                        values.clear();
                        items.moveToNext();
                    }
                }
                finish();
            }
        });
    }
}
