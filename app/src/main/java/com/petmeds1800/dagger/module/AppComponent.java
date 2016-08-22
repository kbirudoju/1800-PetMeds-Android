package com.petmeds1800.dagger.module;

import com.google.android.gms.analytics.Tracker;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.dagger.AppScope;
import com.petmeds1800.util.FileUtils;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.PermissionUtils;

import android.app.Application;

import dagger.Component;

@AppScope
@Component(modules = {
        ApplicationModule.class,
        RestModule.class,
        GoogleAnalyticsModule.class,
        UtilsModule.class,
        StorageModule.class
})
public interface AppComponent extends Injector{
    Application app();

    Tracker tracker();

    PermissionUtils permissionUtils();

    FileUtils fileUtils();

    PetMedsApiService api();

    GeneralPreferencesHelper generalPreferencesHelper();

}
