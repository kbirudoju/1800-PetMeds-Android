package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
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
    }

    interface Presenter extends BasePresenter {
        public void applyCreditCardPaymentMethod(CreditCardPaymentMethodRequest request);
    }
}
