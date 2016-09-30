package com.petmeds1800.ui.checkout.stepfour.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.PetList;
import com.petmeds1800.model.entities.VetListResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 9/28/2016.
 */
public class PetVetInfoPresenter implements PetVetInfoContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;
    private PetVetInfoContract.View mView;


    public PetVetInfoPresenter(@NonNull PetVetInfoContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void getPetListData() {
        mPetMedsApiService
                .getPetList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetList list) {
                        if (list.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.setPetList(list.getPetList());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(list.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }
    @Override
    public void getVetListData() {
        mPetMedsApiService
                .getVetList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VetListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(VetListResponse vetListResponse) {
                        if (vetListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.setVetList(vetListResponse.getVetList());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(vetListResponse.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }
    @Override
    public void start() {

    }
}
