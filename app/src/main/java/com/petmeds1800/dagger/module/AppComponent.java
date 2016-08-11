package com.petmeds1800.dagger.module;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.util.FileUtils;
import com.petmeds1800.util.PermissionUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        RestModule.class,
        GoogleAnalyticsModule.class,
        UtilsModule.class
})
public interface AppComponent extends Injector{
    Application app();

    Tracker tracker();

    PermissionUtils permissionUtils();

    FileUtils fileUtils();

    PetMedsApiService api();

}
