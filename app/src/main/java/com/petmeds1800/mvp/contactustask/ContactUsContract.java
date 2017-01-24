package com.petmeds1800.mvp.contactustask;

import com.petmeds1800.model.ContactUs;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 1/24/2017.
 */
public interface ContactUsContract {
    interface View extends BaseView<Presenter> {
        void setContactData(ContactUs contactUs);
        void onError(String errorMessage);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getContactData();
    }
}
