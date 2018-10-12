package com.sreyas.cnstapmonitor;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

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

    public static void startLoading(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static void stopLoading(Activity activity){
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
