package com.petmeds1800.ui.pets.support;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 8/26/2016.
 */
public interface AddPetContract {
    interface View extends BaseView<Presenter> {
        boolean checkAndShowError(EditText auditEditText , TextInputLayout auditTextInputLayout , int errorStringId);



    }

    interface Presenter extends BasePresenter {
        void addPetData(AddPetRequest request);
    }
}
