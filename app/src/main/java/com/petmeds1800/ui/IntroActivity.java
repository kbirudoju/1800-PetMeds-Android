package com.petmeds1800.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.ui.fragments.IntroFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

/**
 * Created by Digvijay on 9/1/2016.
 */
public class IntroActivity extends AppCompatActivity implements IntroFragment.OnIntroFinishedListener{

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void onIntroFragmentFinished() {
        mPreferencesHelper.setHasUserSeenIntro(true);
        startActivity(new LoginIntent(this));
        finish();
    }
}
