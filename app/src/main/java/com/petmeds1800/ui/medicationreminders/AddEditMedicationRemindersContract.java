package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.model.entities.AddMedicationReminderRequest;
import com.petmeds1800.model.entities.AddMedicationReminderResponse;
import com.petmeds1800.model.entities.RemoveMedicationReminderRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 14-10-2016.
 */

public class AddEditMedicationRemindersContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
        void onAddEditSuccess(AddMedicationReminderResponse response);
        void onRemoveSuccess();
        void onError(String errorMessage);
        void showErrorCrouton(CharSequence message, boolean span);
    }

    interface Presenter extends BasePresenter {
        void saveReminders(AddMedicationReminderRequest request);
        void updateReminders(AddMedicationReminderRequest request);
        void removeMedicationReminders(RemoveMedicationReminderRequest removeMedicationReminderRequest);
    }

}
