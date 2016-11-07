package com.petmeds1800.ui.shoppingcart.presenter;

import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.shoppingcart.request.AddItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.ApplyCouponRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.RemoveItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.UpdateItemQuantityRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.shoppingcart.ShoppingCartListContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sarthak on 9/23/2016.
 */

public class ShoppingCartListPresenter implements ShoppingCartListContract.Presenter {

    private final ShoppingCartListContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public ShoppingCartListPresenter(ShoppingCartListContract.View mView) {
        this.mView = mView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getGeneralPopulateShoppingCart() {
        mPetMedsApiService.getGeneralPopulateShoppingCart(
                mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),
                                        ApplyCouponRequestShoppingCart.class.getSimpleName());
                            }
                        }
                    }
                });
    }

    @Override
    public void getAddItemShoppingCart(AddItemRequestShoppingCart addItemRequestShoppingCart) {
        mPetMedsApiService.getAddItemShoppingCart(addItemRequestShoppingCart).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),
                                AddItemRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
    }

    @Override
    public void getRemoveItemShoppingCart(RemoveItemRequestShoppingCart removeItemRequestShoppingCart) {
        removeItemRequestShoppingCart
                .set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getRemoveItemShoppingCart(removeItemRequestShoppingCart).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),
                                RemoveItemRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
    }

    @Override
    public void getApplyCouponShoppingCart(ApplyCouponRequestShoppingCart applyCouponRequestShoppingCart) {
        applyCouponRequestShoppingCart
                .set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getApplyCouponShoppingCart(applyCouponRequestShoppingCart).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),
                                ApplyCouponRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
    }

    @Override
    public void getUpdateItemQuantityRequestShoppingCart(
            UpdateItemQuantityRequestShoppingCart updateItemQuantityRequestShoppingCart) {
        updateItemQuantityRequestShoppingCart
                .set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getUpdateItemQuantityRequestShoppingCart(
                updateItemQuantityRequestShoppingCart.getmCommerceIDQuantityMap()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),
                                UpdateItemQuantityRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
    }

    @Override
    public void checkoutPayPal(PayPalCheckoutRequest request) {
        mPetMedsApiService.payPalCheckout(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error",e.getLocalizedMessage());
                        mView.onPayPalError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(Response<String> s) {
                        Log.d("response", s + ">>>");
                        String loactionHeader=s.headers().get("Location");
                        if(loactionHeader==null || loactionHeader.isEmpty()){
                            mView.onPayPalError("");
                        }else{
                            mView.onSuccess(loactionHeader);

                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}
