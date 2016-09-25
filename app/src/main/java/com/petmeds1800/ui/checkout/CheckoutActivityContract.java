package com.petmeds1800.ui.checkout;

import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.HashMap;

/**
 * Created by Abhinav on 25/9/16.
 */
public interface CheckoutActivityContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        //TODO once Sarthak done with CartFragment implementation.We will define this method.
//        void setShoppingCart(ShoppingCart shoppingCart);

        void setCheckoutSteps(CheckoutSteps checkoutSteps);

        /**
         * It should be used to start the next step by making the appropiate fragment transaction
         * @param nextStepCode represents the current active step
         */
        void startNextStep(String nextStepCode);

        void showErrorCrouton(CharSequence message, boolean span);

        void showErrorInMiddle(String errorMessage);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void initializeCheckout(HashMap<String, String> itemsDetail);
    }

    interface StepsFragmentInteractionListener {

        /**
         * It should be used to draw the correct states on the top navigation circles
         * @param lastCompletedStep represents the last completed step
         */
        void setLastCompletedSteps(String lastCompletedStep);

        void setActiveStep(String activeStep);

        void moveToNext(String currentStep);
    }

}
