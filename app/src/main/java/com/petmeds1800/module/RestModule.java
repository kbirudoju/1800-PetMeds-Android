package com.petmeds1800.module;

import com.petmeds1800.BuildConfig;
import com.petmeds1800.R;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module(includes = {ApiModule.class})
public class RestModule {

    private final String mEndpoint;

    public RestModule(final Application app) {
        mEndpoint = app.getString(R.string.server_endpoint);
    }

    @Provides @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(interceptor);
        }

        return builder.build();
    }

    @Provides @Singleton
    public Retrofit provideRetrofit(final OkHttpClient client) {
        final Retrofit.Builder builder = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(mEndpoint);

        return builder.build();
    }
}
