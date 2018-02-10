package com.example.libbys.homepokertournement.DataBaseFiles;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

import java.text.ParseException;
import java.util.Date;

/**
 * Manages the Poker Tournament List view.
 */

public class PokerTournamentCursorAdapter extends CursorAdapter {
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

        String date1 = (cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.STARTTIME)));
        String cost1 = (cursor.getString(cursor.getColumnIndex(PokerContract.TournamentEntry.COST)));
        try {
            Date toformat = databaseHelper.DATABASE_DATE_FORMAT.parse(date1);
            String formattedDate = databaseHelper.APP_DATE_FORMAT.format(toformat);
            date.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cost.setText(cost1);
    }
}
