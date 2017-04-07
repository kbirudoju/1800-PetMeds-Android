package com.petmeds1800.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.ui.resetpassword.ResetPasswordFragment;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

public class ResetPasswordActivity extends AbstractActivity {


    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.reset_password_toolbar_title));
        enableBackButton();
        onNewIntent(getIntent());
    }

    public void showProgress() {
        try {
            startLoadingGif(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void hideProgress() {
        try {
            stopLoadingGif(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Uri data = intent.getData();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            replaceResetPasswordFragment(ResetPasswordFragment.newInstance(data.getQueryParameter(Constants.RESET_TOKEN_KEY)), ResetPasswordFragment.class.getSimpleName());
        } else {
            replaceResetPasswordFragment(ResetPasswordFragment.newInstance(null), ResetPasswordFragment.class.getSimpleName());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reset_password;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new HomeIntent(this);
        startActivity(intent);
        this.finishAffinity();
    }
}
