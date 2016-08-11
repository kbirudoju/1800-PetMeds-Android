package com.petmeds1800.dagger.module;

import com.petmeds1800.api.PetMedsApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    public ApiModule() {
    }

    @Provides
    @Singleton
    public PetMedsApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(PetMedsApiService.class);
    }
}
