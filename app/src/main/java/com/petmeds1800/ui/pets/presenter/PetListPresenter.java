package com.petmeds1800.ui.pets.presenter;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.PetList;
import com.petmeds1800.ui.pets.support.PetListContract;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 8/23/2016.
 */
public class PetListPresenter implements PetListContract.Presenter {
    @Inject
    PetMedsApiService mPetMedsApiService;

    private PetListContract.View mView;


    public PetListPresenter(@NonNull PetListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void setPetListData() {
        mPetMedsApiService
                .getPetList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(PetList list) {
                        if(list.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if(mView.isActive()){
                                mView.updatePetList(list.getPetList());
                            }
                        }else{
                            if(mView.isActive()){
                                mView.onError(list.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    @Override
    public void start() {
        setPetListData();
    }
}
