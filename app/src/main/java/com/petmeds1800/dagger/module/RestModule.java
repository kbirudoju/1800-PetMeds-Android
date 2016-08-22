package com.petmeds1800.dagger.module;

import com.petmeds1800.BuildConfig;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.dagger.AppScope;

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

    public RestModule(String endpoint) {
        mEndpoint = endpoint;
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
    public PetMedsApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(PetMedsApiService.class);
    }
}
