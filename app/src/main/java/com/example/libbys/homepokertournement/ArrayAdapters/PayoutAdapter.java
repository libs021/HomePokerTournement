package com.example.libbys.homepokertournement.ArrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

import java.util.ArrayList;

/**
 * Populates the tournament Preview page with the prizes. We reuse the Layout for populate the player listView.
 */

public class PayoutAdapter extends ArrayAdapter<String> {

    public PayoutAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> players) {
        super(context, resource, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playerintournamentlistview, parent, false);
        }


        TextView standing = convertView.findViewById(R.id.PositionTextView);
        TextView playerName = convertView.findViewById(R.id.nameTextView);
        TextView chipCount = convertView.findViewById(R.id.chipTextView);
        standing.setText("#" + (position + 1));
        playerName.setText(getItem(position));
        chipCount.setVisibility(View.GONE);
        return convertView;
    }
}
