package com.sreyas.cnstapmonitor;

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
    private int tapCountTest;
    private double timeLeftTest;

    @Before
    public  void setUp(){
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
                        tapCountTest = tapCount;
                    }

                    @Override
                    public void onFinished() {
                    }
                };
                tapViewModel.addTapListener(tapListener);
            }
        });
    }

    @Test
    public void getTapCount(){
        taps(3);
        assertEquals(2, tapCountTest);
    }

    @Test
    public void reset(){
        taps(5);
        tapViewModel.reset();
        assertEquals(0, tapCountTest);
        assertEquals(-1, timeLeftTest, .0001);
        taps(3);
        assertEquals(2, tapCountTest);
        assertEquals(4.7, timeLeftTest, .1);
    }

    @Test
    public void finished(){
        taps(5);
        sleep(6000);
        assertEquals(-1, timeLeftTest, .0001);
    }

    private void taps(int count){
        for(int i = 0;i < count;i++){
            tapViewModel.tap();
            sleep(100);
        }
    }

    private void sleep(long millis){
        try{
            Thread.sleep(millis);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
