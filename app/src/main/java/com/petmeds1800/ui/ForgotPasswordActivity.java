package com.petmeds1800.ui;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.mvp.ForgotPasswordTask.DaggerForgotPasswordComponent;
import com.petmeds1800.mvp.ForgotPasswordTask.ForgotPasswordPresenter;
import com.petmeds1800.mvp.ForgotPasswordTask.ForgotPasswordPresenterModule;
import com.petmeds1800.ui.fragments.ForgotPasswordFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * Created by Digvijay on 8/22/2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    @Inject
    ForgotPasswordPresenter mForgotPasswordPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setupComponent();
    }

    private void setupComponent() {
        ForgotPasswordFragment forgotPasswordFragment = (ForgotPasswordFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forgot_password);
        DaggerForgotPasswordComponent.builder()
                .appComponent(PetMedsApplication.getAppComponent())
                .forgotPasswordPresenterModule(new ForgotPasswordPresenterModule(forgotPasswordFragment))
                .build().inject(this);
    }
}
