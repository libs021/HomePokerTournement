package com.example.libbys.homepokertournement.CustomPokerClasses;


/**
 * Custom Class for holding temp data about each player
 */

public class TournamentPlayer {

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
}
