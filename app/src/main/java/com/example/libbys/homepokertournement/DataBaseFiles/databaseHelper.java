package com.example.libbys.homepokertournement.DataBaseFiles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Poker.db";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_PLAYERTABLE = "Create Table " + PokerContract.PlayerEntry.TABLE_NAME + " ( " +
            PokerContract.PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PokerContract.PlayerEntry.NAME + " TEXT NOT NULL, " +
            PokerContract.PlayerEntry.BUYIN + " INTEGER DEFAULT 0, " +
            PokerContract.PlayerEntry.CASHOUT + " INTEGER DEFAULT 0, " +
            PokerContract.PlayerEntry.WIN + "  DEFAULT 0); ";
    private static final String CREATE_TOURNEMENTTABLB = "Create Table " + PokerContract.TournamentEntry.TABLE_NAME + " ( " +
            PokerContract.TournamentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PokerContract.TournamentEntry.STARTTIME + " DATETIME NOT NULL, " +
            PokerContract.TournamentEntry.ENDTIME + " DATETIME, " +
            PokerContract.TournamentEntry.GAME + " TEXT NOT NULL, " +
            PokerContract.TournamentEntry.NUMPLAYERS + " INTEGER DEFAULT 0, " +
            PokerContract.TournamentEntry.COST + " INTEGER NOT NULL);";

    private static final String CREATE_PLAYERTOTOURNAMENT = "Create Table " + PokerContract.PlayerToTournament.TABLE_NAME + " ( " +
            PokerContract.PlayerToTournament._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PokerContract.PlayerToTournament.PLAYER + " INTEGER REFERENCES " + PokerContract.PlayerEntry.TABLE_NAME + "(" + PokerContract.PlayerEntry._ID + ")," +
            PokerContract.PlayerToTournament.TOURNAMENT + " INTEGER REFERENCES " + PokerContract.TournamentEntry.TABLE_NAME + "(" +
            PokerContract.TournamentEntry._ID + "));";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_PLAYERTABLE);
        sqLiteDatabase.execSQL(CREATE_TOURNEMENTTABLB);
        sqLiteDatabase.execSQL(CREATE_PLAYERTOTOURNAMENT);
    }


    //No need to implement on upgrade as our app currently is working with database version 1.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
