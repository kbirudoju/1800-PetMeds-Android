package com.petmeds1800.ui.vet;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.VetListResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/12/2016.
 */
public class  VetListPresenter implements VetListContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private VetListContract.View mView;


    public VetListPresenter(@NonNull VetListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

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
                               mView.onSuccess(vetListResponse.getVetList());
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
