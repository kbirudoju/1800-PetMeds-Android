package com.petmeds1800.mvp.ForgotPasswordTask;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Digvijay on 8/19/2016.
 */
@Module
public class ForgotPasswordPresenterModule {

    private final ForgotPasswordContract.View mView;

    public ForgotPasswordPresenterModule(ForgotPasswordContract.View view) {
        mView = view;
    }

    @Provides
    ForgotPasswordContract.View provideForgotPasswordView(){
        return mView;
    }
}
