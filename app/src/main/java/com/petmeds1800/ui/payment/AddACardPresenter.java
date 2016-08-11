package com.petmeds1800.ui.payment;

import com.petmeds1800.model.Card;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddACardPresenter implements AddACardContract.Presenter {

    private final AddACardContract.View mView;

    AddACardPresenter(AddACardContract.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void saveCard(Card card) {
        //show the progress

        //after successful response from API send a confirmation
        if(mView.isActive()) {
            mView.paymentMethodApproved();
        }
    }

    @Override
    public void isCreditCardNumberValid() {

    }

    @Override
    public void start() {

    }
}
