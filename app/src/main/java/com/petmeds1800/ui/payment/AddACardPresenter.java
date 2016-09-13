package com.petmeds1800.ui.payment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.RemoveCardRequest;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddEditCardResponse;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.UpdateCardRequest;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.support.v4.app.Fragment;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddACardPresenter implements AddACardContract.Presenter {

    private static final int CREDIT_CARD_DIGITS_RULE_1 = 16;

    private static final int CREDIT_CARD_DIGITS_RULE_2 = 15;

    private static final int CVV_DIGITS_RULE_1 = 3;

    private static final int CVV_DIGITS_RULE_2 = 4;

    private final AddACardContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    AddACardPresenter(AddACardContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void saveCard(CardRequest cardRequest) {
        //show the progress
        mPetMedsApiService.addPaymentCard(cardRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddEditCardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("AddACard", e.getMessage());
                    }

                    @Override
                    public void onNext(AddEditCardResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.paymentMethodApproved();
                            }
                        } else {
                            Log.d("AddACard", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.paymentMethodDisapproved(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });

    }


    @Override
    public boolean isCreditCardNumberValid(String creditCardNumber) {
        if (creditCardNumber.length() == CREDIT_CARD_DIGITS_RULE_1
                || creditCardNumber.length() == CREDIT_CARD_DIGITS_RULE_2) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExpirationDateValid(int expirationMonth, int expirationYear) {
        if (expirationMonth <= 0 && expirationYear <= 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isCvvValid(String cvv) {
        if (cvv.length() == CVV_DIGITS_RULE_1 || cvv.length() == CVV_DIGITS_RULE_2) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBillingAddressAvailable() {
        return true;
    }

    @Override
    public void getAddress(String addressId) {
        //show the progress
        mPetMedsApiService
                .getAddressById(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                        addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("GetAddressById", e.getMessage());
                        if (mView.isActive()) {
                            mView.showErrorInMiddle(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(AddAddressResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.displayAddress(s.getProfileAddress());
                            }
                        } else {
                            Log.d("GetAddressById", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.showErrorInMiddle(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });

    }

    @Override
    public void updateCard(UpdateCardRequest updateCardRequest) {
        mPetMedsApiService.updateCard(updateCardRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddEditCardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("UpdateAddress", e.getLocalizedMessage());
                        if (mView.isActive()) {
                            mView.paymentMethodDisapproved(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onNext(AddEditCardResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.paymentMethodApproved();
                            }
                        } else {
                            Log.d("UpdateAddress", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.paymentMethodDisapproved(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void removeCard(RemoveCardRequest removeCardRequest) {
        mPetMedsApiService.removeCard(removeCardRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddEditCardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("RemoveACard", e.getMessage());
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (errorId != 0) { //internet connection error. Unknownhost or SocketTimeout exception
                            if (mView.isActive()) {
                                mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                            }
                        }
                    }

                    @Override
                    public void onNext(AddEditCardResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.cardRemoved();
                            }
                        } else {
                            Log.d("AddACard", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.showErrorCrouton(s.getStatus().getErrorMessages().get(0), false);
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}
