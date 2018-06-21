package com.sreyas.cnstapmonitor.Tap;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.Models.TapRecord;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sreyas on 1/24/2018.
 */

public class TapViewModel {
    private boolean ready = true;
    private int tapCount = 0;
    private double timeLeft = -1;
    private Set<TapListener> tapListeners = new HashSet<>();

    private CountDownTimer countDownTimer = new CountDownTimer(5100, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            setTimeLeft(((millisUntilFinished - 100) / 1000.0));
        }

        @Override
        public void onFinish() {
            finished();
            ready = false;
        }
    };

    public TapViewModel(){}

    int getTapCount() {
        return tapCount;
    }

    public void tap(){
        if(timeLeft < 0){
            if(ready){
                countDownTimer.start();
                ready = false;
            }
        }
        else {
            setTapCount(tapCount + 1);
        }
    }

    public void reset(){
        setTapCount(0);
        setTimeLeft(-1);
        countDownTimer.cancel();
        ready = true;
    }

    public void addTapListener(TapListener tapListener){
        tapListeners.add(tapListener);
    }

    public void removeTapListener(TapListener tapListener){
        tapListeners.remove(tapListener);
    }

    private void setTimeLeft(double val){
        timeLeft = val;
        for (TapListener tapListener: tapListeners) {
            tapListener.onTimeLeftChanged(val);
        }
    }

    private void setTapCount(int val){
        tapCount = val;
        for (TapListener tapListener: tapListeners) {
            tapListener.onTapCountChanged(val);
        }
    }

    private void finished(){
        setTimeLeft(-1);
        for (TapListener tapListener: tapListeners) {
            tapListener.onFinished();
        }
    }

    public void addTapRecord(Context context){
        TapData.addTapRecord(new TapRecord(System.currentTimeMillis() / 60000, tapCount), context);
    }

    public interface TapListener{
        void onTimeLeftChanged(double timeLeft);

        void onTapCountChanged(int tapCount);

        void onFinished();
    }
}