package com.petmeds1800.ui.vet;

import com.petmeds1800.model.SearchVetByZipCodeResponse;
import com.petmeds1800.model.VetList;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by pooja on 10/4/2016.
 */
public interface AddVetContract {
    interface View extends BaseView<Presenter> {

        boolean isActive();

        void onSuccess(ArrayList<VetList> vetList);

        void onError(String errorMessage);
    }
    interface Presenter extends BasePresenter {
        SearchVetByZipCodeResponse getVetList(String zipCode);
    }

}
