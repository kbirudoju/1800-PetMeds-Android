package com.dminc.application;

import com.dminc.application.module.RestModule;

import okhttp3.OkHttpClient;

/**
 * @author Konrad
 */
public class MockRestModule extends RestModule {
    public MockRestModule(TemplateApplication templateApplication) {
        super(templateApplication);
    }

    @Override
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new MockCallInterceptor()).build();
    }
}
