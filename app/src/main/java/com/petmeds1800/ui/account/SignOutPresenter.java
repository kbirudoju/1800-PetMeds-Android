package com.petmeds1800.ui.account;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SignOutRequest;
import com.petmeds1800.model.entities.SignOutResponse;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 12-09-2016.
 */

public class SignOutPresenter implements SignOutContract.Presenter {

    private SignOutContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    public SignOutPresenter(@NonNull SignOutContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void sendDataToServer(String sessionConfigParam) {
        mPetMedsApiService.logout(new SignOutRequest(sessionConfigParam))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignOutResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(SignOutResponse s) {
                        Log.d("SignOutResponse", s.toString());
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccess();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(
                                        s.getStatus().getErrorMessages().size() > 0 ? s.getStatus().getErrorMessages()
                                                .get(0) : null);
                            }
                        }

                    }
                });
    }


    @Override
    public void start() {

    }
}
