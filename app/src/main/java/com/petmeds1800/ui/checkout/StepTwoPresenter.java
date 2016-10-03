package com.petmeds1800.ui.checkout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.ShippingMethodsRequest;
import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;

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

    public StepTwoPresenter(StepTwoContract.View view) {
        mView = view;
        PetMedsApplication.getAppComponent().inject(this);
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
                        mView.hideProgress();
                    }

                    @Override
                    public void onNext(ShippingMethodsResponse shippingMethodsResponse) {
                        mView.hideProgress();
                        mView.onSuccessShippingMethods(shippingMethodsResponse);
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
    public void applyShippingMethods(ShippingMethodsRequest shippingMethodsRequest) {
        mApiService.applyShippingMethods(shippingMethodsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccessShippingMethodsApplied(shoppingCartListResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onErrorShippingMethodsApplied(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                            }
                        }
                    }
                });
    }


    @Override
    public void start() {

    }
}
