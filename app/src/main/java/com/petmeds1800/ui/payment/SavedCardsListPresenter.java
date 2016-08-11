package com.petmeds1800.ui.payment;

import android.support.annotation.NonNull;

import com.petmeds1800.model.Card;

import java.util.ArrayList;

/**
 * Created by Abhinav on 11/8/16.
 */
public class SavedCardsListPresenter implements SavedCardsListContract.Presenter {

    private SavedCardsListContract.View mView;

    public SavedCardsListPresenter(@NonNull SavedCardsListContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getSavedCards() {

        //create test data
        ArrayList<Card> cardsList = new ArrayList<>();
        cardsList.add(new Card("****1234","VISA","Mar 2021",true));
        cardsList.add(new Card("****5678","VISA","Mar 2021",false));
        cardsList.add(new Card("****9999", "VISA", "Mar 2021", false));
        cardsList.add(new Card("****5555", "VISA", "Mar 2021", false));

        if(mView.isActive()){
            mView.showCardsListView(cardsList);
//            mView.showNoCardsView();
        }
    }

    @Override
    public void start() {
        getSavedCards();
    }
}
