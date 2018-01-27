package com.example.libbys.homepokertournement.DataBaseFiles;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * {@link ContentProvider} for PokerApp app.
 */

public class PokerProvider extends ContentProvider {

    public static final String LOG_TAG = PokerProvider.class.getSimpleName();
    private static final int PLAYER = 100;
    private static final int PLAYER_ID = 101;
    private static final int TOURNAMENT = 200;
    private static final int PLAYERTOTOURNAMENT = 300;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_PLAYERS, PLAYER);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_PLAYERS + "/#", PLAYER_ID);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_TOURNAMENT, TOURNAMENT);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_PLAYERTOTOURNAMENT, PLAYERTOTOURNAMENT);
    }

    private databaseHelper db;

    @Override
    public boolean onCreate() {
        db = new databaseHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase database = db.getReadableDatabase();
        //Allows the cursor to be notified when the data changes, so it will refresh its data.
        Cursor cursor = database.query(PokerContract.PlayerEntry.TABLE_NAME, strings, s, strings1, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = db.getWritableDatabase();
        long rowID;
        Log.e("PokerProvider", "insert: " + sUriMatcher.match(uri));
        switch (sUriMatcher.match(uri)) {
            case PLAYER:
                rowID = database.insert(PokerContract.PlayerEntry.TABLE_NAME, null, contentValues);
                return ContentUris.withAppendedId(uri, rowID);
            case TOURNAMENT:
                rowID = database.insert(PokerContract.TournamentEntry.TABLE_NAME, null, contentValues);
                return ContentUris.withAppendedId(uri, rowID);
            case PLAYERTOTOURNAMENT:
                rowID = database.insert(PokerContract.PlayerToTournament.TABLE_NAME, null, contentValues);
                return ContentUris.withAppendedId(uri, rowID);
        }
        return ContentUris.withAppendedId(uri, -1);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
