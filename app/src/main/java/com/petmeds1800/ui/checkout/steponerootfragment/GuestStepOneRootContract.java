package com.petmeds1800.ui.checkout.steponerootfragment;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShippingGroups;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by Sdixit on 26-09-2016.
 */

public interface GuestStepOneRootContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void navigateOnSuccess(ShoppingCartListResponse response);

        void onError(String errorMessage);

        void showErrorCrouton(CharSequence message, boolean span);

        void showWarningView(CharSequence message);

        void hideWarningView();

        void setAddress(Address address);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends BasePresenter {
        void saveGuestShippingAddressData(ShippingAddressRequest shippingAddressRequest);

        void extractAddress(ArrayList<ShippingGroups> shippingGroupses);
    }

    interface AddGuestNewAddressFragmentInteractionListener {
        boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId);
    }

}
