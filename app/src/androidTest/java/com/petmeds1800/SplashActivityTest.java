package com.petmeds1800;

import android.test.ActivityInstrumentationTestCase2;

import com.petmeds1800.ui.SplashActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

public class SplashActivityTest extends ActivityInstrumentationTestCase2<SplashActivity> {


    public SplashActivityTest() { super(SplashActivity.class); }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }


    public void testTextViewContainsAppName() {
        onView(withId(R.id.text_splash_screen))
                .check(matches(withText(containsString("Template Application"))));
    }

}
