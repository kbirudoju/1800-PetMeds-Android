package com.petmeds1800.ui.vet.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.AddVetResponse;
import com.petmeds1800.model.RemoveVetRequest;
import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.model.entities.RemovePetResponse;
import com.petmeds1800.ui.vet.EditVetContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/19/2016.
 */
public class EditVetPresenter implements EditVetContract.Presenter{
    @Inject
    PetMedsApiService mPetMedsApiService;

    private EditVetContract.View mView;

    public EditVetPresenter(@NonNull EditVetContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void updateVet(UpdateVetRequest request) {
        mPetMedsApiService.updateVet(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddVetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(AddVetResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.onSuccess(s.getVet());
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void removeVet(RemoveVetRequest request) {
        mPetMedsApiService.removeVet(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemovePetResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(RemovePetResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.onPetRemoveSuccess();
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}
