package com.sreyas.cnstapmonitor;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import com.sreyas.cnstapmonitor.Tap.TapViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TapViewModelTest {
    private TapViewModel tapViewModel;
    private TapViewModel.TapListener tapListener;
    private int tapCount;
    private double timeLeftTest;

    @Before
    public void setUp(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                tapViewModel = new TapViewModel();
                tapListener = new TapViewModel.TapListener() {
                    @Override
                    public void onTimeLeftChanged(double timeLeft) {
                        timeLeftTest = timeLeft;
                    }

                    @Override
                    public void onTapCountChanged(int tapCount) {
                        TapViewModelTest.this.tapCount = tapCount;
                    }

                    @Override
                    public void onFinished() {
                    }

                    @Override
                    public void onFeedbackReady(String string) {
                    }

                    @Override
                    public void onSupport(Intent intent) {
                    }
                };
                tapViewModel.addTapListener(tapListener);
            }
        });
    }

    @Test
    public void respondToDialog(){
        //ToDO
    }

    @Test
    public void getTapCount(){
        tap(3);
        assertEquals(2, tapViewModel.getTapCount());
    }

    @Test
    public void reset(){
        tap(5);
        tapViewModel.reset();
        assertEquals(0, tapCount);
        assertEquals(-1, timeLeftTest, .0001);
        tap(3);
        assertEquals(2, tapCount);
        assertEquals(4.7, timeLeftTest, .1);
    }

    @Test
    public void finished(){
        tap(5);
        TestUtil.sleep(6000);
        assertEquals(0, timeLeftTest, .0001);
    }

    private void tap(int count){
        for(int i = 0;i < count;i++){
            tapViewModel.tap();
            TestUtil.sleep(100);
        }
    }
}
