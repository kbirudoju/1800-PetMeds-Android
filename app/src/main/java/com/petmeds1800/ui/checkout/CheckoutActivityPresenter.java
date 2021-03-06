package com.petmeds1800.ui.checkout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.InitCheckoutResponse;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import com.petmeds1800.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 25/9/16.
 */
public class CheckoutActivityPresenter implements CheckoutActivityContract.Presenter {

    private final CheckoutActivityContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private HashMap<String, String> mItemDetail;

    public CheckoutActivityPresenter(CheckoutActivityContract.View view) {
        mView = view;
        PetMedsApplication.getAppComponent().inject(this);
    }

    private void renewSessionConfirmationNumber() {

        mPetMedsApiService.getSessionConfirmationNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SessionConfNumberResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView.isActive()) {
                            mView.hideProgress();
                            mView.showErrorCrouton(e.getLocalizedMessage() , false);
                        }
                    }

                    @Override
                    public void onNext(SessionConfNumberResponse sessionConfNumberResponse) {

                        if (!sessionConfNumberResponse.getSessionConfirmationNumber().isEmpty()) {
                            mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            initializeCheckout(mItemDetail);
                        }
                    }
                });

    }

    @Override
    public void initializeCheckout(final HashMap<String, String> itemsDetail) {
        try {
            mView.showProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mItemDetail = itemsDetail;
        //attach the session confirmation number
        itemsDetail.put("_dynSessConf",
                mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

        mPetMedsApiService.initializeCheckout(itemsDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InitCheckoutResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("InitCheckout", e.getMessage());
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("InitCheckout", "retrying after session renew");
                            initializeCheckout(itemsDetail);

                            return;

                        }

                        else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(e.getMessage(), false);
                                mView.hideProgress();
                            }
                        }

                    }

                    @Override
                    public void onNext(InitCheckoutResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            //TODO need to know how to handle a success response
                        } else {
                            Log.d("Init Checkout", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                            }
                        }
                        if (mView.isActive()) {
                            if (s != null) {
                                //hide progress
                                mView.hideProgress();

                                if(s.getShoppingCart() != null) {
                                    mView.updateShoppingCartInShoppingCartListResponse(s.getShoppingCart());
                                }
                                if (s.getCheckoutSteps() != null) {
                                    mView.setCheckoutSteps(s.getCheckoutSteps());
                                    mView.startNextStep(s.getCheckoutSteps().getStepState().getNextCheckoutStep());

                                }
                            }
                        }

                    }
                });


    }

    @Override
    public void checkSecurityStatus() {
        try {
            mView.showProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPetMedsApiService.getSecurityStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecurityStatusResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (mView.isActive()) {
                            //hide the progress
                            mView.hideProgress();
                            //show the retry view
                            if (errorId == R.string.noInternetConnection || errorId == R.string.connectionTimeout) {
//                                mView.showRetryView(mContext.getString(errorId));
                            }

                            mView.showErrorCrouton(e.getMessage(), false);
                        }

                    }

                    @Override
                    public void onNext(SecurityStatusResponse securityStatusResponse) {
                        int securityStatus = securityStatusResponse.getSecurityStatus();
                        Log.i("security status:", securityStatus + "");
                        mView.hideProgress();
                        //pass on security status
                        mView.setSecurityStatus(securityStatus);
                    }
                });

    }


    @Override
    public void mapItemIdWithQuantity(final ShoppingCartListResponse shoppingCartListResponse) {
//        mView.showProgress();
        Observable.create(new Observable.OnSubscribe<HashMap<String, String>>() {
            @Override
            public void call(Subscriber<? super HashMap<String, String>> subscriber) {

                ArrayList<CommerceItems> commerceItems = (shoppingCartListResponse.getShoppingCart() != null)
                        ? shoppingCartListResponse.getShoppingCart().getCommerceItems() : null;
                HashMap<String, String> itemDetails = new HashMap<>();
                if (commerceItems != null) {
                    for (CommerceItems commerceItem : commerceItems) {
                        itemDetails.put(commerceItem.getCommerceItemId(), commerceItem.getQuantity());
                    }
                }

                subscriber.onNext(itemDetails);

            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HashMap<String, String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView.isActive()) {
                            mView.hideProgress();
                            mView.showErrorCrouton(e.getLocalizedMessage(),false);
                        }
                    }

                    @Override
                    public void onNext(HashMap<String, String> itemDetails) {
                        //null checks and empty checks would be done on the fragment itself
                        if(mView.isActive()) {
                            mView.hideProgress();
                            mView.initializeSteps(itemDetails);
                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}
