package com.petmeds1800.mvp.SignupTask;

import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpPresenter implements SignUpContract.Presenter {

    @NonNull
    private final SignUpContract.View mSignUpView;

    @Inject
    public SignUpPresenter(@NonNull SignUpContract.View signUpView) {
        mSignUpView = signUpView;
    }

    @Override
    public boolean validateEmail(String email) {
        return false;
    }

    @Override
    public boolean validatePassword(String password) {
        return false;
    }

    @Override
    public boolean validateUserName(String name) {
        return false;
    }

    @Override
    public boolean validateAddress(String address) {
        return false;
    }

    @Override
    public boolean validateCity(String city) {
        return false;
    }

    @Override
    public boolean validatePostalCode(String postalCOde) {
        return false;
    }

    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        return false;
    }

    @Override
    public void start() {

    }
}
