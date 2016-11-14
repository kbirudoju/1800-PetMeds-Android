package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.model.entities.SavedShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 26-09-2016.
 */

public interface StepOneRootContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void onSuccess(ShoppingCartListResponse response);

        void onError(String errorMessage);

        void showWarningView(CharSequence message);

        void hideWarningView();

        void showErrorCrouton(CharSequence message, boolean span);
    }

    interface Presenter extends BasePresenter {
        void saveShippingAddress(SavedShippingAddressRequest request);
    }

}
