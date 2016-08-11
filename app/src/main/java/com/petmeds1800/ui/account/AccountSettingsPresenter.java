package com.petmeds1800.ui.account;

import android.support.annotation.NonNull;
import android.util.Log;

import com.petmeds1800.model.UserModel;
import com.petmeds1800.model.entities.User;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * Created by Abhinav on 4/8/16.
 */
public class AccountSettingsPresenter implements AccountSettingsContract.Presenter {

    private static final int PASSWORD_LENGTH = 3;
    private final UserModel mUserModel;

    @NonNull
    private final AccountSettingsContract.View mView;

    @Inject
    AccountSettingsPresenter(UserModel userModel , AccountSettingsContract.View settingsView) {
        mUserModel = userModel;
        mView = settingsView;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created. For more information,
     * see Java Concurrency in Practice.
     */
    @Inject
    void setupListener() {
        mView.setPresenter(this);
    }

    @Override
    public boolean validateName(String name) {
        return name.isEmpty()? false : true;
    }

    @Override
    public boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validatePassword(String password) {
        return password.length() >= PASSWORD_LENGTH ? true : false;
    }

    @Override
    public void findUserData() {

        //test data
        User user = new User();
        user.setmName("Abhinav");
        user.setmUsername("aagarwal@dminc.com");
        user.setmPassword("dontknow");

        if(mView.isActive()) {
            mView.setUserData(user);
        }
    }

    @Override
    public void saveSettings(String name, String username, String password) {
        if(mView.isActive()){
            mView.showSuccess();
        }
    }



    @Override
    public void start() {

    }
}
