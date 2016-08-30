package com.petmeds1800.ui.payment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MySavedCard;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 11/8/16.
 */
public class SavedCardsListPresenter implements SavedCardsListContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private SavedCardsListContract.View mView;

    public SavedCardsListPresenter(@NonNull SavedCardsListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getSavedCards() {

        mPetMedsApiService
                .getSavedCards(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MySavedCard>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //notify about the error.It could be any type of error while getting data from the API
                        Log.e(SavedCardsListPresenter.class.getName(), e.getMessage());
                        if (mView.isActive()) {
                            mView.showErrorMessage(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onNext(MySavedCard s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)
                                && s.getCreditCardList() != null && s.getCreditCardList().size() > 0) {

                            if (mView.isActive()) {
                                mView.showCardsListView(s.getCreditCardList());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showNoCardsView();
                            }
                        }
                    }
                });

    }

    @Override
    public void start() {
        getSavedCards();
    }
}
