package com.petmeds1800.ui.vet;

import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 10/24/2016.
 */
public interface VetDetailContract {
    interface View extends BaseView<Presenter> {

        boolean isActive();

        void onSuccess();

        void onError(String errorMessage);
    }
    interface Presenter extends BasePresenter {
        void requestReferral(UpdateVetRequest request);
    }
}
