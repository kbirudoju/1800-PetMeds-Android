package com.petmeds1800.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.petmeds1800.R;

import android.content.Context;

import javax.inject.Inject;

/**
 * Created by arthur on 04/03/15.
 */
public class AnalyticsUtil {

    @Inject
    Context mContext;
    @Inject Tracker mTracker;

    public void trackScreen(final ScreenName screen) {
        mTracker.setScreenName(mContext.getString(screen.getScreenNameRef()));
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    public void trackEvent(final Event event, final String label) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(mContext.getString(event.getCategoryRef()))
                .setAction(mContext.getString(event.getActionRef()))
                .setLabel(label)
                .build());
    }

    public static enum ScreenName {

        SPLASH(R.string.activity_splash),
        HOME(R.string.activity_home),
        LOREM(R.string.fragment_lorem);

        private final int mScreenNameRef;

        private ScreenName(final int resourceId) {
            mScreenNameRef = resourceId;
        }

        public int getScreenNameRef() {
            return mScreenNameRef;
        }
    }

    public static enum Event {

        LINK_CLICK(R.string.link_article, R.string.link_action);

        private final int mCategoryRef;
        private final int mActionRef;

        private Event(final int categoryRef, final int actionRef) {
            mCategoryRef = categoryRef;
            mActionRef = actionRef;
        }

        public int getCategoryRef() {
            return mCategoryRef;
        }

        public int getActionRef() {
            return mActionRef;
        }
    }
}
