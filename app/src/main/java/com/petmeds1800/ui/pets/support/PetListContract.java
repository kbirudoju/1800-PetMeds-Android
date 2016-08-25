package com.petmeds1800.ui.pets.support;

import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by pooja on 8/23/2016.
 */
public interface PetListContract {
    interface View extends BaseView<Presenter> {
        void updatePetList( List<Pets> petList);

    }

    interface Presenter extends BasePresenter {
        void setPetListData();
    }
}
