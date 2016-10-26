package com.petmeds1800.util;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sarthak on 26-Oct-16.
 */

public class AsyncUpdateShoppingCartIconCountThread  {

    private PetMedsApiService mPetMedsApiService;
    private GeneralPreferencesHelper mPreferencesHelper;
    private String API_SUCCESS_CODE = "SUCCESS";
    private Handler mHandler;
    private boolean isSuccess = false;
    private String retValueCount;


    public AsyncUpdateShoppingCartIconCountThread(PetMedsApiService mPetMedsApiService, GeneralPreferencesHelper mPreferencesHelper, Handler mHandler) {
        this.mPetMedsApiService = mPetMedsApiService;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mHandler = mHandler;
    }

    public void UpdateValueThreadResponse(String val){
        Message msg = Message.obtain(null, Constants.KEY_COMPLETED_ASYN_COUNT_FETCH);
        Bundle b = new Bundle();
        b.putString(Constants.KEY_SHOPPING_CART_ICON_VALUE, retValueCount);
        b.putBoolean(Constants.KEY_SHOPPING_CART_ASYNC_SUCCESS,isSuccess);
        msg.setData(b);

        try {
            mHandler.sendMessage(msg);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAsyncUpdateShoppingCartIconCountThread(){
        Runnable runnable = new FetchCartListCountRunnable();
        try {
            new Thread(runnable).start();
        } catch (Exception e){
            retValueCount = e.getMessage();
            e.printStackTrace();
            UpdateValueThreadResponse(retValueCount);
        }
    }

    private class FetchCartListCountRunnable implements Runnable{

        @Override
        public void run() {
            mPetMedsApiService.getGeneralPopulateShoppingCart(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShoppingCartListResponse>() {

                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) { retValueCount = e.getMessage();  }

                @Override
                public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                    if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                        isSuccess = true;
                        retValueCount = String.valueOf(shoppingCartListResponse.getItemCount());
                        UpdateValueThreadResponse(retValueCount);
                    } else {
                        retValueCount = shoppingCartListResponse.getStatus().getErrorMessages().get(0);
                        UpdateValueThreadResponse(retValueCount);
                    }
                }
            });
        }
    }
}
