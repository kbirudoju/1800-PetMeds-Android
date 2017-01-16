package com.petmeds1800.mvp;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 12/8/2016.
 */
public class RefillNotificationPresenter implements RefillNotificationContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private RefillNotificationContract.View mView;

    private Context mContext;

    private AddToCartRequest addToCartRequest;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;


    public RefillNotificationPresenter(@NonNull RefillNotificationContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        this.mContext = context;

    }


    @Override
    public void checkSecurityStatus() {
        mPetMedsApiService.getSecurityStatus()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<SecurityStatusResponse, Observable<ShoppingCartListResponse>>() {
                    @Override
                    public Observable<ShoppingCartListResponse> call(SecurityStatusResponse securityStatusResponse) {
                        if (securityStatusResponse.getSecurityStatus() == 0) {
                            mView.onSecurityStatusError();
                            return null;
                        }
                        return mPetMedsApiService
                                .addEasyRefillItemsToShoppingCart()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError", e.getLocalizedMessage());
                        if (RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection) {
                            mView.onShoppingCartError(mContext.getString(R.string.no_internet_caps));
                        } else {
                            mView.onShoppingCartError(e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {

                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.onShoppingCartSuccess(shoppingCartListResponse);
                        } else {

                            mView.onShoppingCartError(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                        }


                    }
                });
    }

    @Override
    public void start() {

    }

}
