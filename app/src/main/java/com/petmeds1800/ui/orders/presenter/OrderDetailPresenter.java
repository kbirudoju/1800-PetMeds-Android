package com.petmeds1800.ui.orders.presenter;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.ReOrderRequest;
import com.petmeds1800.model.ReOrderResponse;
import com.petmeds1800.ui.orders.OrderDetailContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 10/5/2016.
 */
public class OrderDetailPresenter implements OrderDetailContract.Presenter
{

   private OrderDetailContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    public OrderDetailPresenter(@NonNull OrderDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void reOrder(ReOrderRequest reOrderRequest) {
        mPetMedsApiService.reOrder(reOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReOrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ReOrderResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.onSuccess();
                        } else {
                            if (mView.isActive()) {
                                mView.onError(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {

    }
}
