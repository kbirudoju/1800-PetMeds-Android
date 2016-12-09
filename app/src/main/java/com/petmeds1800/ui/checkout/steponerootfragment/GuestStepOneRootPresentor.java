package com.petmeds1800.ui.checkout.steponerootfragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShippingAddress;
import com.petmeds1800.model.shoppingcart.response.ShippingGroups;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.GetSessionCookiesHack;
import com.petmeds1800.util.RetrofitErrorHandler;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 05-10-2016.
 */

public class GuestStepOneRootPresentor implements GuestStepOneRootContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private GuestStepOneRootContract.View mView;

    private GuestStepOneRootFragment mContext;

    public GuestStepOneRootPresentor(@NonNull GuestStepOneRootContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mContext = (GuestStepOneRootFragment) view;
        PetMedsApplication.getAppComponent().inject(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void saveGuestShippingAddressData(final ShippingAddressRequest shippingAddressRequest) {
        mView.hideWarningView();
        new GetSessionCookiesHack() {
            @Override
            public void getSessionCookiesOnFinish(boolean doLogin, Throwable e) {
                int errorId = RetrofitErrorHandler.getErrorMessage(e);
                if (errorId == R.string.noInternetConnection) {
                    mView.showErrorCrouton(mContext.getString(errorId), false);
                    mView.hideProgress();
                } else {
                    doSaveAddress(shippingAddressRequest);
                }
            }

            @Override
            public void getSessionCookiesShowProgress() {
                mView.showProgress();
            }

            @Override
            public void getSessionCookiesHideProgress() {
                mView.hideProgress();
            }
        }.doHackForGettingSessionCookies(false, mPetMedsApiService);

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

    private void doSaveAddress(final ShippingAddressRequest shippingAddressRequest) {
        mPetMedsApiService.getSessionConfirmationNumber()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, SessionConfNumberResponse>() {
                    @Override
                    public SessionConfNumberResponse call(Throwable throwable) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(throwable);
                        if (errorId == R.string.noInternetConnection) {
                            mView.hideProgress();
                            mView.showErrorCrouton(mContext.getString(errorId), false);
                        } else {
                            return mPreferencesHelper.getSessionConfirmationResponse();
                        }
                        return null;
                    }
                }).flatMap(new Func1<SessionConfNumberResponse, Observable<ShoppingCartListResponse>>() {
            @Override
            public Observable<ShoppingCartListResponse> call(SessionConfNumberResponse sessionConfNumberResponse) {
                if (sessionConfNumberResponse != null) {
                    String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                    Log.v("sessionToken", sessionConfNumber);
                    if (sessionConfNumber != null) {
                        mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                    }
                    shippingAddressRequest.setDynSessConf(sessionConfNumber);
                    return mPetMedsApiService.saveGuestShippingAddressData(shippingAddressRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                } else {
                    return null;
                }
            }
        })
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
                                           mView.hideProgress();
                                       }

                                   } else {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(e.getLocalizedMessage(), false);
                                           mView.hideProgress();
                                       }
                                   }
                               }

                               @Override
                               public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                                   if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                       if(shippingAddressRequest.getPassword()!=null){
                                           mPreferencesHelper.setIsUserLoggedIn(true);
                                           mPreferencesHelper.setLoginEmail(shippingAddressRequest.getEmail());
                                           mPreferencesHelper.setLoginPassword(shippingAddressRequest.getPassword());
                                       }
                                       if (mView.isActive()) {
                                           mView.navigateOnSuccess(shoppingCartListResponse);
                                       }
                                   } else if (shoppingCartListResponse.getStatus().getCode().equals(API_WARNING_CODE)) {
                                       if(mView.isActive()) {
                                           mView.showWarningView(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                                           mView.hideProgress();
                                       }

                                   } else {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(shoppingCartListResponse.getStatus().getErrorMessages().get(0), false);
                                           mView.hideProgress();
                                       }
                                   }

                               }
                           }

                );
    }
}
