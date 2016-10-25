package com.petmeds1800.ui.refillreminder;

import com.petmeds1800.model.refillreminder.request.RemoveRefillReminderRequest;
import com.petmeds1800.model.refillreminder.request.UpdateRefillReminderRequest;
import com.petmeds1800.model.refillreminder.response.MonthSelectListResponse;
import com.petmeds1800.model.shoppingcart.response.Status;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sarthak on 24-Oct-16.
 */

public interface EditReminderContract {

    interface View extends BaseView<EditReminderContract.Presenter> {
        boolean isActive();
        boolean postGeneralPopulateRefillReminderMonthList(MonthSelectListResponse monthSelectListResponse);
        boolean onError(String errorMessage);
        boolean onSuccessRemoveorUpdate(Status status);
    }

    interface Presenter extends BasePresenter {
        void getGeneralPopulateRefillReminderMonthList();
        void getUpdateRefillReminder(UpdateRefillReminderRequest updateRefillReminderRequest);
        void getRemoveRefillReminder(RemoveRefillReminderRequest removeRefillReminderRequest);
    }
}
