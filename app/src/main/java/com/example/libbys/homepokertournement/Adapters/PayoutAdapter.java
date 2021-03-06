package com.example.libbys.homepokertournement.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Populates the tournament Preview page with the prizes. We reuse the Layout for populate the player listView.
 */

public class PayoutAdapter extends ArrayAdapter<Double> {

    public PayoutAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Double> players) {
        super(context, resource, players);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playerintournamentlistview, parent, false);
        }

        TextView standing = convertView.findViewById(R.id.PositionTextView);
        TextView playerName = convertView.findViewById(R.id.nameTextView);
        TextView chipCount = convertView.findViewById(R.id.chipTextView);
        standing.setText(getContext().getApplicationContext().getString(R.string.position, (position + 1)));
        playerName.setText(getContext().getApplicationContext().getString(R.string.prize, getItem(position)));
        chipCount.setVisibility(View.GONE);
        Log.e(TAG, "getView: " + (getItem(position)));
        return convertView;
    }
}
