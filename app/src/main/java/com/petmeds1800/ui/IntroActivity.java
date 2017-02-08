package com.petmeds1800.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.widget.LinearLayout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.ui.fragments.IntroFragment;
import com.petmeds1800.ui.fragments.dialog.BaseDialogFragment;
import com.petmeds1800.ui.fragments.dialog.LoadingGIFDialogFragment;
import com.petmeds1800.ui.fragments.dialog.OkCancelDialogFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.GetSessionCookiesHack;
import com.petmeds1800.util.Log;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/1/2016.
 */
public class IntroActivity extends AbstractActivity implements IntroFragment.OnIntroFinishedListener {

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private GetSessionCookiesHack mGetSessionCookiesHack;

    @BindView(R.id.container_layout)
    LinearLayout mContainerLayout;

    @Inject
    PetMedsApiService mApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //  setContentView(R.layout.activity_intro);
        PetMedsApplication.getAppComponent().inject(this);


        mGetSessionCookiesHack = new GetSessionCookiesHack() {
            @Override
            public void getSessionCookiesOnFinish(boolean doLogin, Throwable e) {
                int errorId = RetrofitErrorHandler.getErrorMessage(e);

                if (errorId == R.string.noInternetConnection) {
                    hideProgress();
                    showNonCancelableDialog(getString(errorId));
                    // showErrorCrouton(getString(errorId), false);

                } else if (!(e instanceof SecurityException)) {
                    hideProgress();
                    showNonCancelableDialog(e.getMessage());
                    // showErrorCrouton(e.getMessage(), false);

                } else if ((e instanceof SecurityException)) {
//                        initializeSessionConfirmationNumber();
                    hideProgress();
                    startHomeActivity();

                }
            }


            @Override
            public void getSessionCookiesShowProgress() {
                showProgress();
            }

            @Override
            public void getSessionCookiesHideProgress() {
                hideProgress();
            }
        };
    }

    @Override
    protected int getLayoutResource() {

        return R.layout.activity_intro;
    }

    @Override
    public void onIntroFragmentFinished() {
        SessionConfNumberResponse sessionConfNumberResponse = mPreferencesHelper.getSessionConfirmationResponse();
        if (sessionConfNumberResponse != null && sessionConfNumberResponse.getSessionConfirmationNumber() != null) {
            startHomeActivity();
        } else {
            showProgress();
            mGetSessionCookiesHack.doHackForGettingSessionCookies(false, mApiService);
        }


    }

    private void startHomeActivity() {
        mPreferencesHelper.setHasUserSeenIntro(true);
        startActivity(new HomeIntent(this));
        finish();
    }

    public void showErrorCrouton(CharSequence message, boolean span) {
        if (span) {
            Utils.displayCrouton(this, (Spanned) message, mContainerLayout);
        } else {
            Utils.displayCrouton(this, (String) message, mContainerLayout);
        }
    }


    public void showProgress() {
        try {
            startLoadingGif(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hideProgress() {
        try {
            stopLoadingGif(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showNonCancelableDialog(String errorMessage) {

        final OkCancelDialogFragment okCancelDialogFragment = OkCancelDialogFragment.newInstance(errorMessage,
                getString(R.string.error_txt),
                getString(R.string.label_retry),
                getString(R.string.label_exit));

        okCancelDialogFragment.setPositiveListener(new BaseDialogFragment.DialogButtonsListener() {
            @Override
            public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                okCancelDialogFragment.dismiss();
                showProgress();
                mGetSessionCookiesHack.doHackForGettingSessionCookies(false, mApiService);

            }
        });
        okCancelDialogFragment.setNegativeListener(new BaseDialogFragment.DialogButtonsListener() {
            @Override
            public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                okCancelDialogFragment.dismiss();
                finish();
            }
        });
        okCancelDialogFragment.setCancelable(false);
        okCancelDialogFragment.show(getSupportFragmentManager());
    }


}
