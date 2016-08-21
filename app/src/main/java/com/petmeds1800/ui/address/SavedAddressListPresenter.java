package com.petmeds1800.ui.address;

import android.support.annotation.NonNull;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MySavedAddress;
import com.petmeds1800.model.entities.MySavedCard;
import com.petmeds1800.ui.fragments.LoginFragment;
import com.petmeds1800.ui.payment.SavedCardsListContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 11/8/16.
 */
public class SavedAddressListPresenter implements SavedAddressListContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private SavedAddressListContract.View mView;

    public SavedAddressListPresenter(@NonNull SavedAddressListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getSavedAddress() {

        mPetMedsApiService.getSavedAddress(LoginFragment.sessionConfirmationNUmber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MySavedAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //notify about the error.It could be any type of error while getting data from the API
                        Log.e(SavedAddressListPresenter.class.getName(),e.getMessage());
                    }

                    @Override
                    public void onNext(MySavedAddress s) {
                        if(s.getStatus().getCode().equals(API_SUCCESS_CODE)
                                && s.getProfileAddresses() != null && s.getProfileAddresses().size() > 0){

                            if (mView.isActive()) {
                                mView.showAddressListView(s.getProfileAddresses());
                            }
                        }
                        else {
                            if (mView.isActive()) {
                                mView.showNoAddressView();
                            }
                        }
                    }
                });

    }

    @Override
    public void start() {
        getSavedAddress();
    }
}
