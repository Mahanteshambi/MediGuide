package com.application.rxdose.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    private static Date previousDay(int i) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);
        return cal.getTime();
    }

    public static String getPreviousDateString(int i) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        return dateFormat.format(previousDay(i));
    }
}
