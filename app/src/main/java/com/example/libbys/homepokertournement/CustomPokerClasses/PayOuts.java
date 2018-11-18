package com.example.libbys.homepokertournement.CustomPokerClasses;

import java.sql.CallableStatement;
import java.util.ArrayList;

/**
 * Class to hold static arrays of payoutPercentages
 */

public class PayOuts {
    //Applied to head to head tournaments
    private static final double[] HEADTOHEAD = {1};
    // Applied to tournaments with less than 3-9 players
    private static final double[] LESSTHAN10 = {.67, .33};
    //Top 3 players will receive prizes 1st 50% second 30%, and 3rd 20% will be applied to tournaments with less than 30 players
    private static final double[] LESSTHAN30 = {.5, .30, .20};
    //applied to tournaments with 30-50
    private static final double[] LESSTHAN50 = {.4, .24, .16, .12, .08};
    //applied to tournaments with 51-100 players
    private static final double[] LESSTHAN100 = {.30, .20, .12, .0925, .0750, .0625, .0525, .0425, .0325, .0225};
    //TODO implement payout schedules for more than 100 tournaments, currently will keep same payouts as 100

    public static double[] getPayoutPercentages(int numberofPlayers) {
        if (numberofPlayers < 2) return new double[] {};
        else if (numberofPlayers == 2) return HEADTOHEAD;
        else if (numberofPlayers < 10) return LESSTHAN10;
        else if (numberofPlayers < 30) return LESSTHAN30;
        else if (numberofPlayers < 50) return LESSTHAN50;
        return LESSTHAN100;
    }

}
