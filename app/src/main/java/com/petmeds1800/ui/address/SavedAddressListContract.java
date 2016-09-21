package com.petmeds1800.ui.address;

import com.petmeds1800.model.Address;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by Abhinav on 11/8/16.
 */
public interface SavedAddressListContract {


    interface View extends BaseView<Presenter> {

        boolean isActive();
        void showNoAddressView();
        void showAddressListView(List<Address> addressList);
        void startAddressUpdate(Address address);
        void showErrorMessage(String errorMessage);
    }

    interface Presenter extends BasePresenter {

        void getSavedAddress();
    }


}
