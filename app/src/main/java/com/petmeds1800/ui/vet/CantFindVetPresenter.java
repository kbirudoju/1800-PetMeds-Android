package com.petmeds1800.ui.vet;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.AddVetRequest;
import com.petmeds1800.model.AddVetResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/3/2016.
 */
public class CantFindVetPresenter implements CantFindVetContract.Presenter{

    @Inject
    PetMedsApiService mPetMedsApiService;

    private CantFindVetContract.View mView;

    public CantFindVetPresenter(@NonNull CantFindVetContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void addVetData(AddVetRequest addVetRequest) {
        mPetMedsApiService.addVet(addVetRequest)
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
                            if (mView.isActive()) {
                                mView.onSuccess(s.getVet());
                            }
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
