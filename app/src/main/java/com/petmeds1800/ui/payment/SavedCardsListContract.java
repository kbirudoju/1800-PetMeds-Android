package com.petmeds1800.ui.payment;

import com.petmeds1800.model.Card;
import com.petmeds1800.model.shoppingcart.request.CardDetailRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by Abhinav on 11/8/16.
 */
public interface SavedCardsListContract {


    interface View extends BaseView<Presenter> {

        boolean isActive();
        void showNoCardsView();
        void showCardsListView(List<Card> cardsList);
        void showErrorMessage(String errorMessage);
        void startCardUpdate(Card card);
        void showCroutanMessage(String errorMessage);
    }

    interface Presenter extends BasePresenter {

        void getSavedCards();
        void getCardDetaiBypaymentCardKey(CardDetailRequest cardDetailRequest);
    }

}
