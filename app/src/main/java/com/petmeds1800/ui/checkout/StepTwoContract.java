package com.petmeds1800.ui.checkout;

import com.petmeds1800.model.entities.ShippingMethodsRequest;
import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Digvijay on 9/27/2016.
 */
public interface StepTwoContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();
        void showProgress();

        void hideProgress();

        void showActivityProgress();

        void hideActivityProgress();

        void showErrorCrouton(CharSequence message, boolean span);

        void onSuccessShippingOptions(String htmlResponse);

        void onErrorShippingOptions();

        void onSuccessShippingMethods(ShippingMethodsResponse shippingMethodsResponse);

        void onErrorShippingMethods();

        void onSuccessShippingMethodsApplied(ShoppingCartListResponse shoppingCartListResponse);

        void onErrorShippingMethodsApplied(String error);
    }

    interface Presenter extends BasePresenter {

        void populateShippingMethodsListRecycler();

        void showShippingOptions();

        void applyShippingMethods(ShippingMethodsRequest shippingMethodsRequest);
    }
}
