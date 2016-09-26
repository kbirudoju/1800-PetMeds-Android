package com.petmeds1800.ui.checkout;

import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Digvijay on 9/27/2016.
 */
public interface StepTwoContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void showErrorCrouton(CharSequence message, boolean span);

        void onSuccessShippingOptions(String htmlResponse);

        void onErrorShippingOptions();

        void onSuccessShippingMethods(ShippingMethodsResponse shippingMethodsResponse);

        void onErrorShippingMethods();
    }

    interface Presenter extends BasePresenter {

        void populateShippingMethodsListRecycler();

        void showShippingOptions();
    }
}
