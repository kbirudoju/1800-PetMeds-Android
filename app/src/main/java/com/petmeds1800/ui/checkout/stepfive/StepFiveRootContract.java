package com.petmeds1800.ui.checkout.stepfive;

import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.OrderReviewSubmitResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class StepFiveRootContract {

    interface View extends BaseView<StepFiveRootContract.Presenter> {

        boolean isActive();

        void populateOrderReviewDetails(OrderReviewSubmitResponse response);

        void onError(String errorMessage);

        void showErrorCrouton(CharSequence message, boolean span);

        void navigateOnOrderConfirmation(CommitOrderResponse response);
    }

    interface Presenter extends BasePresenter {

        void getOrderReviewDetails(String sessionConfig);

        void submitComittedOrderDetails(String sessionConfig);
    }
}
