package com.petmeds1800.ui.payment;

import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.AddACardResponse;
import com.petmeds1800.model.entities.CardRequest;

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
    @Inject
    PetMedsApiService mPetMedsApiService;

    private final AddACardContract.View mView;

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
                .subscribe(new Subscriber<AddACardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("AddACard", e.getMessage());
                    }

                    @Override
                    public void onNext(AddACardResponse s) {
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
        if (creditCardNumber.length() == CREDIT_CARD_DIGITS_RULE_1 || creditCardNumber.length() == CREDIT_CARD_DIGITS_RULE_2) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExpirationDateValid(int expirationMonth, int expirationYear) {
        if (expirationMonth <= 0 && expirationYear <= 0)
            return false;
        else
            return true;
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
    public void start() {

    }
}
