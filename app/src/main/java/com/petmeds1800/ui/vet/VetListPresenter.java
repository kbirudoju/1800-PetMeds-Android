package com.petmeds1800.ui.vet;

import android.support.annotation.NonNull;
import com.petmeds1800.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.MySavedAddress;
import com.petmeds1800.model.entities.VetListResponse;
import com.petmeds1800.ui.address.SavedAddressListPresenter;
import com.petmeds1800.util.GeneralPreferencesHelper;

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

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

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
    public void getZipCode() {

        mPetMedsApiService
                .getSavedAddress(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MySavedAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //notify about the error.It could be any type of error while getting data from the API
                        Log.e(SavedAddressListPresenter.class.getName(), e.getMessage());
                        if (mView.isActive()) {
                           // mView.showErrorMessage(e.getMessage());

                        }
                    }

                    @Override
                    public void onNext(MySavedAddress s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)
                                && s.getProfileAddresses() != null && s.getProfileAddresses().size() > 0) {

                            if (mView.isActive()) {
                                //mView.showAddressListView(s.getProfileAddresses());
                                for(Address address : s.getProfileAddresses()){
                                    if(address.getIsDefaultShippingAddress()){
                                        mView.onZipCodeSuccess(address.getPostalCode());
                                        return;
                                    }
                                }


                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onZipCodeError();
                            }
                        }
                    }
                });

    }

    @Override
    public void start() {

    }
}
