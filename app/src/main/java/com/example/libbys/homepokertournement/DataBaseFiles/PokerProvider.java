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

/**
 * {@link ContentProvider} for PokerApp app.
 */

public class PokerProvider extends ContentProvider {

    public static final String LOG_TAG = PokerProvider.class.getSimpleName();
    private static final int PLAYER = 100;
    private static final int PLAYER_ID = 101;
    private static final int TOURNAMENT = 200;
    private static final int TOURNAMENT_ID = 201;
    private static final int PLAYERTOTOURNAMENT = 300;
    private static final int PLAYERBYTOURNAMENTID = 550;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_PLAYERS, PLAYER);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_PLAYERS + "/#", PLAYER_ID);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_TOURNAMENT, TOURNAMENT);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_TOURNAMENT + "/#", TOURNAMENT_ID);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_PLAYERTOTOURNAMENT, PLAYERTOTOURNAMENT);
        sUriMatcher.addURI(PokerContract.CONTENT_AUTHORITY, PokerContract.PATH_GETPLAYERBYTOURNAMENTID + "/#", PLAYERBYTOURNAMENTID);
    }

    private databaseHelper db;

    @Override
    public boolean onCreate() {
        db = new databaseHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] toSelect, @Nullable String selection, @Nullable String[] args, @Nullable String sort) {
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case PLAYER:
                //Allows the cursor to be notified when the data changes, so it will refresh its data.
                cursor = database.query(PokerContract.PlayerEntry.TABLE_NAME, toSelect, null, null, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case TOURNAMENT:
                selection = selection + "> ? ";
                cursor = database.query(PokerContract.TournamentEntry.TABLE_NAME, toSelect, selection, args, null, null, sort);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case TOURNAMENT_ID:
                selection = selection + "= ? ";
                return database.query(PokerContract.TournamentEntry.TABLE_NAME, toSelect, selection, args, null, null, null);
            case PLAYERBYTOURNAMENTID:
                String query = "Select * from " + PokerContract.PlayerEntry.TABLE_NAME + " Left join " + PokerContract.PlayerToTournament.TABLE_NAME +
                        " on " + PokerContract.PlayerEntry.TABLE_NAME + "." + PokerContract.PlayerEntry._ID +
                        " = " + PokerContract.PlayerToTournament.PLAYER + "  where " + PokerContract.PlayerToTournament.TABLE_NAME +
                        "." + PokerContract.PlayerToTournament.TOURNAMENT + " = ? ";
                cursor = database.rawQuery(query, args);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
        }
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
        switch (sUriMatcher.match(uri)) {
            case PLAYER:
                rowID = database.insert(PokerContract.PlayerEntry.TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, rowID);
            case TOURNAMENT:
                rowID = database.insert(PokerContract.TournamentEntry.TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, rowID);
            case PLAYERTOTOURNAMENT:
                rowID = database.insert(PokerContract.PlayerToTournament.TABLE_NAME, null, contentValues);
                uri = Uri.withAppendedPath(PokerContract.BASE_CONTENT_URI, PokerContract.PATH_GETPLAYERBYTOURNAMENTID);
                getContext().getContentResolver().notifyChange(uri, null);
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
        SQLiteDatabase database = db.getReadableDatabase();
        int numberOfRowsUpdated = 0;
        switch (sUriMatcher.match(uri)) {
            case PLAYER_ID:
                s = s + "=?";
                numberOfRowsUpdated = database.update(PokerContract.PlayerEntry.TABLE_NAME, contentValues, s, strings);
                return numberOfRowsUpdated;
            case TOURNAMENT_ID:
                s = s + "=?";
                numberOfRowsUpdated = database.update(PokerContract.TournamentEntry.TABLE_NAME, contentValues, s, strings);
                return numberOfRowsUpdated;
            default:
                return numberOfRowsUpdated;
        }
    }


}
