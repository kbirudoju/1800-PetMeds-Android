package com.petmeds1800.module;

import com.google.android.gms.analytics.Tracker;

import com.petmeds1800.api.ApiService;
import com.petmeds1800.util.FileUtils;
import com.petmeds1800.util.PermissionUtils;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        RestModule.class,
        GoogleAnalyticsModule.class,
        UtilsModule.class
})
public interface AppComponent extends Injector {
    Application app();
    Retrofit retrofit();
    Tracker tracker();
    PermissionUtils permissionUtils();
    FileUtils fileUtils();
    ApiService api();
}
