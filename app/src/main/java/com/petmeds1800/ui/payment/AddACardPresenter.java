package com.petmeds1800.ui.payment;

import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.Status;
import com.petmeds1800.ui.fragments.LoginFragment;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddACardPresenter implements AddACardContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private final AddACardContract.View mView;

    AddACardPresenter(AddACardContract.View view){
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
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {

                        if(mView.isActive()){
                            mView.paymentMethodApproved();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                    }

                    @Override
                    public void onNext(Status s) {

                    }
                });

    }

    @Override
    public void isCreditCardNumberValid() {

    }

    @Override
    public void start() {

    }
}
