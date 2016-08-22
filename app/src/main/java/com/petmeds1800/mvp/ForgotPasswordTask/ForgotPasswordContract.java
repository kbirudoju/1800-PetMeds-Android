package com.petmeds1800.mvp.ForgotPasswordTask;

import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Digvijay on 8/19/2016.
 */
public interface ForgotPasswordContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void setEmailError(String errorString);
    }

    interface Presenter extends BasePresenter {

        boolean validateEmail(String email);
    }
}
