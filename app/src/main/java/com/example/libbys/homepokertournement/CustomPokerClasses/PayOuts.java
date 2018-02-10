package com.example.libbys.homepokertournement.CustomPokerClasses;

/**
 * Class to hold static arrays of payoutPercentages
 */

public class PayOuts {
    //Top 3 players will receive prizes 1st 50% second 30%, and 3rd 20% will be applied to tournaments with less than 30 players
    public static final double[] LESSTHAN30 = {.5, .30, .20};
    //applied to tournaments with 30-50
    public static final double[] LESSTHAN50 = {.4, .24, .16, .12, .08};
    //applied to tournaments with 51-100 players
    public static final double[] LESSTHAN100 = {.30, .20, .12, .0925, .0750, .0625, .0525, .0425, .0325, .0225};
    //TODO implement payout schedules for more than 100 tournaments.

}
