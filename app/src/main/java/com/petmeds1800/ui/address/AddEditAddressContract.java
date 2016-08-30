package com.petmeds1800.ui.address;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.petmeds1800.model.UsaState;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by Abhinav on 13/8/16.
 */
public interface AddEditAddressContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void addressAdded();

        void addressUpdated();

        void showErrorMessage(String errorMessage);

        boolean checkAndShowError(EditText auditEditText , TextInputLayout auditTextInputLayout , int errorStringId);

        void usaStatesListReceived(String[] usaStateArray);

        void countryListReceived(String[] countryArray);
    }

    interface Presenter extends BasePresenter {

        void saveAddress(AddressRequest card);

        void getUsaStatesList();

        String getUsaStateCode(String usaStateName);

        void getCountryList();

        String getCountryCode(String countryName);

        void updateAddress(AddressRequest addressRequest);
    }
}
