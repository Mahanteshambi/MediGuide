package com.application.rxdose.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(final String password) {
        Matcher matcher = Constants.PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
}
