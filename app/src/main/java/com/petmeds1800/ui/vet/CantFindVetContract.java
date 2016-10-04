package com.petmeds1800.ui.vet;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.petmeds1800.model.AddVetRequest;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 10/3/2016.
 */
public interface CantFindVetContract {
    interface View extends BaseView<Presenter> {
        boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId);

        boolean isActive();

        void onSuccess(Vet vet);

        void onError(String errorMessage);
    }
    interface Presenter extends BasePresenter {
        void addVetData(AddVetRequest addVetRequest);
    }
    interface VetSelectionListener {
        void setVet(Vet vet);
    }
}
