package com.example.libbys.homepokertournement.CustomPokerClasses;


import android.support.annotation.NonNull;

/**
 * Custom Class for holding temp data about each player
 */

public class TournamentPlayer implements Comparable<TournamentPlayer> {

    //represents the name of the play in Question
    private String mName;

    //represents the chip count of the player
    private int mChipCount;

    //Represents the playedID of the Player
    private int mID;

    public TournamentPlayer(@NonNull String Name, int ChipCount, int id) {
        mName = Name;
        mChipCount = ChipCount;
        mID = id;
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

    public int getmID() {
        return mID;
    }

    @Override
    //This allows the built in collections to sort the tournament players by chip count.
    //if you want to add additional ways to sort players you will need to use comperator.
    public int compareTo(@NonNull TournamentPlayer tournamentPlayer) {
        return tournamentPlayer.getmChipCount() - this.getmChipCount();
    }
}
