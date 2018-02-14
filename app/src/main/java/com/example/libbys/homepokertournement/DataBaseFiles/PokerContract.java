package com.example.libbys.homepokertournement.DataBaseFiles;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PokerContract {
    public static final String PATH_TOURNAMENT = "Tournaments";
    public static final String PATH_GETPLAYERBYTOURNAMENTID = "PlayerBytournament";
    public static final String CONTENT_AUTHORITY = "com.example.libbys.homepokertournement";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PLAYERS = "Players";
    public static final String PATH_PLAYERTOTOURNAMENT = "PlayertoTournament";

    //class never needs to instantiated as it only holds constants
    private PokerContract() {
    }

    public static final class PlayerEntry implements BaseColumns {

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
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/player";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/player";
        static final String TABLE_NAME = "Players";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
    }

    public static final class TournamentEntry implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        //Columns for the Tournament table
        //Represents the name of the game
        public static final String GAME = "Game";
        //represents the start time of the Tournement
        public static final String STARTTIME = "StartTime";
        // represents the cost of the Tournament
        public static final String COST = "Costs";
        // Represents the starting Chip Count of the tournament
        public static final String STARTINGCHIPS = "StartingChips";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/tournament";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/tournament";
        static final String TABLE_NAME = "Tournaments";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        //represents the end time of the tournement
        static final String ENDTIME = "EndTime";
    }

    //Table to hold what players are playing in what tournament.
    public static final class PlayerToTournament implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        //Columns for table
        //Represents the PlayerID
        public static final String PLAYER = "Player";
        //Represents the TournamentID;
        public static final String TOURNAMENT = "Tournament";
        //Represents the position the player with ID finished in the tournament with ID
        public static final String POSITION = "Position";
        //represents the prize that player with ID earned in tournament with ID
        public static final String PRIZE = "Prize";
        static final String TABLE_NAME = "PlayertoTournament";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

    }


}

