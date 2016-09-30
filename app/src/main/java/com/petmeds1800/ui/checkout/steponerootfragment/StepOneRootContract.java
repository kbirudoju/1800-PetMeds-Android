package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.model.entities.SavedShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 26-09-2016.
 */

public interface StepOneRootContract {
    interface View extends BaseView<StepOneRootContract.Presenter> {
        boolean isActive();
        void onSuccess(ShoppingCartListResponse response);
        void onError(String errorMessage);
        void showErrorCrouton(CharSequence message, boolean span);
    }

    interface Presenter extends BasePresenter {
         public void saveShippingAddress(SavedShippingAddressRequest request);
    }

}
