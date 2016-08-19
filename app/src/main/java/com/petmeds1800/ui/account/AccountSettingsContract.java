package com.petmeds1800.ui.account;


import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.User;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;
import com.petmeds1800.mvp.LoginTask.LoginContract;

/**
 * Created by Abhinav on 4/8/16.
 */
public interface AccountSettingsContract {


    interface View extends BaseView<Presenter> {

        void enableDoneAction();

        void enableEditAction();

        void enableEditTexts(boolean enable);

        void setUserData(User userData);

        void showSuccess();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        boolean validateName(String name);

        boolean validateEmail(String email);

        boolean validatePassword(String password);

        void findUserData();

        void saveSettings(UpdateAccountSettingsRequest updateAccountSettingsRequest);

    }
}
