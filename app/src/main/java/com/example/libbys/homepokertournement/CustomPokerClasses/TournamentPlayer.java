package com.example.libbys.homepokertournement.CustomPokerClasses;


import android.support.annotation.NonNull;

/**
 * Custom Class for holding temp data about each player
 */

public class TournamentPlayer implements Comparable<TournamentPlayer> {

    private String mName;
    private int mChipCount;

    public TournamentPlayer(String Name, int ChipCount) {
        mName = Name;
        mChipCount = ChipCount;
    }

    public String getmName() {
        return mName;
    }

    public int getmChipCount() {
        return mChipCount;
    }

    public void setmChipCount(int ChipCount) {
        mChipCount = ChipCount;
    }

    @Override
    //This allows the built in collections to sort the tournament players by chip count.
    //if you want to add additional ways to sort players you will need to use comperator.
    public int compareTo(@NonNull TournamentPlayer tournamentPlayer) {
        return tournamentPlayer.getmChipCount() - this.getmChipCount();
    }
}
