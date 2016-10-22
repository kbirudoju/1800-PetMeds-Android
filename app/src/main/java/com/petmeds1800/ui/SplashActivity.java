package com.petmeds1800.ui;

import com.petmeds1800.BuildConfig;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.intent.IntroIntent;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    public static final long DEFAULT_TIMEOUT = 1600;

    public static final long DEBUG_TIMEOUT = DEFAULT_TIMEOUT / 2;

    private static final String STATE_FINISH_TIME = "STATE_FINISH_TIME";

    private static FinishHandler sHandler;

    private long mFinishTime;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        PetMedsApplication.getAppComponent().inject(this);

        if (savedInstanceState != null) {
            mFinishTime = savedInstanceState.getLong(STATE_FINISH_TIME);
        } else if (BuildConfig.DEBUG) {
            mFinishTime = SystemClock.uptimeMillis() + DEBUG_TIMEOUT;
        } else {
            mFinishTime = SystemClock.uptimeMillis() + DEFAULT_TIMEOUT;
        }


    }

    public long getFinishTime() {
        return mFinishTime;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(STATE_FINISH_TIME, mFinishTime);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sHandler == null) {
            sHandler = new FinishHandler(mPreferencesHelper);
        }

        sHandler.setSplashActivity(this);
        sHandler.sendEmptyMessageAtTime(FinishHandler.MSG_FINISH, mFinishTime);
    }

    @Override
    protected void onStop() {
        super.onStop();

        sHandler.setSplashActivity(null);
        sHandler.removeMessages(FinishHandler.MSG_FINISH);

        if (isFinishing()) {
            sHandler = null;
        }
    }

    private static class FinishHandler extends Handler {

        public static final int MSG_FINISH = 1;

        private SplashActivity mSplashActivity;

        private GeneralPreferencesHelper mPreferencesHelper;

        public FinishHandler(GeneralPreferencesHelper preferencesHelper) {
            mPreferencesHelper = preferencesHelper;
        }

        public void setSplashActivity(final SplashActivity splashActivity) {
            mSplashActivity = splashActivity;
        }

        @Override
        public void handleMessage(final Message msg) {

            if (msg.what == MSG_FINISH && mSplashActivity != null) {
                if (!mPreferencesHelper.getHaUserSeenIntro()) {
                    if (mPreferencesHelper.getIsUserLoggedIn()) {
                        mSplashActivity.startActivity(new HomeIntent(mSplashActivity));
                    } else {
                        mSplashActivity.startActivity(new LoginIntent(mSplashActivity));
                    }
                } else {
                    mSplashActivity.startActivity(new IntroIntent(mSplashActivity));
                }
                mSplashActivity.finish();
            }
        }
    }
}

