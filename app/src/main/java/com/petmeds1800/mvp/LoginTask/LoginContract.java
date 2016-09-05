package com.petmeds1800.mvp.LoginTask;

import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Digvijay on 8/5/2016.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void setEmailError(String errorString);

        void setPasswordError(String errorString);

        void showErrorCrouton(CharSequence message, boolean span);

        void navigateToHome();
    }

    interface Presenter extends BasePresenter {

        boolean validateEmail(String email);

        boolean validatePassword(String password);
    }
}
