package com.petmeds1800.ui.shoppingcart.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.ui.shoppingcart.ShoppingCartListContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

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
    public void getShoppingCartList() {
        mPetMedsApiService.getShoppingCartList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
//                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populateShoppingCartResponse(shoppingCartListResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }

                });


    }

    @Override
    public void start() {

    }
}
