package com.petmeds1800.ui.shoppingcart.presenter;

import com.petmeds1800.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.shoppingcart.request.AddItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.ApplyCouponRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.RemoveItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.UpdateItemQuantityRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.shoppingcart.ShoppingCartListContract;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;
import javax.inject.Named;

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
     @Named(Constants.TAG_REDIRECT_OFF)
    PetMedsApiService mPetMedsApiServiceRedrirectOff;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public ShoppingCartListPresenter(ShoppingCartListContract.View mView) {
        this.mView = mView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getGeneralPopulateShoppingCart() {
        Log.w("ShoppingCartListPresntr", "getGeneralPopulateShoppingCart Enter");

        mPetMedsApiService.getGeneralPopulateShoppingCart(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                //check if we need to retry as a consequence of 409 conflict
                if (e instanceof SecurityException) {
                    Log.d("getAddItemShoppingCart", "retrying after session renew");

                    getGeneralPopulateShoppingCart();

                    return;

                } else if (RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection || RetrofitErrorHandler.getErrorMessage(e) == R.string.connectionTimeout) {
                    if (mView.isActive()) {
                        mView.showRetryView();
                        mView.onError(e.getMessage(),null);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(e.getMessage(),null);
                    }
                }
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),null);
                    }
                }
            }
        });
        Log.w("ShoppingCartListPresntr", "getGeneralPopulateShoppingCart Exit");
    }

    @Override
    public void getAddItemShoppingCart(final AddItemRequestShoppingCart addItemRequestShoppingCart) {
        Log.w("ShoppingCartListPresntr", "getAddItemShoppingCart Enter");

        mPetMedsApiService.getAddItemShoppingCart(addItemRequestShoppingCart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {

                //check if we need to retry as a consequence of 409 conflict
                if (e instanceof SecurityException) {
                    Log.d("getAddItemShoppingCart", "retrying after session renew");

                    getAddItemShoppingCart(addItemRequestShoppingCart);

                    return;

                }
                else {
                    if (mView.isActive()) {
                        mView.onError(e.getMessage(),AddItemRequestShoppingCart.class.getSimpleName());
                    }
                }
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),AddItemRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
        Log.w("ShoppingCartListPresntr", "getAddItemShoppingCart Exit");
    }

    @Override
    public void getRemoveItemShoppingCart(final RemoveItemRequestShoppingCart removeItemRequestShoppingCart) {
        Log.w("ShoppingCartListPresntr", "getRemoveItemShoppingCart Enter");

        removeItemRequestShoppingCart.set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getRemoveItemShoppingCart(removeItemRequestShoppingCart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                //check if we need to retry as a consequence of 409 conflict
                if (e instanceof SecurityException) {
                    Log.d("getRemoveItemShoppingCart", "retrying after session renew");

                    getRemoveItemShoppingCart(removeItemRequestShoppingCart);

                    return;

                } else {
                    if (mView.isActive()) {
                        mView.onError(e.getMessage(),RemoveItemRequestShoppingCart.class.getSimpleName());
                    }
                }
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),RemoveItemRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
        Log.w("ShoppingCartListPresntr", "getRemoveItemShoppingCart Exit");
    }

    @Override
    public void getApplyCouponShoppingCart(final ApplyCouponRequestShoppingCart applyCouponRequestShoppingCart) {
        Log.w("ShoppingCartListPresntr", "getApplyCouponShoppingCart Enter");

        applyCouponRequestShoppingCart.set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getApplyCouponShoppingCart(applyCouponRequestShoppingCart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                //check if we need to retry as a consequence of 409 conflict
                if (e instanceof SecurityException) {
                    Log.d("getApplyCouponShoppingCart", "retrying after session renew");

                    getApplyCouponShoppingCart(applyCouponRequestShoppingCart);

                    return;

                } else {
                    if (mView.isActive()) {
                        mView.onError(e.getMessage(),ApplyCouponRequestShoppingCart.class.getSimpleName());
                    }
                }
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),ApplyCouponRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });
        Log.w("ShoppingCartListPresntr", "getApplyCouponShoppingCart Exit");
    }

    @Override
    public void getUpdateItemQuantityRequestShoppingCart(final UpdateItemQuantityRequestShoppingCart updateItemQuantityRequestShoppingCart) {
        Log.w("ShoppingCartListPresntr", "getUpdateItemQuantityRequestShoppingCart Enter");

        updateItemQuantityRequestShoppingCart.set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPetMedsApiService.getUpdateItemQuantityRequestShoppingCart(updateItemQuantityRequestShoppingCart.getmCommerceIDQuantityMap()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                //check if we need to retry as a consequence of 409 conflict
                if (e instanceof SecurityException) {
                    Log.d("getUpdateItemQuantityRequestShoppingCart", "retrying after session renew");

                    getUpdateItemQuantityRequestShoppingCart(updateItemQuantityRequestShoppingCart);

                    return;

                } else {
                    if (mView.isActive()) {
                        mView.onError(e.getMessage(),UpdateItemQuantityRequestShoppingCart.class.getSimpleName());
                    }
                }
            }

            @Override
            public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    if (mView.isActive()) {
                        mView.postGeneralPopulateShoppingCart(shoppingCartListResponse);
                    }
                } else {
                    if (mView.isActive()) {
                        mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0),UpdateItemQuantityRequestShoppingCart.class.getSimpleName());
                    }
                }
            }
        });

        Log.w("ShoppingCartListPresntr", "getUpdateItemQuantityRequestShoppingCart Exit");
    }

    @Override
    public void checkoutPayPal(PayPalCheckoutRequest request) {
        mPetMedsApiServiceRedrirectOff.payPalCheckout(request)
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