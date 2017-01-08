package com.petmeds1800.ui.account;

import android.support.annotation.NonNull;

import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SessionConfigRequest;
import com.petmeds1800.model.entities.SignOutResponse;

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

    @Inject
    GeneralPreferencesHelper mGeneralPreferencesHelper;

    public SignOutPresenter(@NonNull SignOutContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void sendDataToServer(final String sessionConfigParam) {
        mPetMedsApiService.logout(new SessionConfigRequest(sessionConfigParam))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignOutResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("SignouPresenter", e.getMessage());
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("Signout", "retrying after session renew");

                            sendDataToServer(mGeneralPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

                            return;

                        }

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
