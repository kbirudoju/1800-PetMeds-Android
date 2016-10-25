package com.petmeds1800.ui.refillreminder.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.refillreminder.request.RemoveRefillReminderRequest;
import com.petmeds1800.model.refillreminder.request.UpdateRefillReminderRequest;
import com.petmeds1800.model.refillreminder.response.MonthSelectListResponse;
import com.petmeds1800.model.shoppingcart.response.Status;
import com.petmeds1800.ui.refillreminder.EditReminderContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sarthak on 24-Oct-16.
 */

public class EditReminderPresenter implements EditReminderContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private final EditReminderContract.View mView;

    public EditReminderPresenter(EditReminderContract.View mView) {
        this.mView = mView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getGeneralPopulateRefillReminderMonthList() {
        mPetMedsApiService.getRefillReminderMonthList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<MonthSelectListResponse>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(MonthSelectListResponse monthSelectListResponse) {
                if (monthSelectListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {

                    if (mView.isActive()) {
                        mView.postGeneralPopulateRefillReminderMonthList(monthSelectListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(monthSelectListResponse.getStatus().getErrorMessages().get(0));
                    }
                }
            }
        });
    }

    @Override
    public void getUpdateRefillReminder(UpdateRefillReminderRequest updateRefillReminderRequest) {
        updateRefillReminderRequest.set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getUpdateRefillReminder(updateRefillReminderRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Status>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Status status) {
                if (mView.isActive()) {
                    mView.onSuccessRemoveorUpdate(status);
                }
            }
        });
    }

    @Override
    public void getRemoveRefillReminder(RemoveRefillReminderRequest removeRefillReminderRequest) {
        removeRefillReminderRequest.set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getRemoveRefillReminder(removeRefillReminderRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Status>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Status status) {
                if (mView.isActive()) {
                    mView.onSuccessRemoveorUpdate(status);
                }
            }
        });
    }

    @Override
    public void start() {

    }
}
