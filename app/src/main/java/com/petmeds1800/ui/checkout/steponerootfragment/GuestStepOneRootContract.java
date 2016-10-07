package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by Sdixit on 26-09-2016.
 */

public interface GuestStepOneRootContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void navigateOnSuccess(ShoppingCartListResponse response);

        void onError(String errorMessage);

        void showErrorCrouton(CharSequence message, boolean span);
    }

    interface Presenter extends BasePresenter {
        void saveGuestShippingAddressData(ShippingAddressRequest shippingAddressRequest);
    }

    interface AddGuestNewAddressFragmentInteractionListener {
        boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId);
    }

}
