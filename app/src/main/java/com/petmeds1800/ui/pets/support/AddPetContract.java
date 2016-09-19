package com.petmeds1800.ui.pets.support;

import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.model.entities.AgeListResponse;
import com.petmeds1800.model.entities.PetBreedTypeListResponse;
import com.petmeds1800.model.entities.PetMedicalConditionResponse;
import com.petmeds1800.model.entities.PetMedicationResponse;
import com.petmeds1800.model.entities.PetTypesListResponse;
import com.petmeds1800.model.entities.RemovePetRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by pooja on 8/26/2016.
 */
public interface AddPetContract {

    interface View extends BaseView<Presenter> {

        boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId);

        boolean isActive();

        void onSuccess();

        void onError(String errorMessage);

        void populateData(PetMedicationResponse response);
        void populatePetAgeData(AgeListResponse reponse);
        void populatePetTypeData(PetTypesListResponse response);
        void populatePetBreedTypeData(PetBreedTypeListResponse reponse);
        void populatePetMedicalconditionsData(PetMedicalConditionResponse response);


    }

    interface Presenter extends BasePresenter {

        void addPetData(AddPetRequest request);

        void updatePetData(AddPetRequest request);

        void removePet(RemovePetRequest request);

        void populatePetMedicationsList();
        void populatePetAgeList();
        void populatePetTypeList();
        void pouplatePetBreedTypeList();
        void populatePetMedicalconditionsList();
    }
}
