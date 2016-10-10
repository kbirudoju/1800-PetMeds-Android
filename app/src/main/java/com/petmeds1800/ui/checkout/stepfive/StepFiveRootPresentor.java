package com.petmeds1800.ui.checkout.stepfive;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.CommitOrderRequest;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.OrderReviewSubmitResponse;

import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class StepFiveRootPresentor implements StepFiveRootContract.Presenter {

    private StepFiveRootContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    public StepFiveRootPresentor(StepFiveRootContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getOrderReviewDetails(String sessionConfig) {
        mPetMedsApiService.getOrderReviewDetails(sessionConfig)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderReviewSubmitResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.showErrorCrouton(e.getLocalizedMessage(), false);

                    }

                    @Override
                    public void onNext(OrderReviewSubmitResponse orderReviewSubmitResponse) {
                        if (orderReviewSubmitResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.populateOrderReviewDetails(orderReviewSubmitResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(orderReviewSubmitResponse.getStatus().getErrorMessages().get(0),
                                        false);
                            }
                        }

                    }
                });
    }

    @Override
    public void submitComittedOrderDetails(CommitOrderRequest commitOrderRequest) {
        mPetMedsApiService.submitCommitedOrderDetails(commitOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommitOrderResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mView.showErrorCrouton(e.getLocalizedMessage(), false);
                        Log.v("error is ::::: ","error is ::::::::::::::::::::::::::>>>>>>>>>>>>>>>>>>>>>>>>> "+e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(CommitOrderResponse commitOrderResponse) {
                        if (commitOrderResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.navigateOnOrderConfirmation(commitOrderResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(commitOrderResponse.getStatus().getErrorMessages().get(0),
                                        false);
                            }
                        }

                    }
                });
    }
}

