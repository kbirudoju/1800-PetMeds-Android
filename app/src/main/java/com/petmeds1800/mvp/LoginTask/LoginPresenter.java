package com.petmeds1800.mvp.LoginTask;

import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Digvijay on 8/4/2016.
 */
public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private final LoginContract.View mLoginView;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected with {@code @Nullable}
     * values.
     */
    @Inject
    LoginPresenter(LoginContract.View loginView) {
        mLoginView = loginView;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created. For more information,
     * see Java Concurrency in Practice.
     */
    @Inject
    void setupListener() {
        mLoginView.setPresenter(this);
    }

    @Override
    public void validateCredentials(String username, String password) {

        if(mLoginView != null) {
            doLogin(username, password);
        }
    }

    @Override
    public void start() {

    }

    private void doLogin(String username, String password) {

    }
}
