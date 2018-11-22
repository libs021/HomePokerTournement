package com.example.libbys.homepokertournement.DataBaseFiles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class databaseHelper extends SQLiteOpenHelper {
    public static final SimpleDateFormat DATABASE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final SimpleDateFormat APP_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:MM");
    private static final String DATABASE_NAME = "Poker.db";
    private static final int DATABASE_VERSION = 6;
    private static final String CREATE_PLAYERTABLE = "Create Table " + PokerContract.PlayerEntry.TABLE_NAME + " ( " +
            PokerContract.PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PokerContract.PlayerEntry.NAME + " TEXT NOT NULL, " +
            PokerContract.PlayerEntry.BUYIN + " INTEGER DEFAULT 0, " +
            PokerContract.PlayerEntry.CASHOUT + " DOUBLE DEFAULT 0, " +
            PokerContract.PlayerEntry.PLAYED + " INTEGER DEFAULT 0, " +
            PokerContract.PlayerEntry.WIN + " DOUBLE DEFAULT 0); ";
    private static final String CREATE_TOURNEMENTTABLB = "Create Table " + PokerContract.TournamentEntry.TABLE_NAME + " ( " +
            PokerContract.TournamentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PokerContract.TournamentEntry.STARTTIME + " DATETIME NOT NULL, " +
            PokerContract.TournamentEntry.ENDTIME + " DATETIME, " +
            PokerContract.TournamentEntry.GAME + " TEXT NOT NULL, " +
            PokerContract.TournamentEntry.COST + " INTEGER NOT NULL, " +
            PokerContract.TournamentEntry.ISCOMPLETE + " BOOLEAN DEFAULT 0," +
            PokerContract.TournamentEntry.STARTINGCHIPS + " INTEGER DEFAULT 1500);";


    private static final String CREATE_PLAYERTOTOURNAMENT = "Create Table " + PokerContract.PlayerToTournament.TABLE_NAME + " ( " +
            PokerContract.PlayerToTournament.PLAYER + " INTEGER REFERENCES " + PokerContract.PlayerEntry.TABLE_NAME + "(" + PokerContract.PlayerEntry._ID + ")," +
            PokerContract.PlayerToTournament.TOURNAMENT + " INTEGER REFERENCES " + PokerContract.TournamentEntry.TABLE_NAME + "(" +
            PokerContract.TournamentEntry._ID + "), " +
            PokerContract.PlayerToTournament.POSITION + " INTEGER, " +
            PokerContract.PlayerToTournament.PRIZE + " INTEGER NOT NULL DEFAULT 0, PRIMARY KEY (" + PokerContract.PlayerToTournament.PLAYER + ", " +
            PokerContract.PlayerToTournament.TOURNAMENT + "));";

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
