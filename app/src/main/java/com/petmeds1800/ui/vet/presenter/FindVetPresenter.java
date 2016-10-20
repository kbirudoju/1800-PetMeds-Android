package com.petmeds1800.ui.vet.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.SearchVetByZipCodeResponse;
import com.petmeds1800.ui.vet.FindVetContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/18/2016.
 */
public class FindVetPresenter implements FindVetContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private FindVetContract.View mView;

    public FindVetPresenter(@NonNull FindVetContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void getVetList(String zipCode) {
        mPetMedsApiService.getVetByZipCode(zipCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchVetByZipCodeResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(SearchVetByZipCodeResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                           mView.onSuccess(s.getVetList());
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

