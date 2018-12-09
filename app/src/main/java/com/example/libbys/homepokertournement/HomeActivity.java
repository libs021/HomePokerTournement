package com.example.libbys.homepokertournement;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libbys.homepokertournement.Adapters.PokerTournamentCursorAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.NewPlayerDialog;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.dateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Home Screen for the App, Loads some buttons, as well as lists the upcoming tournaments.
 */

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NewPlayerDialog.NewPlayerInterface {
    PokerTournamentCursorAdapter cursorAdapter;
    //Designates whether the user wants to see upcoming or past tournaments.
    Boolean upcoming = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        cursorAdapter = new PokerTournamentCursorAdapter(this, null, 0);
        ListView listView = findViewById(R.id.tournamentListView);
        listView.setAdapter(cursorAdapter);
        getSupportLoaderManager().initLoader(0, null, HomeActivity.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this, TournamentPreview.class);
                Uri uri = ContentUris.withAppendedId(PokerContract.TournamentEntry.CONTENT_URI, l);
                Bundle bundle = createTournamentInfoBundle(adapterView,i);
                intent.putExtras(bundle);
                intent.setData(uri);
                startActivity(intent);
            }
        });


    }

    private Bundle createTournamentInfoBundle(AdapterView<?> adapterView, int i) {
        PokerTournamentCursorAdapter adapter = (PokerTournamentCursorAdapter) adapterView.getAdapter();
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(i);
        Bundle bundle = new Bundle();
        bundle.putString(PokerContract.TournamentEntry.GAME,cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.GAME)));
        bundle.putInt(PokerContract.TournamentEntry.COST,cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.COST)));
        bundle.putString(PokerContract.TournamentEntry.STARTTIME,cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTTIME)));
        bundle.putInt(PokerContract.TournamentEntry.STARTINGCHIPS,cursor.getInt(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTINGCHIPS)));
        return bundle;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] toSelect = {PokerContract.TournamentEntry._ID, PokerContract.TournamentEntry.GAME,
                PokerContract.TournamentEntry.COST, PokerContract.TournamentEntry.STARTTIME,PokerContract.TournamentEntry.STARTINGCHIPS};
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String time = dateUtils.DATABASE_DATE_FORMAT.format(today);
        String selection;
        if (upcoming) selection = PokerContract.TournamentEntry.STARTTIME + ">?";
        else selection = PokerContract.TournamentEntry.STARTTIME + "<?";
        String[] selectionArgs = {time};
        return new android.support.v4.content.CursorLoader(this, PokerContract.TournamentEntry.CONTENT_URI, toSelect, selection,
                selectionArgs, PokerContract.TournamentEntry.STARTTIME);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    //This is the callback called from our dialog that gives us the player name.
    @Override
    public void add(String name) {
        ContentValues values = new ContentValues();
        values.put(PokerContract.PlayerEntry.NAME, name);
        getContentResolver().insert(PokerContract.PlayerEntry.CONTENT_URI, values);
        Toast.makeText(this, getString(R.string.AddedPlayer, name), Toast.LENGTH_LONG).show();

    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.MenuAddPlayer:
                newPlayer();
                return true;
            case R.id.MenuNewTournament:
                newTournament();
                return true;
            case R.id.MenuViewPlayer:
                viewPlayer();
                return true;
            case R.id.MenuToggle:
                toggleUpcoming();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleUpcoming() {
        TextView upcomingTextView = findViewById(R.id.upcomingPast);
        if (upcoming) upcomingTextView.setText(R.string.previous_Tournaments);
        else upcomingTextView.setText(getString(R.string.upcoming_tournaments));
        upcoming = !upcoming;
        getSupportLoaderManager().restartLoader(0,null,HomeActivity.this);

    }

    private void viewPlayer() {
        Intent intent = new Intent(HomeActivity.this, PlayerListActivity.class);
        startActivity(intent);
    }

    private void newTournament() {
        Intent intent = new Intent(HomeActivity.this, TournamentActivity.class);
        startActivity(intent);
    }

    private void newPlayer() {
        NewPlayerDialog dialog = new NewPlayerDialog();
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "AddPlayer");
    }
}
