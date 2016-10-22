package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 05-10-2016.
 */

public class GuestStepOneRootPresentor implements GuestStepOneRootContract.Presenter {
    private GuestStepOneRootContract.View mView;
    @Inject
    PetMedsApiService mPetMedsApiService;
    public GuestStepOneRootPresentor(@NonNull GuestStepOneRootContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }


    @Override
    public void start() {

    }
    @Override
    public void saveGuestShippingAddressData(ShippingAddressRequest shippingAddressRequest) {
        mPetMedsApiService.saveGuestShippingAddressData(shippingAddressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("AddACard", e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if(errorId != 0){
                            if(mView.isActive()){
                                mView.showErrorCrouton(((Fragment)mView).getString(errorId), false);
                            }

                        }
                        else {
                            if(mView.isActive()){
                                mView.showErrorCrouton(e.getLocalizedMessage(), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse ) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.navigateOnSuccess(shoppingCartListResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(shoppingCartListResponse.getStatus().getErrorMessages().get(0),false);
                            }
                        }

                    }
                });
    }

}
