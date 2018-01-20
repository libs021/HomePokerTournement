package DataBaseFiles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Poker,db";
    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_DATABASE = "Create Table " + PlayerContract.PlayerEntry.TABLE_NAME + " ( " +
            PlayerContract.PlayerEntry._ID + "( INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PlayerContract.PlayerEntry.NAME + " TEXT NOT NULL, " +
            PlayerContract.PlayerEntry.BUYIN + " INTEGER DEFAULT 0, " +
            PlayerContract.PlayerEntry.CASHOUT + "INTEGER DEFAULT 0. " +
            PlayerContract.PlayerEntry.WIN + "INTEGER DEFAULT 0); ";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }


    //No need to implement on upgrade as our app currently is working with database version 1.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
