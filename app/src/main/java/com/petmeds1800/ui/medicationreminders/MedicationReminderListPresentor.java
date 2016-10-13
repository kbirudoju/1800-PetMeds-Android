package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MedicationReminderListResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class MedicationReminderListPresentor implements MedicationReminderListContract.Presenter {
    @Inject
    PetMedsApiService mPetMedsApiService;
    private final MedicationReminderListContract.View mView;
    public MedicationReminderListPresentor(MedicationReminderListContract.View view){
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }
    @Override
    public void getMedicationReminderList() {
        mPetMedsApiService.getMedicationReminderList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MedicationReminderListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(MedicationReminderListResponse medicationReminderListResponse ) {
                        if (medicationReminderListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccess(medicationReminderListResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(medicationReminderListResponse.getStatus().getErrorMessages().get(0),false);
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {
        getMedicationReminderList();
    }
}
