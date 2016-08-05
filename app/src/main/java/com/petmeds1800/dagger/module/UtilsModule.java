package com.petmeds1800.dagger.module;

import com.petmeds1800.util.FileUtils;
import com.petmeds1800.util.PermissionUtils;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gguser on 10/10/14.
 */
@Module
public class UtilsModule {

    public UtilsModule() {
    }

    @Provides @Singleton
    PermissionUtils providePermissionsUtils(Application app) {
        return new PermissionUtils(app);
    }

    @Provides @Singleton
    FileUtils provideFileUtils() {
        return new FileUtils();
    }

}
