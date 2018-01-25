package com.sreyas.cnstapmonitor.TapView;

import android.os.CountDownTimer;

/**
 * Created by Sreyas on 1/24/2018.
 */

public class TapViewLogic {
    static final int ready = 0, waiting = -1, running = 1;
    private int tapCount = 0;
    private TapFragment tapFragment;
    private int tapStarted = 0;

    private CountDownTimer countDownTimer = new CountDownTimer(5100, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            if(tapStarted == ready){
                tapStarted = running;
            }
            tapFragment.timeIntervalUpdate((millisUntilFinished - 100) / 1000.0);
        }

        @Override
        public void onFinish() {
            tapFragment.timeFinished();
            tapStarted = waiting;
        }
    };

    TapViewLogic(TapFragment tapFragment){
        this.tapFragment = tapFragment;
    }

    void incrementTapCount(){
        if(tapStarted == running){
            tapCount += 1;
        }
        if(tapStarted == ready){
            countDownTimer.start();
        }
    }

    int getTapCount() {
        return tapCount;
    }

    void resetTapCount(){
        tapCount = 0;
    }

    void setReady(){
        tapStarted = ready;
    }
}
