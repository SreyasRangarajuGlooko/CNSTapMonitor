package com.sreyas.cnstapmonitor.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.sreyas.cnstapmonitor.R;

public class FeedbackHandler {

    public String getFeedbackMessage(String message, int response, int tapCount, Activity activity) {
        if (message.equals(activity.getString(R.string.save_message, tapCount)) && response == 1 &&
                askFeedbackCheck(activity)) {
            return activity.getString(R.string.enjoy_app);
        } else if (message.equals(activity.getString(R.string.enjoy_app))) {
            if (response == 0) {
                return activity.getString(R.string.support_feedback);
            } else if (response == 1) {
                return activity.getString(R.string.rate_app);
            }
        } else if ((message.equals(activity.getString(R.string.rate_app)) ||
                message.equals(activity.getString(R.string.support_feedback))) &&
                response == 1) {
            return activity.getString(R.string.feedback_action);
        }
        return "";
    }

    public Intent getFeedbackAction(String message, Activity activity) {
        if (message.equals(activity.getString(R.string.rate_app))) {
            setUserRatedApp(activity);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.rate_link)));
            return intent;
        } else if (message.equals(activity.getString(R.string.support_feedback))) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(activity.getString(R.string.support_email_address)));
            intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.support_email_subject));
            return intent;
        }
        return null;
    }

    public void incrementSaveCount(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(activity.getString(R.string.save_count), sharedPreferences.getInt(activity.getString(R.string.save_count), 0) + 1);
        editor.apply();
    }

    private boolean askFeedbackCheck(Activity activity) {
        return true;
//        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(activity.getString(R.string.user_rated_app), 0) == 0 &&
//                (sharedPreferences.getInt(activity.getString(R.string.save_count), 0) == 10 ||
//                        sharedPreferences.getInt(activity.getString(R.string.save_count), 0) % 90 == 0);
    }

    private void setUserRatedApp(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(activity.getString(R.string.user_rated_app), 1);
        editor.apply();
    }
}
