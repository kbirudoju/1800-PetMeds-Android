package com.petmeds1800.mvp.LoginTask;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Digvijay on 8/4/2016.
 */
@Module
public class LoginPresenterModule {

    private final LoginContract.View mView;

    public LoginPresenterModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    LoginContract.View provideLoginView() {
        return mView;
    }

}
