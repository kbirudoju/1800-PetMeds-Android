package com.petmeds1800;

import com.petmeds1800.dagger.module.AppComponent;
import com.petmeds1800.dagger.module.ApplicationModule;
import com.petmeds1800.dagger.module.DaggerAppComponent;
import com.petmeds1800.dagger.module.RestModule;

import android.app.Application;

import timber.log.Timber;

public class PetMedsApplication extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppComponent = createAppComponent();
    }

    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restModule(new RestModule(this))
                .build();
    }

    public static synchronized AppComponent getAppComponent() {
        return mAppComponent;
    }


}
