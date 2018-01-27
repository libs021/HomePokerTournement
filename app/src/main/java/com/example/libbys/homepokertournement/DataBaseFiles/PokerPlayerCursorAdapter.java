package com.example.libbys.homepokertournement.DataBaseFiles;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

/**
 * Created by Libby's on 1/19/2018.
 */

public class PokerPlayerCursorAdapter extends CursorAdapter {

    public PokerPlayerCursorAdapter(Context context, Cursor c, int flags) {
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
        return LayoutInflater.from(context).inflate(R.layout.playerlistview, parent, false);
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
        TextView NameTextView = view.findViewById(R.id.playerName);
        TextView BuyInTextView = view.findViewById(R.id.buyin);
        TextView CashOutTextView = view.findViewById(R.id.cashout);
        TextView TotalTextView = view.findViewById(R.id.totalwinnings);

        String Name = cursor.getString(cursor.getColumnIndex(PokerContract.PlayerEntry.NAME));
        int Buyin = cursor.getInt(cursor.getColumnIndex(PokerContract.PlayerEntry.BUYIN));
        int CashOut = cursor.getInt(cursor.getColumnIndex(PokerContract.PlayerEntry.CASHOUT));
        int TotalWinnings = cursor.getInt(cursor.getColumnIndex(PokerContract.PlayerEntry.WIN));
        String toInsert;
        toInsert = String.format(context.getString(R.string.Buyin), Buyin);
        NameTextView.setText(Name);
        BuyInTextView.setText(toInsert);
        toInsert = String.format(context.getString(R.string.Cashout), CashOut);
        CashOutTextView.setText(toInsert);
        toInsert = String.format(context.getString(R.string.Total), TotalWinnings);
        TotalTextView.setText(toInsert);
    }
}
