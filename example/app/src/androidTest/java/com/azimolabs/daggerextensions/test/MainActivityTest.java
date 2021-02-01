package com.azimolabs.daggerextensions.test;


import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.azimolabs.daggerextensions.example.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkIfProperFieldsAreDisplayed() {
        onView(allOf(isAssignableFrom(TextView.class),
                withText(equalToIgnoringCase("text provided by ExampleInterface"))))
                .check(matches(isDisplayed()));

        onView(allOf(isAssignableFrom(TextView.class),
                withText(equalToIgnoringCase("text provided by ExampleSecondInterface"))))
                .check(matches(isDisplayed()));
    }
}