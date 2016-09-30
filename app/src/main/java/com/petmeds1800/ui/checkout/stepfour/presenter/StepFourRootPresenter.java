package com.petmeds1800.ui.checkout.stepfour.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SavePetVetRequest;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 9/30/2016.
 */
public class StepFourRootPresenter implements StepFourRootContract.Presenter{
    @Inject
    PetMedsApiService mPetMedsApiService;
    private StepFourRootContract.View mView;


    public StepFourRootPresenter(@NonNull StepFourRootContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void start() {

    }

    @Override
    public void applyPetVetInfo(SavePetVetRequest request) {
        mPetMedsApiService.savePetVet(request)
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
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse ) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                           if (mView.isActive()) {
                                mView.onSuccess();
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(shoppingCartListResponse.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }
    }

