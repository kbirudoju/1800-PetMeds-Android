package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by Sdixit on 20-10-2016.
 */

public interface MedicationReminderItemListContract {

    interface View extends BaseView<MedicationReminderItemListContract.Presenter> {

        boolean isActive();

        void onSuccess(ArrayList<OrderList> list);

        void onError(String errorMessage);

        void showErrorCrouton(CharSequence message, boolean span);
    }

    interface Presenter extends BasePresenter {

        void getOrderList();
    }
    interface AddEditMedicationReminderListener{
        void setItemDescription(String productName,String itemDescription);
    }
}
