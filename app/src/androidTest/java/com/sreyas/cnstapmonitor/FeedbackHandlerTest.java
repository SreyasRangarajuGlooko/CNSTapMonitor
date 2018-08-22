package com.sreyas.cnstapmonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.util.Log;

import com.sreyas.cnstapmonitor.Models.FeedbackHandler;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

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
import static org.junit.Assert.assertTrue;


public class FeedbackHandlerTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule(MainActivity.class);

    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FeedbackHandler feedbackHandler = new FeedbackHandler();

    @Before
    public void setup() {
        activity = intentsTestRule.getActivity();
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setUserRatedApp(0);
        setSaveCount(9);
    }

    @Test
    public void getFeedbackMessage() {
        setUserRatedApp(0);
        setSaveCount(1);
        assertFeedbackMessageFromSave(false);
        setSaveCount(10);
        assertFeedbackMessageFromSave(true);
        setSaveCount(90);
        assertFeedbackMessageFromSave(true);
        setSaveCount(100);
        assertFeedbackMessageFromSave(false);
        setSaveCount(180);
        assertFeedbackMessageFromSave(true);
        setUserRatedApp(1);
        setSaveCount(1);
        assertFeedbackMessageFromSave(false);
        setSaveCount(10);
        assertFeedbackMessageFromSave(false);
        setSaveCount(90);
        assertFeedbackMessageFromSave(false);

        int randInt = new Random().nextInt(100);
        assertEquals("",
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.save_message,
                        randInt),
                        0, randInt, activity));
        assertEquals(activity.getString(R.string.support_feedback),
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.enjoy_app),
                        0, randInt, activity));
        assertEquals(activity.getString(R.string.rate_app),
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.enjoy_app),
                        1, randInt, activity));
        assertEquals("",
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.rate_app),
                        0, randInt, activity));
        assertEquals(activity.getString(R.string.feedback_action),
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.rate_app),
                        1, randInt, activity));
        assertEquals("",
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.support_feedback),
                        0, randInt, activity));
        assertEquals(activity.getString(R.string.feedback_action),
                feedbackHandler.getFeedbackMessage(activity.getString(R.string.support_feedback),
                        1, randInt, activity));
    }

    @Test
    public void getFeedbackAction(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.rate_link)));
        assertTrue(intentEquals(intent, feedbackHandler.getFeedbackAction(activity.getString(R.string.rate_app), activity)));
        intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(activity.getString(R.string.support_email_address)));
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.support_email_subject));
        assertTrue(intentEquals(intent, feedbackHandler.getFeedbackAction(activity.getString(R.string.support_feedback), activity)));
    }

    @Test
    public void incrementSaveCount(){
        feedbackHandler.incrementSaveCount(activity);
        assertEquals(10, sharedPreferences.getInt(activity.getString(R.string.save_count), 0));
    }

    @Test
    public void noFeedback() {
        TestUtil.performTapTest(3);
        onView(withText(activity.getString(R.string.enjoy_app))).check(matches(isDisplayed()));
        onView(withText("NO")).perform(click());
        onView(withText(activity.getString(R.string.support_feedback))).check(matches(isDisplayed()));
        onView(withText("NO")).perform(click());
        onView(withId(R.id.tap_info)).check(matches(isDisplayed()));
        checkTimerReset();
    }

    @Test
    public void noRating() {
        TestUtil.performTapTest(3);
        onView(withText(activity.getString(R.string.enjoy_app))).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
        onView(withText(activity.getString(R.string.rate_app))).check(matches(isDisplayed()));
        onView(withText("NO")).perform(click());
        onView(withId(R.id.tap_info)).check(matches(isDisplayed()));
        checkTimerReset();
    }

    @Test
    public void feedback() {
        TestUtil.performTapTest(3);
        onView(withText(activity.getString(R.string.enjoy_app))).check(matches(isDisplayed()));
        onView(withText("NO")).perform(click());
        onView(withText(activity.getString(R.string.support_feedback))).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
        Matcher expectedIntent = allOf(hasAction(Intent.ACTION_SENDTO),
                hasData(Uri.parse(activity.getString(R.string.support_email_address))),
                hasExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.support_email_subject)));
        intended(expectedIntent);
    }

    @Test
    public void rating() {
        TestUtil.performTapTest(3);
        onView(withText(activity.getString(R.string.enjoy_app))).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
        onView(withText(activity.getString(R.string.rate_app))).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
        Matcher expectedIntent = allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(activity.getString(R.string.rate_link))));
        intended(expectedIntent);
        assertEquals(1, sharedPreferences.getInt(activity.getString(R.string.user_rated_app), 0));
    }

    private void checkTimerReset() {
        onView(withId(R.id.tap_count)).check(matches(withText("0")));
        onView(withId(R.id.tap_info)).check(matches(withText(activity.getString(R.string.tap_info))));
    }

    private void setUserRatedApp(int feedbackCheck) {
        editor.putInt(activity.getString(R.string.user_rated_app), feedbackCheck);
        editor.apply();
    }

    private void setSaveCount(int saveCount) {
        editor.putInt(activity.getString(R.string.save_count), saveCount);
        editor.apply();
    }

    private void assertFeedbackMessageFromSave(boolean feedbackCheck) {
        int randInt = new Random().nextInt(100);
        if (feedbackCheck) {
            assertEquals(activity.getString(R.string.enjoy_app),
                    feedbackHandler.getFeedbackMessage(activity.getString(R.string.save_message, randInt),
                            1, randInt, activity));
        } else {
            assertEquals("", feedbackHandler.getFeedbackMessage(activity.getString(R.string.save_message,
                    randInt),
                    1, randInt, activity));
        }
    }

    private boolean intentEquals(Intent intent1, Intent intent2){
        return intent1.filterEquals(intent2) &&
                ((intent1.getExtras() == null && intent2.getExtras() == null) ||
                        intent1.getExtras().toString().equals(intent2.getExtras().toString()));
    }
}
