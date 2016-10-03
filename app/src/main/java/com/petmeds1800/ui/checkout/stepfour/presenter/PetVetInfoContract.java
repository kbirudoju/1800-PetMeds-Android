package com.petmeds1800.ui.checkout.stepfour.presenter;

import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by pooja on 9/28/2016.
 */
public interface PetVetInfoContract {
    interface View extends BaseView<Presenter> {
        void setPetList( List<Pets> petList);
        void onError(String errorMessage);
        boolean isActive();
        void setVetList(List<Vet> vetList);
    }

    interface Presenter extends BasePresenter {
        void getPetListData();
        void getVetListData();
    }
    interface PetSelectionListener {
        void setPet(Pets pet);
    }

}
