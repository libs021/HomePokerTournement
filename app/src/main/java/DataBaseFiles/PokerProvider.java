package DataBaseFiles;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PlayerContract.CONTENT_AUTHORITY, PlayerContract.PATH_PLAYERS, PLAYER);
        sUriMatcher.addURI(PlayerContract.CONTENT_AUTHORITY, PlayerContract.PATH_PLAYERS + "/*", PLAYER_ID);
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
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
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
