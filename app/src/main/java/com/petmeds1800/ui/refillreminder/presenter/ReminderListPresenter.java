package com.petmeds1800.ui.refillreminder.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.refillreminder.response.EasyRefillReminder;
import com.petmeds1800.model.refillreminder.response.OrderItems;
import com.petmeds1800.model.refillreminder.response.RefillReminderListResponse;
import com.petmeds1800.ui.refillreminder.ReminderListContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public class ReminderListPresenter implements ReminderListContract.Presenter {

    private final ReminderListContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public ReminderListPresenter(ReminderListContract.View mView) {
        this.mView = mView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getGeneralPopulateRefillReminderList() {
        mPetMedsApiService.getRefillReminderList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RefillReminderListResponse>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(RefillReminderListResponse refillReminderListResponse) {
                if (refillReminderListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {

                    if (mView.isActive()) {
                        mView.postGeneralPopulateRefillReminderList(refillReminderListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(refillReminderListResponse.getStatus().getErrorMessages().get(0));
                    }
                }
            }
        });
    }

    @Override
    public void start() {

    }
}
