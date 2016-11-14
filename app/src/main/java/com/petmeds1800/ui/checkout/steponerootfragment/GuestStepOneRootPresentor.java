package com.petmeds1800.ui.checkout.steponerootfragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShippingAddress;
import com.petmeds1800.model.shoppingcart.response.ShippingGroups;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.RetrofitErrorHandler;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 05-10-2016.
 */

public class GuestStepOneRootPresentor implements GuestStepOneRootContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private GuestStepOneRootContract.View mView;

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
        mView.hideWarningView();
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
                                   if (errorId != 0) {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                                       }

                                   } else {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(e.getLocalizedMessage(), false);
                                       }
                                   }
                               }

                               @Override
                               public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                                   if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                       if (mView.isActive()) {
                                           mView.navigateOnSuccess(shoppingCartListResponse);
                                       }
                                   } else if (shoppingCartListResponse.getStatus().getCode().equals(API_WARNING_CODE)) {
                                       mView.showWarningView(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                                   } else {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(shoppingCartListResponse.getStatus().getErrorMessages().get(0), true);
                                       }
                                   }

                               }
                           }

                );
    }

    @Override
    public void extractAddress(final ArrayList<ShippingGroups> shippingGroupses) {

        //we need to perform this unnecessary conversion into Address model as reusable 'AddEditFragment' only knows about Address model
        Observable.create(new Observable.OnSubscribe<Address>() {
            @Override
            public void call(Subscriber<? super Address> subscriber) {

                if (shippingGroupses == null || shippingGroupses.size() == 0) {
                    subscriber.onNext(null);
                }

                ShippingAddress shippingAddress = null;
                Address address = new Address();
                if (shippingGroupses.size() > 0) {
                    shippingAddress = shippingGroupses.get(0).getShippingAddress();
                    address.setAddress1(shippingAddress.getAddress1());
                    address.setAddress2(shippingAddress.getAddress2());
                    address.setCity(shippingAddress.getCity());
                    address.setState(shippingAddress.getState());
                    address.setCountry(shippingAddress.getCountry());
                    address.setFirstName(shippingAddress.getFirstName());
                    address.setLastName(shippingAddress.getLastName());
                    address.setPhoneNumber(shippingAddress.getPhoneNumber());
                    address.setPostalCode(shippingAddress.getPostalCode());

                    subscriber.onNext(address);
                }
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Address>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("GuestStepThressPres", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Address address) {
                        if (mView.isActive()) {
                            mView.setAddress(address);
                        }
                    }
                });

    }

}
