package com.sreyas.cnstapmonitor;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class TestUtil {
    static void sleep(long millis){
        try{
            Thread.sleep(millis);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    static void performTapTest(int tapCount){
        for(int i = 0;i < tapCount; i++){
            onView(withId(R.id.tap_count)).perform(click());
        }
        TestUtil.sleep(5000);
        onView(withId(android.R.id.button1)).perform(click());
    }
}
