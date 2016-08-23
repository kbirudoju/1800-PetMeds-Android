package com.petmeds1800.ui.address;

import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Abhinav on 13/8/16.
 */
public interface AddAddressContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void addressAdded();

        void addressAdditionFailed(String errorMessage);
    }

    interface Presenter extends BasePresenter {

        void saveAddress(AddressRequest card);

    }
}
