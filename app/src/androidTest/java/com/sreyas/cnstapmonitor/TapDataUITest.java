package com.sreyas.cnstapmonitor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.Models.TapRecord;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class TapDataUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void addTapRecord(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        for(int j = 0;j < 3;j++){
            for(int i = 0;i < 6; i++){
                onView(withId(R.id.tap_count)).perform(click());
            }
            sleep(5000);
            onView(withId(android.R.id.button1)).perform(click());
        }
        onView(withText("DATA")).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return ((RecyclerView) item).getAdapter().getItemCount() == 3;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        ArrayList<TapRecord> tapRecords = TapData.getTapData(appContext);
        assertEquals(5, tapRecords.get(tapRecords.size() - 1).getNumTaps());
    }

    @Test
    public void deleteTapRecord(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        for(int i = 0;i < 3; i++){
            onView(withId(R.id.tap_count)).perform(click());
        }
        sleep(5000);
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("DATA")).perform(click());
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(R.id.delete_item).performClick();
            }
        }));
        onView(withId(R.id.recycler_view)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return ((RecyclerView) item).getAdapter().getItemCount() == 3;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        ArrayList<TapRecord> tapRecords = TapData.getTapData(appContext);
        assertEquals(2, tapRecords.get(tapRecords.size() - 1).getNumTaps());
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
