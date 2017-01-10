package com.petmeds1800.ui.orders.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.ReOrderRequest;
import com.petmeds1800.model.ReOrderResponse;
import com.petmeds1800.model.entities.OrderDetailResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.orders.OrderDetailContract;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/5/2016.
 */
public class OrderDetailPresenter implements OrderDetailContract.Presenter {

    private OrderDetailContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public OrderDetailPresenter(@NonNull OrderDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void reOrder(ReOrderRequest reOrderRequest) {
        mPetMedsApiService.reOrder(reOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReOrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ReOrderResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.onSuccess();
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }


    @Override
    public void cancelOrder(ReOrderRequest reOrderRequest) {
        mPetMedsApiService.cancelOrder(reOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReOrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ReOrderResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.onCancelSuccess();

                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }


    @Override
    public void addToCart(AddToCartRequest addToCartRequest) {
        mPetMedsApiService.addToCart(addToCartRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ShoppingCartListResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.addToCartSuccess();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });


    }

    @Override
    public void getOrderDetail(String sessionConfig, final String orderId) {
        mPetMedsApiService.getOrderDetail(sessionConfig, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderDetailResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("getOrderDetail", "retrying after session renew");
                            getOrderDetail(
                                    mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                                    orderId);
                            return;

                        }
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(OrderDetailResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onOrderDetailSuccess(s.getOrderDetails());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onOrderDetailError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}
