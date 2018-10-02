package com.sreyas.cnstapmonitor.Tap;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;

import com.sreyas.cnstapmonitor.Models.Analytics;
import com.sreyas.cnstapmonitor.Models.FeedbackHandler;
import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.Models.TapDataListener;
import com.sreyas.cnstapmonitor.Models.TapRecord;
import com.sreyas.cnstapmonitor.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sreyas on 1/24/2018.
 */

public class TapViewModel {
    private boolean ready = true;
    private int tapCount = 0;
    private double timeLeft = -1;
    private TapDataListener tapDataListener;
    private Set<TapListener> tapListeners = new HashSet<>();
    private FeedbackHandler feedbackHandler = new FeedbackHandler();

    private CountDownTimer countDownTimer = new CountDownTimer(5000, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            setTimeLeft((millisUntilFinished) / 1000.0);
        }

        @Override
        public void onFinish() {
            finished();
            ready = false;
        }
    };

    public TapViewModel() {
    }

    public interface TapListener {
        void onTimeLeftChanged(double timeLeft);

        void onTapCountChanged(int tapCount);

        void onFinished();

        void onFeedbackReady(String message);

        void onSupport(Intent intent);
    }

    public int getTapCount() {
        return tapCount;
    }

    public void tap() {
        if (timeLeft < 0) {
            if (ready) {
                countDownTimer.start();
            }
        } else if(ready){
            setTapCount(tapCount + 1);
        }
    }

    public void reset() {
        setTapCount(0);
        setTimeLeft(-1);
        countDownTimer.cancel();
        ready = true;
    }

    public void addTapListener(TapListener tapListener) {
        tapListeners.add(tapListener);
    }

    public void removeTapListener(TapListener tapListener) {
        tapListeners.remove(tapListener);
    }

    public void setTapDataListener(TapDataListener tapDataListener) {
        this.tapDataListener = tapDataListener;
    }

    public void respondToDialog(String message, int response, Activity activity) {
//        Analytics.logFeedbackEvent(message, ((response == 1) ? activity.getString(R.string.yes) : activity.getString(R.string.no)));
        if (message.equals(activity.getString(R.string.save_message, tapCount)) && response == 1) {
            newTapRecord(activity);
        }
        String feedback = feedbackHandler.getFeedbackMessage(message, response, tapCount, activity);
        if (feedback.isEmpty()) {
            reset();
        } else if (feedback.equals(activity.getString(R.string.feedback_action))) {
            Intent feedbackAction = feedbackHandler.getFeedbackAction(message, activity);
            if (feedbackAction != null) {
                for (TapListener tapListener : tapListeners) {
                    tapListener.onSupport(feedbackAction);
                }
            }
            reset();
        } else {
            for (TapListener tapListener : tapListeners) {
                tapListener.onFeedbackReady(feedback);
            }
        }
    }

    private void newTapRecord(Activity activity) {
        TapData.addTapRecord(new TapRecord(System.currentTimeMillis() / 60000, tapCount), activity);
        feedbackHandler.incrementSaveCount(activity);
        tapDataListener.onTapDataChanged();
//        Analytics.logSaveEvent(feedbackHandler.getSaveCount(activity), tapCount, feedbackHandler.getUserRatedApp(activity));
    }

    private void setTimeLeft(double val) {
        timeLeft = val;
        for (TapListener tapListener : tapListeners) {
            tapListener.onTimeLeftChanged(val);
        }
    }

    private void setTapCount(int val) {
        tapCount = val;
        for (TapListener tapListener : tapListeners) {
            tapListener.onTapCountChanged(val);
        }
    }

    private void finished() {
        setTimeLeft(0);
        for (TapListener tapListener : tapListeners) {
            tapListener.onFinished();
        }
    }
}
