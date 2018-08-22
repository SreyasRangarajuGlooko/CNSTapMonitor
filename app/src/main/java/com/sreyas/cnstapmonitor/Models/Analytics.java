package com.sreyas.cnstapmonitor.Models;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {
    private static FirebaseAnalytics firebaseAnalytics;

    public static void initialize(Context context){
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static void logSaveEvent(int saveCount, int tapCount, int userRatedApp){
        Bundle params = new Bundle();
        params.putString("save_count", String.valueOf(saveCount));
        params.putString("tap_count", String.valueOf(tapCount));
        params.putString("userRatedApp", String.valueOf(userRatedApp));
        firebaseAnalytics.logEvent("save_event", params);
    }

    public static void logFeedbackEvent(String message, String response){
        Bundle params = new Bundle();
        params.putString("message", message);
        params.putString("response", response);
        firebaseAnalytics.logEvent("feedback_event", params);
    }
}
