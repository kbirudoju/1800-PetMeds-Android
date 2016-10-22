package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 20-10-2016.
 */

public class MedicationReminderItemsListPresentor implements MedicationReminderItemListContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private final MedicationReminderItemListContract.View mView;

    public MedicationReminderItemsListPresentor(MedicationReminderItemListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getOrderList() {

        mPetMedsApiService
                .getOrderList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyOrder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(MyOrder myOrder) {
                        if (myOrder.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccess(myOrder.getOrderList());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(myOrder.getStatus().getErrorMessages().get(0), false);
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {
        getOrderList();
    }
}
