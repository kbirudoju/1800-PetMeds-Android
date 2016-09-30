package com.petmeds1800.ui.checkout.stepfour.presenter;

import com.petmeds1800.model.entities.SavePetVetRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 9/30/2016.
 */
public interface StepFourRootContract {
    interface View extends BaseView<Presenter>
    {
        boolean isActive();
        void onSuccess();
        void onError(String errorMessage);
    }

    interface Presenter extends BasePresenter {
        void applyPetVetInfo(SavePetVetRequest request);
    }
}
