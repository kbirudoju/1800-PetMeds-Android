package com.petmeds1800.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.entities.PetItemList;
import com.petmeds1800.model.entities.RefillItem;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.model.entities.Widget;
import com.petmeds1800.model.entities.WidgetListResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 12/8/2016.
 */
public class RefillNotificationPresenter implements RefillNotificationContract.Presenter  {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private RefillNotificationContract.View mView;
    private Context mContext;
    private  AddToCartRequest addToCartRequest;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;


    public RefillNotificationPresenter(@NonNull RefillNotificationContract.View view,Context context) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        this.mContext=context;

    }


    @Override
    public void checkSecurityStatus() {
        mPetMedsApiService.getSecurityStatus()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<SecurityStatusResponse, Observable<WidgetListResponse>>() {
                    @Override
                    public Observable<WidgetListResponse> call(SecurityStatusResponse securityStatusResponse) {
                        if (securityStatusResponse.getSecurityStatus() == 0) {
                            mView.onSecurityStatusError();
                            return null;
                        }
                        return mPetMedsApiService
                                .getWidgetData()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io());
                    }
                }).flatMap(new Func1<WidgetListResponse, Observable<ShoppingCartListResponse>>() {
            @Override
            public Observable<ShoppingCartListResponse> call(WidgetListResponse widgetListResponse) {
                if (widgetListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                    ArrayList<Widget> widgetData = widgetListResponse.getWidgets();
                    if (widgetData != null && widgetData.size() > 0) {
                        PetItemList petItem = extractRefillData(widgetData);
                        if (petItem != null) {
                            String productId = petItem.getSku().getParentProduct().getProductId();
                            String skuId = petItem.getSku().getSkuId();
                            int quantity = petItem.getRefillQuantity();
                            addToCartRequest = new AddToCartRequest(skuId, productId, quantity, mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                        }
                    }
                } else {
                    mView.onHomeWidgetError(widgetListResponse.getStatus().getErrorMessages().get(0));
                    return null;
                }

                return mPetMedsApiService
                        .addToCart(addToCartRequest)
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
                        if(RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection ) {
                            mView.onShoppingCartError(mContext.getString(R.string.no_internet_caps));
                        }else {
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

    private PetItemList extractRefillData(ArrayList<Widget> widgetData){
        for (int widgetCount = 0; widgetCount < widgetData.size(); widgetCount++) {
            if (widgetData.get(widgetCount).getWidgetType().equalsIgnoreCase(Constants.VIEW_TYPE_REFILL)) {
                ArrayList<RefillItem> refillItem = widgetData.get(widgetCount).getData().getRefillItems();
                if(refillItem!=null && refillItem.size()>0){
                    RefillItem refillListItem = refillItem.get(0);
                    ArrayList<PetItemList> petItemList = refillListItem.getPetItemList();
                    if(petItemList!=null && petItemList.size()>0) {
                        PetItemList petItem = petItemList.get(0);
                        petItem.getSku();
                        return petItem;
                    }
                }



            }
        }
        return null;
    }
}
