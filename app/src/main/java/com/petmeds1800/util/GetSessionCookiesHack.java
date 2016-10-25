package com.petmeds1800.util;

import android.util.Log;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Digvijay on 10/25/2016.
 */

public abstract class GetSessionCookiesHack {

    public void doHackForGettingSessionCookies(final boolean doLogin, PetMedsApiService apiService) {
        getSessionCookiesShowProgress();
        //TODO: remove this temporary hack after backend resolves their problem of cookies
        apiService.login(new LoginRequest("", "", "test"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getSessionCookiesOnFinish(doLogin, e);
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        getSessionCookiesHideProgress();
                        Log.v("temporary hack response", loginResponse.getStatus().getCode());
                    }
                });
    }

    public abstract void getSessionCookiesOnFinish(boolean doLogin, Throwable e);

    public abstract void getSessionCookiesShowProgress();

    public abstract void getSessionCookiesHideProgress();
}
