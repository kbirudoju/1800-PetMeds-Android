package com.petmeds1800.ui.payment;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.entities.CardRequest;
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
    }

    interface Presenter extends BasePresenter {

        void saveCard(CardRequest card);
        boolean isCreditCardNumberValid(String creditCardNumber);
        boolean isExpirationDateValid(int expirationMonth , int expirationYear);
        boolean isCvvValid(String cvv);
        boolean isBillingAddressAvailable();

    }

    /**
     * This interface would help AddAddressFragment to contact with AddressSelectionFragment
     */
    interface AddressSelectionListener {

        void setAddress(Address address);

    }
}