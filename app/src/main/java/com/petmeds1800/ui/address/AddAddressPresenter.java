package com.petmeds1800.ui.address;

import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.AddACardResponse;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.ui.payment.AddACardContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddAddressPresenter implements AddAddressContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private final AddAddressContract.View mView;

    AddAddressPresenter(AddAddressContract.View view){
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void saveAddress(AddressRequest addressRequest) {
        //show the progress
        mPetMedsApiService.addAddress(addressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("AddACard",e.getMessage());
                        mView.addressAdditionFailed(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(AddAddressResponse s) {
                        if(s.getStatus().getCode().equals(API_SUCCESS_CODE)){
                            if(mView.isActive()){
                                mView.addressAdded();
                            }
                        }
                        else{
                            Log.d("AddACard",s.getStatus().getErrorMessages().get(0));
                            if(mView.isActive()){
                                mView.addressAdditionFailed(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });

    }

    @Override
    public void start() {

    }
}
