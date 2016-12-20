package com.petmeds1800.ui.refillreminder;

import com.petmeds1800.model.refillreminder.response.RefillReminderListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sarthak on 21-Oct-16.
 */

public interface ReminderListContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();
        boolean postGeneralPopulateRefillReminderList(RefillReminderListResponse refillReminderListResponse);
        boolean onError(String errorMessage);
        void showRetryView();
        void hideRetryView();
    }

    interface Presenter extends BasePresenter {
        void getGeneralPopulateRefillReminderList();
    }
}
