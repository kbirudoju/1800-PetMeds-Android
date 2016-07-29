package com.dminc.application;

import com.dminc.application.ui.HomeActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(application = TestTemplateApplication.class,
        constants = BuildConfig.class,
        sdk = 16,
        packageName = "com.dminc.application")
// Make sure to test on a RELEASE build variant
public class HomeActivityUnitTest {

    private HomeActivity mActivity;

    @Before
    public void setup() {
        mActivity = Robolectric.setupActivity(HomeActivity.class);
    }

    @Test
    public void myActivityAppearsAsExpectedInitially() {
        String expectedTitle = "Template Application";
        assertEquals(mActivity.getTitle(), expectedTitle);
        // do the same test with assertJ, it's easier to read the code and it has better error messages
        assertThat(mActivity).hasTitle(expectedTitle);
    }

    @Test
    public void testSavingInstanceState() {
        HomeActivity activity = Robolectric.buildActivity(HomeActivity.class).create().start().resume().visible().get();
        assertEquals("initial_value", activity.getMyTestString());
        // Destroy and re-create activity
        activity.recreate();
        assertThat(activity).isNotNull();
        assertEquals("saving_state", activity.getMyTestString());
    }
}
