package com.petmeds1800.ui.account;

import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;



public interface AccountContract {

    interface View extends BaseView<Presenter> {

        void onSignoutSuccess();

        void onError(String errorMessage);

        boolean isActive();

        void navigateToCartFragment();
    }

    interface Presenter extends BasePresenter {

        void signout(String sessionConfigParam);

        void addEasyRefillReorder();
    }


}
