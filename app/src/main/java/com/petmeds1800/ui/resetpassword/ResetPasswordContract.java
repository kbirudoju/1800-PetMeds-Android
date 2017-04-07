package com.petmeds1800.ui.resetpassword;

import com.petmeds1800.model.entities.CheckResetPasswordTokenRequest;
import com.petmeds1800.model.entities.PasswordResetResponse;
import com.petmeds1800.model.entities.SaveResetPasswordRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Siddharth on 4/4/2017.
 */

public class ResetPasswordContract {
    interface View extends BaseView<ResetPasswordContract.Presenter> {
        boolean isActive();
        void onSuccess(PasswordResetResponse response);
        void onSavedResetPasswordDetailsSuccess();
        void showErrorCrouton(CharSequence message, boolean span);
        void onTokenExpired();
        void onSignOutSuccess(boolean isForcedSignOut);
    }

    interface Presenter extends BasePresenter {
        void checkResetPasswordLinkValidity(CheckResetPasswordTokenRequest checkResetPasswordTokenRequest);
        void saveResetPasswordDetails(SaveResetPasswordRequest saveResetPasswordRequest);
        boolean validatePassword(String password);
        void signout();
    }
}
