package com.petmeds1800.mvp.ForgotPasswordTask;

import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Digvijay on 8/19/2016.
 */
public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    @NonNull
    private final ForgotPasswordContract.View mForgotPasswordView;

    @Inject
    ForgotPasswordPresenter(@NonNull ForgotPasswordContract.View forgotPasswordView){
        mForgotPasswordView = forgotPasswordView;
    }

    @Inject
    void setupListener(){
        mForgotPasswordView.setPresenter(this);
    }

    @Override
    public boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void start() {

    }
}
