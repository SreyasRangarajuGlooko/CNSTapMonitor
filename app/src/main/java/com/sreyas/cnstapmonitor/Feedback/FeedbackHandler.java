package com.sreyas.cnstapmonitor.Feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Window;

import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.R;

import java.util.concurrent.Callable;

public class FeedbackHandler {
    private Activity activity;

    public FeedbackHandler(Activity activity){
        this.activity = activity;
    }

    public void launchFeedbackConditionally(){
        if(askFeedbackCheck()){
            appEnjoymentFeedback();
        }
    }

    private boolean askFeedbackCheck(){
        boolean giveFeedback;
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences.getInt(activity.getString(R.string.feedback_check), 0) < 2){
            giveFeedback = TapData.getTapData(activity).size() % 10 == 0;
        }
        else{
            giveFeedback = TapData.getTapData(activity).size() % 90 == 0;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(activity.getString(R.string.feedback_check), sharedPreferences.getInt(activity.getString(R.string.feedback_check), 0) + 1);
        editor.apply();
        return giveFeedback;
    }

    private void appEnjoymentFeedback(){
        showDialog(activity.getString(R.string.enjoy_app), new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                askRating();
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                askSupportFeedback();
                return null;
            }
        });
    }

    private void askRating(){
        showDialog(activity.getString(R.string.rate_app), new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.rate_link))));
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return null;
            }
        });
    }

    private void askSupportFeedback(){
        showDialog(activity.getString(R.string.support_feedback), new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(activity.getString(R.string.support_email_address)));
                intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.support_email_subject));
                activity.startActivity(intent);
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return null;
            }
        });
    }

    private void showDialog(String message, final Callable<Void> positiveAction, final Callable<Void> negativeAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try{
                    positiveAction.call();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try{
                    negativeAction.call();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });
        AlertDialog saveDialog = builder.create();
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.show();
    }
}
