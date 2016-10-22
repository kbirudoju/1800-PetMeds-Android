package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.shoppingcart.request.CardDetailRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class StepThreeRootContract {

    interface View extends BaseView<StepThreeRootContract.Presenter> {

        boolean isActive();

        void onSuccessCreditCardPayment(ShoppingCartListResponse response);

        void onError(String errorMessage);

        void showErrorCrouton(CharSequence message, boolean span);

        void setUpdatedAddressOnSuccess(Address address);

        void errorOnUpdateAddress();
    }

    interface Presenter extends BasePresenter {

        void applyCreditCardPaymentMethod(CreditCardPaymentMethodRequest request);

        void getBillingAddressById(String sessionConfig, String addressId);

    }

}
