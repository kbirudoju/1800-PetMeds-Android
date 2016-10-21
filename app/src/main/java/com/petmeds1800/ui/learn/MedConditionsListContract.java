package com.petmeds1800.ui.learn;

import com.petmeds1800.model.entities.NameValueData;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by Digvijay on 10/20/2016.
 */

public interface MedConditionsListContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void populateConditionsListView(List<NameValueData> medConditionList);

        void showErrorCrouton(CharSequence message, boolean span);

        void showWebViewFragment(String medConditionName);

        void showRetryView(String errorMessage);
    }

    interface Presenter extends BasePresenter {

        void getConditionsList();
    }
}
