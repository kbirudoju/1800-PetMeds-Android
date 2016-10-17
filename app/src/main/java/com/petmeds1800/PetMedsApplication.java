package com.petmeds1800;

import com.petmeds1800.dagger.component.AppComponent;
import com.petmeds1800.dagger.component.DaggerAppComponent;
import com.petmeds1800.dagger.module.ApplicationModule;
import com.petmeds1800.dagger.module.RestModule;
import com.petmeds1800.dagger.module.StorageModule;
import com.urbanairship.UAirship;

import android.support.multidex.MultiDexApplication;

public class PetMedsApplication extends MultiDexApplication {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        UAirship.takeOff(this, new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship airship) {

                // Enable user notifications
                airship.getPushManager().setUserNotificationsEnabled(true);
            }
        });

        mAppComponent = createAppComponent();
    }

    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restModule(new RestModule(getString(R.string.server_endpoint) , getApplicationContext()))
                .storageModule(new StorageModule())
                .build();
    }

    public static synchronized AppComponent getAppComponent() {
        return mAppComponent;
    }

}
