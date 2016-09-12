package com.petmeds1800.mvp.SignupTask;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Digvijay on 9/9/2016.
 */
@Module
public class SignUpPresenterModule {

    private final SignUpContract.View mView;

    public SignUpPresenterModule(SignUpContract.View view) {
        mView = view;
    }

    @Provides
    SignUpContract.View provideSignUpView(){
        return mView;
    }
}
