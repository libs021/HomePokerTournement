package com.example.libbys.homepokertournement.ArrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.R;

import java.util.ArrayList;

/**
 * This Array Adapter populates information about a player in a tournament
 */

public class TournamentPlayerAdapter extends ArrayAdapter<TournamentPlayer> {

    public TournamentPlayerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TournamentPlayer> players) {
        super(context, resource, players);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playerintournamentlistview, parent, false);
        }

        TournamentPlayer player = getItem(position);
        TextView standing = convertView.findViewById(R.id.PositionTextView);
        TextView playerName = convertView.findViewById(R.id.nameTextView);
        TextView chipCount = convertView.findViewById(R.id.chipTextView);
        //The array should be sorted prior to displaying the listview.
        standing.setText(String.format(getContext().getApplicationContext().getString(R.string.position), position + 1));
        playerName.setText(player.getmName());
        int numofChips = player.getmChipCount();
        chipCount.setText(String.valueOf(numofChips));
        return convertView;
    }
}
