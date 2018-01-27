package com.example.libbys.homepokertournement.DataBaseFiles;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

/**
 * Created by Libby's on 1/27/2018.
 */

public class PokerTournamentCursorAdapter extends CursorAdapter {
    /**
     * Constructor that always enables auto-requery.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @deprecated This option is discouraged, as it results in Cursor queries
     * being performed on the application's UI thread and thus can cause poor
     * responsiveness or even Application Not Responding errors.  As an alternative,
     * use {@link LoaderManager} with a {@link CursorLoader}.
     */
    public PokerTournamentCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    /**
     * Constructor that allows control over auto-requery.  It is recommended
     * you not use this, but instead {@link #CursorAdapter(Context, Cursor, int)}.
     * When using this constructor, {@link #FLAG_REGISTER_CONTENT_OBSERVER}
     * will always be set.
     *
     * @param context     The context
     * @param c           The cursor from which to get the data.
     * @param autoRequery If true the adapter will call requery() on the
     *                    cursor whenever it changes so the most recent
     */
    public PokerTournamentCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter; may
     *                be any combination of {@link #FLAG_AUTO_REQUERY} and
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public PokerTournamentCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.tournamentlistview, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView date = view.findViewById(R.id.date);
        TextView cost = view.findViewById(R.id.cost);
        TextView numOfPlayers = view.findViewById(R.id.numOfplayers);

        String date1 = (cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTTIME)));
        String cost1 = (cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.COST)));
        String NumPlayers1 = (cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.NUMPLAYERS)));
        date.setText(date1);
        cost.setText(cost1);
        numOfPlayers.setText(NumPlayers1);
    }
}
