package com.petmeds1800;

import com.petmeds1800.dagger.component.AppComponent;
import com.urbanairship.UAirship;

import android.support.multidex.MultiDexApplication;

public abstract class PetMedsBaseApplication extends MultiDexApplication {

    private static AppComponent mAppComponent;
    public static boolean menuItemsClicked = false;

    @Override
    public void onCreate() {
        super.onCreate();
       UAirship.takeOff(this, new UAirship.OnReadyCallback() {
           @Override
           public void onAirshipReady(UAirship airship) {
           }
       });
        mAppComponent = createAppComponent();
    }
    protected abstract AppComponent createAppComponent();
   /* protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restModule(new RestModule(getString(R.string.server_endpoint), getApplicationContext()))
                .storageModule(new StorageModule())
                .build();
    }*/



    }

