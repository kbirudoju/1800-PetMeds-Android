package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class StepThreeRootPresentor implements StepThreeRootContract.Presenter {

    private StepThreeRootContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    public StepThreeRootPresentor(StepThreeRootContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }


    @Override
    public void applyCreditCardPaymentMethod(CreditCardPaymentMethodRequest request) {
        mPetMedsApiService.applyCreditCardPaymentMethod(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccessCreditCardPayment(shoppingCartListResponse);
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
