package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.GuestCheckoutPaymentReuest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class GuestStepThreeRootContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void onSuccessAddressAddition(AddAddressResponse addAddressResponse);

        void onSuccessCreditCardAddition(Object response);

        void onError(String errorMessage);

        void showErrorCrouton(CharSequence message, boolean span);

        void onSuccessCreditCardPayment(ShoppingCartListResponse response);
    }

    interface Presenter extends BasePresenter{

        void applyCreditCardPaymentMethod(AddressRequest addressRequest, CardRequest cardRequest);
    }
    /**
     * This interface should be implemented by the child AddGuestCardFragment
     * in order to pass on the card object
     */
    public interface PaymentMethodInteractionListener {

        boolean isActive();

        /**
         *
         * @return true is there is an error and false otherwise
         */
        boolean checkAndShowError();

        CardRequest getCard();

    }

    public interface AddAddressInteractionListener {

        boolean isActive();

        /**
         *
         * @return true is there is an error and false otherwise
         */
        boolean validateFields();

        void initializeGuestAddressRequest();

        AddressRequest getAddressRequest();

    }

}
