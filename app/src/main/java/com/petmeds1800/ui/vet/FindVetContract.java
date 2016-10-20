package com.petmeds1800.ui.vet;

import com.petmeds1800.model.VetList;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by pooja on 10/18/2016.
 */
public interface FindVetContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void onSuccess(ArrayList<VetList> vetList);

        void onError(String errorMessage);
    }
    public interface Presenter extends BasePresenter {
        void getVetList(String zipCode);
    }
}
