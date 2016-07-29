package com.dminc.application;

import com.dminc.application.ui.SplashActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.widget.TextView;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(application = TestTemplateApplication.class,
        constants = BuildConfig.class,
        sdk = 16,
        packageName = "com.dminc.application")
// Make sure to test on a RELEASE build variant
public class SplashActivityUnitTest {

    private SplashActivity mActivity;

    @Before
    public void setup() {
        mActivity = Robolectric.buildActivity(SplashActivity.class).create().get();
    }

    @Test
    public void myActivityHasNoTitle() {
        String expectedTitle = "Template Application";
        assertThat(mActivity).hasTitle(expectedTitle);
    }

    @Test
    public void myActivityAppearsAsExpectedInitially() {
        String expectedText = "Template Application";

        TextView layout = (TextView) mActivity.findViewById(R.id.text_splash_screen);
        assertThat(layout).containsText(expectedText);
    }


    @Test
    public void testSavingInstanceState() {
        SplashActivity activity = Robolectric.buildActivity(SplashActivity.class).create().start().resume().visible().get();
        long initialFinishTime = activity.getFinishTime();

        // Destroy and re-create activity
        activity.recreate();

        assertThat(activity).isNotNull();
        assertEquals(initialFinishTime, activity.getFinishTime());
    }

    @Test
    public void testNewFinishTimeForNewActivity() {
        SplashActivity activity = Robolectric.buildActivity(SplashActivity.class).create().start().resume().visible().get();
        long finishTime = activity.getFinishTime();

        Robolectric.getForegroundThreadScheduler().advanceBy(100);
        Robolectric.getBackgroundThreadScheduler().advanceBy(100);
        // Destroy and create a new activity
        SplashActivity newActivity = Robolectric.buildActivity(SplashActivity.class).create().start().resume().visible().get();

        assertThat(newActivity).isNotNull();
        assertNotEquals(finishTime, newActivity.getFinishTime());
    }
}
