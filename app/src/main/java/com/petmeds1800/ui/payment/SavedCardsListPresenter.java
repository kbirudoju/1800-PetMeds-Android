package com.petmeds1800.ui.payment;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.entities.MySavedCard;
import com.petmeds1800.model.entities.Status;
import com.petmeds1800.ui.fragments.LoginFragment;

import java.util.ArrayList;

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

    private SavedCardsListContract.View mView;

    public SavedCardsListPresenter(@NonNull SavedCardsListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getSavedCards() {

        mPetMedsApiService.getSavedCards(LoginFragment.sessionConfirmationNUmber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MySavedCard>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                    }

                    @Override
                    public void onNext(MySavedCard s) {
                        if(s.getCreditCardList() != null && s.getCreditCardList().size() > 0){
                            if (mView.isActive()) {
                                mView.showCardsListView(s.getCreditCardList());
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
