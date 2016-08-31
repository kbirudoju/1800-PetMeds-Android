package com.petmeds1800.mvp.LoginTask;

import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Digvijay on 8/4/2016.
 */

/**
 * Listens to user actions from the UI ({@link com.petmeds1800.ui.fragments.LoginFragment}), retrieves the data and
 * updates the UI as required. <p /> By marking the constructor with {@code @Inject}, Dagger injects the dependencies
 * required to create an instance of the LoginPresenter (if it fails, it emits a compiler error). It uses {@link
 * LoginPresenterModule} to do so. <p/> Dagger generated code doesn't require public access to the constructor or class,
 * and therefore, to ensure the developer doesn't instantiate the class manually bypassing Dagger, it's good practice
 * minimise the visibility of the class/constructor as much as possible.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private static final int PASSWORD_LENGTH = 8;

    @NonNull
    private final LoginContract.View mLoginView;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected with {@code @Nullable}
     * values.
     */
    @Inject
    LoginPresenter(@NonNull LoginContract.View loginView) {
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
    public boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validatePassword(String password) {
        return password.length() >= PASSWORD_LENGTH;
    }

    @Override
    public void start() {

    }

}
