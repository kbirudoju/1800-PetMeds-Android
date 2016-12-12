package com.petmeds1800;

import com.petmeds1800.dagger.component.AppComponent;
import com.petmeds1800.dagger.component.DaggerAppComponent;
import com.petmeds1800.dagger.module.ApplicationModule;
import com.petmeds1800.dagger.module.RestModule;
import com.petmeds1800.dagger.module.StorageModule;
import com.testfairy.TestFairy;

public class PetMedsApplication extends PetMedsBaseApplication {

    private static AppComponent mAppComponent;
    public static boolean menuItemsClicked = false;

    @Override
    public void onCreate() {
        super.onCreate();
        TestFairy.begin(this, getString(R.string.testfairy_app_token));
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
