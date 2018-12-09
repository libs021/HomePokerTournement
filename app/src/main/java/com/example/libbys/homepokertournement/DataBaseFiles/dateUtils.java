package com.example.libbys.homepokertournement.DataBaseFiles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dateUtils {
    public static final SimpleDateFormat DATABASE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final SimpleDateFormat APP_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:MM");

    public static String getCurrentDate() {
        Date date = new Date();
        return DATABASE_DATE_FORMAT.format(date);
    }
}
