package com.petmeds1800.ui.checkout;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.entities.ShippingMethodsRequest;
import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Digvijay on 9/27/2016.
 */
public class StepTwoPresenter implements StepTwoContract.Presenter {

    private final StepTwoContract.View mView;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    ShippingMethodsRequest mShippingMethodsRequest;
    public StepTwoPresenter(StepTwoContract.View view) {
        mView = view;
        PetMedsApplication.getAppComponent().inject(this);
    }

    public void initializeShippingMethod(ShippingMethodsRequest shippingMethodsRequest){
        this.mShippingMethodsRequest=shippingMethodsRequest;
    }
    @Override
    public void populateShippingMethodsListRecycler() {
        mView.showProgress();
        mApiService.getShippingMethods()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShippingMethodsResponse>() {
                    @Override
                    public void onCompleted() {
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);

                        if (mView.isActive()) {
                            if (errorId != 0) {
                                mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                            } else {
                                mView.showErrorCrouton("Unexpected error", false);
                            }
                            mView.hideProgress();
                        }


                    }

                    @Override
                    public void onNext(ShippingMethodsResponse shippingMethodsResponse) {
                        if (shippingMethodsResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccessShippingMethods(shippingMethodsResponse);
                                mView.hideProgress();
                            }

                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(shippingMethodsResponse.getStatus().getErrorMessages().get(0), false);
                                mView.hideProgress();
                            }
                        }
                    }
                });
    }

    @Override
    public void showShippingOptions() {
        mView.showProgress();
        mApiService.getShippingOptions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                    }

                    @Override
                    public void onNext(String htmlResponse) {
                        mView.hideProgress();
                        mView.onSuccessShippingOptions(htmlResponse);
                    }
                });
    }

    @Override
    public void applyShippingMethods() {
        mApiService.applyShippingMethods(mShippingMethodsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("StepTwoPresenter", "retrying after session renew");
                            mShippingMethodsRequest.setDynSessConf(
                                    mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                            applyShippingMethods();

                            return;

                        }

                        //check for other errors
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (errorId != 0) {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                                mView.hideActivityProgress();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(e.getLocalizedMessage(), false);
                                mView.hideActivityProgress();
                            }
                        }
                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccessShippingMethodsApplied(shoppingCartListResponse);
                                mView.hideActivityProgress();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onErrorShippingMethodsApplied(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                                mView.hideActivityProgress();
                            }
                        }
                    }
                });
    }

    @Override
    public void start() {

    }
}
