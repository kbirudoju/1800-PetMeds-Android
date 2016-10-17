package com.petmeds1800.dagger.module;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.dagger.scopes.AppScope;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin on 7/6/15.
 */
@Module
public class ApplicationModule {
    private final PetMedsApplication mApp;

    public ApplicationModule(PetMedsApplication app) {
        this.mApp = app;
    }

    @Provides
    @AppScope
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @AppScope
    Context provideContext(){
        return mApp;
    }
}
