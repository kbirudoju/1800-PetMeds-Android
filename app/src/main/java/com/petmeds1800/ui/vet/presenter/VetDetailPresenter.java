package com.petmeds1800.ui.vet.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.model.entities.RemovePetResponse;
import com.petmeds1800.ui.vet.VetDetailContract;
import com.petmeds1800.ui.vet.VetDetailContract.Presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/24/2016.
 */
public class VetDetailPresenter implements Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private VetDetailContract.View mView;

    public VetDetailPresenter(@NonNull VetDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }
    @Override
    public void requestReferral(UpdateVetRequest request) {
        mPetMedsApiService.requestRefrral(request)
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
                            mView.onSuccess();
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
