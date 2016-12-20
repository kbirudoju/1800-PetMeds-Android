package com.petmeds1800.ui.pushnotifications;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.PushNotificationRequest;
import com.petmeds1800.model.entities.PushNotificationResponse;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 15-12-2016.
 */

public class PushNotificationPresenter implements PushNotificationContract.Presenter {

    private PushNotificationContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    public PushNotificationPresenter(@NonNull PushNotificationContract.View mView) {
        this.mView = mView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void savePushNotificationFlag(PushNotificationRequest pushNotificationRequest) {
        mPetMedsApiService.savePushNotificationFlag(pushNotificationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PushNotificationResponse>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   //error handling would be implemented once we get the details from backend team
                                   int errorId = RetrofitErrorHandler.getErrorMessage(e);
                                   if (mView.isActive()) {
                                       if (errorId != 0) {
                                           mView.onError(((Activity) mView).getString(errorId));
                                       } else {
                                           mView.onError("Unexpected error");
                                       }

                                   }
                               }

                               @Override
                               public void onNext(PushNotificationResponse pushNotificationResponse) {
                                   Log.d("response", pushNotificationResponse.toString());
                                   if (pushNotificationResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                       if (mView.isActive()) {
                                           mView.onNotificationFlagSuccess();
                                       }
                                   } else {
                                       if (mView.isActive()) {
                                           mView.onNotificationFlagError(
                                                   pushNotificationResponse.getStatus().getErrorMessages().size() > 0
                                                           ? pushNotificationResponse.getStatus().getErrorMessages()
                                                           .get(0) : null);
                                       }
                                   }

                               }
                           }

                );
    }

    @Override
    public void start() {

    }
}
