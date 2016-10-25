package com.petmeds1800.ui.account;

import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 12-09-2016.
 */

public interface SignOutContract {

    interface View extends BaseView<Presenter> {

        void onSuccess();

        void onError(String errorMessage);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void sendDataToServer(String sessionConfigParam);
    }


}
