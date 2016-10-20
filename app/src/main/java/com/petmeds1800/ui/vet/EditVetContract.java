package com.petmeds1800.ui.vet;

import com.petmeds1800.model.RemoveVetRequest;
import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 10/19/2016.
 */
public interface EditVetContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void onSuccess(Vet vet);

        void onError(String errorMessage);
        void onPetRemoveSuccess();
    }
     interface Presenter extends BasePresenter {
        void updateVet(UpdateVetRequest request);
        void removeVet(RemoveVetRequest request);
    }
}
