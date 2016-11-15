package com.petmeds1800.ui.checkout.stepfive;

import com.petmeds1800.model.entities.CommitOrderRequest;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.Item;
import com.petmeds1800.model.entities.OrderReviewSubmitResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

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

        void setPetVetInfo(String petVetInfo);
    }

    interface Presenter extends BasePresenter {

        void getOrderReviewDetails(String sessionConfig);

        void submitComittedOrderDetails(CommitOrderRequest commitOrderRequest);
        void populatePetVetInfo(final ArrayList<Item> items, final ArrayList<String> applicableSteps);

    }
}
