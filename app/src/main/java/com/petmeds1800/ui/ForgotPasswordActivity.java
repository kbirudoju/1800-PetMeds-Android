package com.petmeds1800.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.mvp.ForgotPasswordTask.DaggerForgotPasswordComponent;
import com.petmeds1800.mvp.ForgotPasswordTask.ForgotPasswordPresenter;
import com.petmeds1800.mvp.ForgotPasswordTask.ForgotPasswordPresenterModule;
import com.petmeds1800.ui.fragments.ForgotPasswordFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

/**
 * Created by Digvijay on 8/22/2016.
 */
public class ForgotPasswordActivity extends AbstractActivity {

    @Inject
    ForgotPasswordPresenter mForgotPasswordPresenter;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_password;
    }

    private void setupComponent() {
        ForgotPasswordFragment forgotPasswordFragment = (ForgotPasswordFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forgot_password);
        DaggerForgotPasswordComponent.builder()
                .appComponent(PetMedsApplication.getAppComponent())
                .forgotPasswordPresenterModule(new ForgotPasswordPresenterModule(forgotPasswordFragment))
                .build().inject(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setToolBarTitle(getString(R.string.title_forgot_password));
        enableBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if(mPreferencesHelper.getIsUserLoggedIn()){
                super.onBackPressed();
            }else{
                startActivity(new LoginIntent(this));
                this.finishAffinity();
            }

        }
        return super.onOptionsItemSelected(menuItem);
    }
}
