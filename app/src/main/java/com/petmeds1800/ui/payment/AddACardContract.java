package com.petmeds1800.ui.payment;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.UpdateCardRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by Abhinav on 13/8/16.
 */
public interface AddACardContract{

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void paymentMethodApproved();

        void paymentMethodDisapproved(String errorMessage);

        void displayAddress(Address address);

        void populateData(Card card);

        /**
         * All error should be passed on through this method, applicable to "Editing a Card" flow.This would actually provide a way to the user to retry.
         * (For "Add a Card" flow we already have a tick action item button.)
         * @param errorMessage
         */
        void showErrorInMiddle(String errorMessage);

    }

    interface Presenter extends BasePresenter {

        void saveCard(CardRequest card);
        boolean isCreditCardNumberValid(String creditCardNumber);
        boolean isExpirationDateValid(int expirationMonth , int expirationYear);
        boolean isCvvValid(String cvv);
        boolean isBillingAddressAvailable();
        void getAddress(String addressId);
        void updateCard(UpdateCardRequest updateCardRequest);

    }

    /**
     * This interface would help AddAddressFragment to contact with AddressSelectionFragment
     */
    interface AddressSelectionListener {

        void setAddress(Address address, int requestCode);
    }
}
