package com.petmeds1800.ui.vet;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.SearchVetByZipCodeResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/4/2016.
 */
public class AddVetPresenter implements AddVetContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private AddVetContract.View mView;
    private SearchVetByZipCodeResponse searchVetByZipCodeResponse;

    public AddVetPresenter(@NonNull AddVetContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public SearchVetByZipCodeResponse getVetList(String zipCode) {
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
                            searchVetByZipCodeResponse=s;
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
        return searchVetByZipCodeResponse;
    }

    @Override
    public void start() {

    }
}
