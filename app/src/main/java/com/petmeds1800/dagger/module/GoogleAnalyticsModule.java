package com.petmeds1800.dagger.module;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import com.petmeds1800.BuildConfig;
import com.petmeds1800.R;
import com.petmeds1800.dagger.AppScope;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gguser on 10/10/14.
 */
@Module
public class GoogleAnalyticsModule {

    public GoogleAnalyticsModule() {
    }

    @Provides @AppScope
    Tracker provideTracker(Application app) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(app);
        if (BuildConfig.DEBUG) {
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.setDryRun(true);
        }
        return analytics.newTracker(app.getString(R.string.tracking_id));
    }

}
