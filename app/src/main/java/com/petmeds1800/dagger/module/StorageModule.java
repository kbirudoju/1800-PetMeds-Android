package com.petmeds1800.dagger.module;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.petmeds1800.dagger.scopes.AppScope;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;

/**
 * Created by Digvijay on 8/19/2016.
 */
@Module
public class StorageModule {

    @Provides
    @AppScope
    public GeneralPreferencesHelper providePreferencesHelper(Context context) {
        return new GeneralPreferencesHelper(context);
    }

    @Provides
    public CookieJar providePersistentCookie(Context context){
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }
}
