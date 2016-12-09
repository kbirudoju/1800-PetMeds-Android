package com.petmeds1800.api;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Abhinav on 6/12/16.
 */
public class RxCallAdapterFactoryWithErrorHandling extends CallAdapter.Factory {

    @Inject
    SetCookieCache mCookieCache;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private final RxJavaCallAdapterFactory original;

    public RxCallAdapterFactoryWithErrorHandling() {
        original = RxJavaCallAdapterFactory.create();
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }


    private class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {

        private final Retrofit retrofit;
        private PetMedsApiService mApiService;
        private final CallAdapter<?> wrapped;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
            mApiService = retrofit.create(PetMedsApiService.class);
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Func1<Throwable, Observable>() {
                @Override
                public Observable call(final Throwable throwable) {

                    if (throwable.getMessage().contains("Conflict")) {
                        Utils.getAndUpdateNewCookie(throwable, mCookieCache);

                        return mApiService.getSessionConfirmationNumber()
                                .flatMap(new Func1<SessionConfNumberResponse, Observable<?>>() {
                                    @Override
                                    public Observable<?> call(SessionConfNumberResponse sessionConfNumberResponse) {

                                        //save the new confirmation number
                                        if (sessionConfNumberResponse != null) {
                                            mPreferencesHelper
                                                    .saveSessionConfirmationResponse(sessionConfNumberResponse);
                                        }

                                        //sending a security exception to open up a fingerprint auth dialog
                                        return Observable.error(new SecurityException(throwable)); //TODO we should ideally create one custom exception
                                    }
                                });

                    }

                    // re-throw this error because it's not recoverable from here
                    return Observable.error(throwable);
                }


            });
        }
    }

}