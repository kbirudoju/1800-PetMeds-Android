package com.petmeds1800.mvp.SignupTask;

import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Digvijay on 9/9/2016.
 */
public interface SignUpContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void setErrorOnInput(String errorString, int viewId);

        void showErrorCrouton(CharSequence message, boolean span);

        void showWarningView(CharSequence message);

        void hideWarningView();

        void navigateToHome();

        void onStatesListReceived(String[] statesArray);

        void onCountryListReceived(String[] countryArray);
    }

    interface Presenter extends BasePresenter {

        boolean validateEmail(String email);

        boolean validatePassword(String password);

        boolean validateFirstName(String firstName);

        boolean validateLastName(String lastName);

        boolean validateAddress(String address);

        boolean validateCity(String city);

        boolean validatePostalCode(String postalCode);

        boolean validatePhoneNumber(String phoneNumber);

        void getStatesList();

        String getStateCode(String stateName);

        void getCountriesList();

        String getCountryCode(String countryName);
    }
}
