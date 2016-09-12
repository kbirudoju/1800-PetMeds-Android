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

        void setEmailError(String errorString);

        void setPasswordError(String errorString);

        void setNameError(String errorString, int viewId);

        void setAddressError(String errorString, int viewId);

        void setPostalCodeError(String errorString);

        void setPhoneError(String errorString);

        void showErrorCrouton(CharSequence message, boolean span);

        void navigateToHome();
    }

    interface Presenter extends BasePresenter {

        boolean validateEmail(String email);

        boolean validatePassword(String password);

        boolean validateUserName(String name);

        boolean validateAddress(String address);

        boolean validateCity(String city);

        boolean validatePostalCode(String postalCode);

        boolean validatePhoneNumber(String phoneNumber);
    }
}
