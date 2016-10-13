package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.model.entities.MedicationReminderListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sdixit on 13-10-2016.
 */

public interface MedicationReminderListContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
        void onSuccess(MedicationReminderListResponse response);
        void onError(String errorMessage);
        void showErrorCrouton(CharSequence message, boolean span);
    }

    interface Presenter extends BasePresenter {
        void getMedicationReminderList();
    }
}
