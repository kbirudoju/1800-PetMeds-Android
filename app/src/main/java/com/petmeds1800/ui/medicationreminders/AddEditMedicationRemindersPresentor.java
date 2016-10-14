package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.AddMedicationReminderRequest;
import com.petmeds1800.model.entities.AddMedicationReminderResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 14-10-2016.
 */

public class AddEditMedicationRemindersPresentor implements AddEditMedicationRemindersContract.Presenter{
    @Inject
    PetMedsApiService mPetMedsApiService;
    private final AddEditMedicationRemindersContract.View mView;
    public AddEditMedicationRemindersPresentor(AddEditMedicationRemindersContract.View view){
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void saveReminders(AddMedicationReminderRequest addMedicationReminderRequest) {
        mPetMedsApiService.saveMedicationReminders(addMedicationReminderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddMedicationReminderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(AddMedicationReminderResponse addMedicationReminderResponse ) {
                        if (addMedicationReminderResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccess(addMedicationReminderResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(addMedicationReminderResponse.getStatus().getErrorMessages().get(0),false);
                            }
                        }

                    }
                });
    }
}
