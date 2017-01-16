package com.petmeds1800.ui.account;

import android.support.annotation.NonNull;

import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SessionConfigRequest;
import com.petmeds1800.model.entities.SignOutResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 12-09-2016.
 */

public class AccountPresenter implements AccountContract.Presenter {

    private AccountContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mGeneralPreferencesHelper;

    public AccountPresenter(@NonNull AccountContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void signout(final String sessionConfigParam) {
        mPetMedsApiService.logout(new SessionConfigRequest(sessionConfigParam))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignOutResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("SignouPresenter", e.getMessage());
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("Signout", "retrying after session renew");

                            signout(mGeneralPreferencesHelper.getSessionConfirmationResponse()
                                    .getSessionConfirmationNumber());

                            return;

                        }

                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(SignOutResponse s) {
                        Log.d("SignOutResponse", s.toString());
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSignoutSuccess();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(
                                        s.getStatus().getErrorMessages().size() > 0 ? s.getStatus().getErrorMessages()
                                                .get(0) : null);
                            }
                        }

                    }
                });
    }

    @Override
    public void addEasyRefillReorder() {

        mPetMedsApiService.addEasyRefillItemsToShoppingCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("addEasyRefillReorder", "retrying after session renew");

                            addEasyRefillReorder();

                            return;

                        }
                        else {
                            if(mView.isActive()){
                                mView.onError(e.getLocalizedMessage());
                            }
                        }

                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        Log.d("addEasyRefillReorder", shoppingCartListResponse.toString());
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.navigateToCartFragment();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(
                                        shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                            }
                        }
                    }
                });
    }


    @Override
    public void start() {

    }
}
