package com.example.libbys.homepokertournement.CustomPokerClasses;

/**
 * Class created to define blind schedule
 */

public class Blinds {
    /**
     * Represents the small blind at the specified level, to get the big blind double the small blind
     * For instance the big blind at round 3 is 80 (40*2) once you get to the end or the array the
     * tournament will continue with the last value until finished
     */

    public static final int[] DEFAULT_BLINDS1500 = {20, 30, 40, 50, 100, 150, 200, 300, 400, 600, 800, 1000, 1200, 1600, 2000};

}