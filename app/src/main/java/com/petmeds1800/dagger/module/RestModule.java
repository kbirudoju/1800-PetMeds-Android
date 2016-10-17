package com.petmeds1800.dagger.module;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.petmeds1800.BuildConfig;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.dagger.scopes.AppScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class RestModule {

    private final String mEndpoint;

    private final SetCookieCache mSessionCookie;

    private final SharedPrefsCookiePersistor mSharedPrefsCookiePersistor;

    public RestModule(String endpoint , Context applicationContext) {
        mEndpoint = endpoint;
        mSessionCookie = new SetCookieCache();
        mSharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(applicationContext);

    }

    @Provides
    @AppScope
    public OkHttpClient provideOkHttpClient(CookieJar cookieJar) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(interceptor);

        }
        return builder.cookieJar(cookieJar).build();
    }

    @Provides
    @AppScope
    public Retrofit provideRetrofit(final OkHttpClient client) {
        final Retrofit.Builder builder = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mEndpoint);

        return builder.build();
    }

    @Provides
    @AppScope
    public CookieJar providePersistentCookie(Context context){
        return new PersistentCookieJar(mSessionCookie, mSharedPrefsCookiePersistor);
    }

    @Provides
    @AppScope
    public SetCookieCache provideCookieCache(){
        return mSessionCookie;
    }

  /*  @Provides
    @AppScope
    public CookieJar providePersistentCookie(Context context){
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }
*/
    @Provides
    @AppScope
    public PetMedsApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(PetMedsApiService.class);
    }
}
