package com.example.libbys.homepokertournement;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerCursorAdapter;
import com.example.libbys.homepokertournement.DataBaseFiles.databaseHelper;

public class PlayerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PokerCursorAdapter cursorAdapter = new PokerCursorAdapter(this, null, 0);
    databaseHelper helper = new databaseHelper(this);
    Boolean[] isItemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);
        ListView listView = findViewById(R.id.PlayerListView);
        listView.setAdapter(cursorAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
        //If there is a URI in from the previous activity we are creating a new tournament and
        // the use should be selecting the players to add to the tournament.
        if (getIntent().getData() != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!isItemSelected[i])
                        view.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                    else view.setBackgroundColor(000000);
                    isItemSelected[i] = !isItemSelected[i];
                }
            });
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
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
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
        cursorAdapter.changeCursor(data);
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
        cursorAdapter.changeCursor(null);
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
                int numberOfPlayers = 0;
                for (int i = 0; i < cursorAdapter.getCount(); i++) {
                    ContentValues values = new ContentValues();
                    if (isItemSelected[i] == true) {
                        Cursor items = cursorAdapter.getCursor();
                        items.moveToPosition(i);
                        int playerID = items.getInt(items.getColumnIndex(PokerContract.PlayerEntry._ID));
                        values.put(PokerContract.PlayerToTournament.TOURNAMENT, toID);
                        values.put(PokerContract.PlayerToTournament.PLAYER, playerID);
                        getContentResolver().insert(PokerContract.PlayerToTournament.CONTENT_URI, values);
                        numberOfPlayers++;
                    }
                }
                Toast.makeText(PlayerListActivity.this, "You inserted" + numberOfPlayers, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
