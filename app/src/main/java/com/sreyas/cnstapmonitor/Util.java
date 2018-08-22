package com.sreyas.cnstapmonitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sreyas on 1/25/2018.
 */

public final class Util {
    public static String timeNumToString(long timeStamp) {
        Date date = new Date(timeStamp * 60000L);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.US);
        return dateFormat.format(date);
    }
}
