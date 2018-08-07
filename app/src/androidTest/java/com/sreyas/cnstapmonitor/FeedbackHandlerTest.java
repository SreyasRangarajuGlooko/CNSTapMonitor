package com.sreyas.cnstapmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.sreyas.cnstapmonitor.Models.FeedbackHandler;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;


public class FeedbackHandlerTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule(MainActivity.class);

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Before
    public void setup(){
        sharedPreferences = intentsTestRule.getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setUserRatedApp(0);
        setSaveCount(9);
    }

    @Test
    public void askFeedbackCheck(){
        FeedbackHandler feedbackHandler = new FeedbackHandler(intentsTestRule.getActivity());
        setUserRatedApp(0);
        setSaveCount(1);
        assertEquals(false, feedbackHandler.askFeedbackCheck());
        setSaveCount(10);
        assertEquals(true, feedbackHandler.askFeedbackCheck());
        setSaveCount(90);
        assertEquals(true, feedbackHandler.askFeedbackCheck());
        setSaveCount(100);
        assertEquals(false, feedbackHandler.askFeedbackCheck());
        setSaveCount(180);
        assertEquals(true, feedbackHandler.askFeedbackCheck());

        setUserRatedApp(1);
        setSaveCount(1);
        assertEquals(false, feedbackHandler.askFeedbackCheck());
        setSaveCount(10);
        assertEquals(false, feedbackHandler.askFeedbackCheck());
        setSaveCount(90);
        assertEquals(false, feedbackHandler.askFeedbackCheck());
        setSaveCount(100);
        assertEquals(false, feedbackHandler.askFeedbackCheck());
        setSaveCount(180);
        assertEquals(false, feedbackHandler.askFeedbackCheck());

    }

    @Test
    public void noFeedback(){
        TestUtil.performTapTest(3);
        onView(withText("NO")).perform(click());
        onView(withText("NO")).perform(click());
        onView(withId(R.id.tap_info)).check(matches(isDisplayed()));
    }

    @Test
    public void noRating(){
        TestUtil.performTapTest(3);
        onView(withText("YES")).perform(click());
        onView(withText("NO")).perform(click());
        onView(withId(R.id.tap_info)).check(matches(isDisplayed()));
    }

    @Test
    public void feedback(){
        TestUtil.performTapTest(3);
        onView(withText("NO")).perform(click());
        onView(withText("YES")).perform(click());
        Matcher expectedIntent = allOf(hasAction(Intent.ACTION_SENDTO),
                hasData(Uri.parse(intentsTestRule.getActivity().getString(R.string.support_email_address))),
                hasExtra(Intent.EXTRA_SUBJECT, intentsTestRule.getActivity().getString(R.string.support_email_subject)));
        intended(expectedIntent);
    }

    @Test
    public void rating(){
        TestUtil.performTapTest(3);
        onView(withText("YES")).perform(click());
        onView(withText("YES")).perform(click());
        Matcher expectedIntent = allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(intentsTestRule.getActivity().getString(R.string.rate_link))));
        intended(expectedIntent);
        assertEquals(1, sharedPreferences.getInt(intentsTestRule.getActivity().getString(R.string.user_rated_app), 0));
    }

    private void setUserRatedApp(int feedbackCheck){
        editor.putInt(intentsTestRule.getActivity().getString(R.string.user_rated_app), feedbackCheck);
        editor.apply();
    }

    private void setSaveCount(int saveCount){
        editor.putInt(intentsTestRule.getActivity().getString(R.string.save_count), saveCount);
        editor.apply();
    }
}
