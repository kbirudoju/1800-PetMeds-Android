package com.petmeds1800.dagger.module;

import com.petmeds1800.dagger.AppScope;
import com.petmeds1800.util.FileUtils;
import com.petmeds1800.util.PermissionUtils;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gguser on 10/10/14.
 */
@Module
public class UtilsModule {

    public UtilsModule() {
    }

    @Provides @AppScope
    PermissionUtils providePermissionsUtils(Application app) {
        return new PermissionUtils(app);
    }

    @Provides @AppScope
    FileUtils provideFileUtils() {
        return new FileUtils();
    }

}
