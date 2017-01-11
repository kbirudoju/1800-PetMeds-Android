package com.petmeds1800.ui.support;

import com.petmeds1800.mvp.BasePresenter;

/**
 * Created by Abhinav on 21/12/16.
 */
public interface HomeActivityContract {

    interface View {

        boolean isActive();

        void showNonCancelableDialog(String errorMessage);

        void moveAhead();

        void hideProgress();
    }

    interface Presenter extends BasePresenter {

        void getSecurityStatusFirst();
        void unsubscribeSubscription();

    }

}
