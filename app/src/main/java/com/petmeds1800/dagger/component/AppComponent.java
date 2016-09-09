package com.petmeds1800.dagger.component;

import com.google.android.gms.analytics.Tracker;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.dagger.scopes.AppScope;
import com.petmeds1800.dagger.Injector;
import com.petmeds1800.dagger.module.ApplicationModule;
import com.petmeds1800.dagger.module.GoogleAnalyticsModule;
import com.petmeds1800.dagger.module.RestModule;
import com.petmeds1800.dagger.module.StorageModule;
import com.petmeds1800.dagger.module.UtilsModule;
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
public interface AppComponent extends Injector {
    Application app();

    Tracker tracker();

    PermissionUtils permissionUtils();

    FileUtils fileUtils();

    PetMedsApiService api();

    GeneralPreferencesHelper generalPreferencesHelper();

}
