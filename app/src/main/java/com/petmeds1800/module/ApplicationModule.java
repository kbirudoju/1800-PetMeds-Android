package com.petmeds1800.module;

import com.petmeds1800.TemplateApplication;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin on 7/6/15.
 */
@Module
public class ApplicationModule {
    private final TemplateApplication mApp;

    public ApplicationModule(TemplateApplication app) {
        this.mApp = app;
    }

    @Provides @Singleton
    Application provideApplication() {
        return mApp;
    }
}
