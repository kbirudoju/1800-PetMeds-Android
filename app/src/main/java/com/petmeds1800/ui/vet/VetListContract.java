package com.petmeds1800.ui.vet;

import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by pooja on 10/12/2016.
 */
public interface VetListContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void onSuccess(ArrayList<Vet> vetList);

        void onError(String errorMessage);
    }
    interface Presenter extends BasePresenter {
        void getVetListData();
    }
}
