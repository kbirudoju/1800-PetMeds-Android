package com.petmeds1800;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.petmeds1800.dagger.component.AppComponent;
import com.petmeds1800.dagger.component.DaggerAppComponent;
import com.petmeds1800.dagger.module.ApplicationModule;
import com.petmeds1800.dagger.module.RestModule;
import com.petmeds1800.dagger.module.StorageModule;
import com.testfairy.TestFairy;

import io.fabric.sdk.android.Fabric;

public class PetMedsApplication extends PetMedsBaseApplication {

    private static AppComponent mAppComponent;
    public static boolean menuItemsClicked = false;
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.enableCrashlytics == true) {
            Log.d("initialising","fabric");
            Fabric.with(this, new Crashlytics());
        }else{//this will be executed oly for proddebug
            Log.d("initialising","testfairy");
            TestFairy.begin(this, getString(R.string.testfairy_app_token));
        }
        mAppComponent = createAppComponent();

    }

    @Override
    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restModule(new RestModule(getString(R.string.server_endpoint), getApplicationContext()))
                .storageModule(new StorageModule())
                .build();
    }
    public  static synchronized AppComponent getAppComponent() {
        return mAppComponent;
    }
}
