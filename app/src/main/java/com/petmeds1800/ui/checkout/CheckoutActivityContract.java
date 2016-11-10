package com.petmeds1800.ui.checkout;

import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.HashMap;

/**
 * Created by Abhinav on 25/9/16.
 */
public interface CheckoutActivityContract {

    interface View extends BaseView<Presenter> {

        void hideProgress();

        void showProgress();

        //TODO once Sarthak done with CartFragment implementation.We will define this method.
//        void setShoppingCart(ShoppingCart shoppingCart);

        void setCheckoutSteps(CheckoutSteps checkoutSteps);

        /**
         * It should be used to start the next step by making the appropiate fragment transaction
         *
         * @param nextStepCode represents the current active step
         */
        void startNextStep(String nextStepCode);

        void showErrorCrouton(CharSequence message, boolean span);

        void showErrorInMiddle(String errorMessage);

        boolean isActive();

        void startNextStep(String stepName, ShoppingCartListResponse shoppingCartListResponse, boolean isReviewOn);

        void setSecurityStatus(int securityStatus);
    }

    interface Presenter extends BasePresenter {

        void initializeCheckout(HashMap<String, String> itemsDetail);

        void checkSecurityStatus();
    }

    interface StepsFragmentInteractionListener {

        void startNextStep(String stepName, ShoppingCartListResponse shoppingCartListResponse);

        /**
         * It should be used to draw the correct states on the top navigation circles
         *
         * @param lastCompletedStep represents the last completed step
         */
        void setLastCompletedSteps(String lastCompletedStep);

        void setActiveStep(String activeStep);

        void moveToNext(String currentStep, ShoppingCartListResponse mShoppingCartListResponse);
    }

}
