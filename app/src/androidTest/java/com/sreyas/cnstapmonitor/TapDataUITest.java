package com.sreyas.cnstapmonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.Models.TapRecord;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class TapDataUITest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        clearTapData();
        zeroSaveCount();
    }

    @Test
    public void addTapRecord(){
        for(int i = 0;i < 3;i++){
            TapData.addTapRecord(new TapRecord(System.currentTimeMillis() / 60000, 10), activityTestRule.getActivity());
        }
        TestUtil.performTapTest(6);
        onView(withText("DATA")).perform(click());
        checkItemCount(4);
        ArrayList<TapRecord> tapRecords = TapData.getTapData(activityTestRule.getActivity());
        assertEquals(5, tapRecords.get(tapRecords.size() - 1).getNumTaps());
    }

    @Test
    public void deleteTapRecord(){
        for(int i = 0;i < 3;i++){
            TapData.addTapRecord(new TapRecord(System.currentTimeMillis() / 60000, 10), activityTestRule.getActivity());
        }
        TestUtil.performTapTest(3);
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
        checkItemCount(3);
        ArrayList<TapRecord> tapRecords = TapData.getTapData(activityTestRule.getActivity());
        assertEquals(2, tapRecords.get(tapRecords.size() - 1).getNumTaps());
    }

    @Test
    public void incrementSaveCount(){
        TapData.addTapRecord(new TapRecord(System.currentTimeMillis() / 60000, 10), activityTestRule.getActivity());
        assertEquals(1, activityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE)
                .getInt(activityTestRule.getActivity().getString(R.string.save_count), 0));
    }

    private void clearTapData(){
        TapData.getTapData(activityTestRule.getActivity()).clear();
        TapData.saveTapData(activityTestRule.getActivity());
    }

    private void zeroSaveCount(){
        SharedPreferences.Editor editor = activityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(activityTestRule.getActivity().getString(R.string.save_count), 0);
        editor.apply();
    }

    private void checkItemCount(final int itemCount){
        onView(withId(R.id.recycler_view)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return ((RecyclerView) item).getAdapter().getItemCount() == itemCount;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
    }

}
