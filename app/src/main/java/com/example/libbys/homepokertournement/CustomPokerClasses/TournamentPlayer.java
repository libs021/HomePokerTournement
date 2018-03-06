package com.example.libbys.homepokertournement.CustomPokerClasses;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Custom Class for holding temp data about each player
 */

public class TournamentPlayer implements Comparable<TournamentPlayer>, Parcelable {

    public static final Parcelable.Creator<TournamentPlayer> CREATOR
            = new Parcelable.Creator<TournamentPlayer>() {
        public TournamentPlayer createFromParcel(Parcel in) {
            return new TournamentPlayer(in);
        }

        public TournamentPlayer[] newArray(int size) {
            return new TournamentPlayer[size];
        }
    };
    //represents the name of the play in Question
    private String mName;
    //represents the chip count of the player
    private int mChipCount;
    //Represents the playedID of the Player
    private long mID;

    public TournamentPlayer(@NonNull String Name, int ChipCount, long id) {
        mName = Name;
        mChipCount = ChipCount;
        mID = id;
    }

    private TournamentPlayer(Parcel in) {
        mName = in.readString();
        mChipCount = in.readInt();
        mID = in.readLong();
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

    public long getmID() {
        return mID;
    }

    @Override
    //This allows the built in collections to sort the tournament players by chip count.
    //if you want to add additional ways to sort players you will need to use comperator.
    public int compareTo(@NonNull TournamentPlayer tournamentPlayer) {
        return tournamentPlayer.getmChipCount() - this.mChipCount;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mChipCount);
        dest.writeLong(mID);
    }
}
