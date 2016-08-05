package com.petmeds1800.dagger.module;

import com.petmeds1800.PetMedsApplication;

import android.app.Application;

import javax.inject.Singleton;

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

    @Provides @Singleton
    Application provideApplication() {
        return mApp;
    }
}
