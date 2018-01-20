package com.example.libbys.homepokertournement.DataBaseFiles;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PlayerContract {
    public static final String CONTENT_AUTHORITY = "com.example.libbys.homepokertournement";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PLAYERS = "Players";

    //class never needs to instantiated as it only holds constants
    private PlayerContract() {
    }

    public static final class PlayerEntry implements BaseColumns {

        public static final String TABLE_NAME = "Players";
        public static final String _ID = BaseColumns._ID;

        //columns of the player table
        //Represents the name of the player
        public static final String NAME = "Name";

        //Represents the total win of a player
        public static final String WIN = "Win";

        //Represents the number of times played.
        public static final String PLAYED = "Played";

        //represents the total buyin of a palyer
        public static final String BUYIN = "Buyin";

        //represents the total cash out of player
        public static final String CASHOUT = "Cashout";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/player";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/player";
    }


}

