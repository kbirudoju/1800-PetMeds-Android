package com.petmeds1800.ui.fragments.dialog;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.mtramin.rxfingerprint.data.FingerprintAuthenticationResult;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.ForgotPasswordRequest;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * Created by Digvijay on 8/24/2016.
 */
public class FingerprintAuthenticationDialog extends DialogFragment {

    @BindView(R.id.txv_fingerprint_status)
    TextView mFingerprintStatus;

    @BindView(R.id.imv_fingerprint_icon)
    ImageView mFingerprintIcon;

    @BindView(R.id.fingerprint_container)
    View mFingerprintContent;

    @BindView(R.id.login_container)
    View mLoginContent;

    @BindView(R.id.forgot_password_container)
    View mForgotPasswordContent;

    @BindView(R.id.cancel_button)
    TextView mCancelButton;

    @BindView(R.id.second_dialog_button)
    TextView mSecondDialogButton;

    @BindView(R.id.txv_title_dialog_fingerprint)
    TextView mDialogTitle;

    @BindView(R.id.password_input)
    TextInputLayout mPasswordInput;

    @BindView(R.id.password_edit)
    EditText mPasswordEdit;

    @BindView(R.id.email_input)
    TextInputLayout mEmailInput;

    @BindView(R.id.email_edit)
    EditText mEmailEdit;

    @BindView(R.id.imv_email_sent)
    ImageView mImageEmailSent;

    @BindView(R.id.txv_email_sent)
    TextView mTextEmailSent;

    @BindView(R.id.label_user_email)
    TextView mEmailTextLogin;

    @BindView(R.id.txv_email)
    TextView mEmailTextForgotPassword;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private Subscription fingerprintSubscription = Subscribers.empty();

    private static final int PASSWORD_LENGTH = 8;

    private Stage mStage = Stage.FINGERPRINT;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private String loginEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(STYLE_NO_TITLE, 0);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
        ButterKnife.bind(this, view);
        authenticateFingerprint();
        return view;
    }

    @OnClick(R.id.cancel_button)
    void onCancelButtonClick() {
        if (mStage == Stage.FORGOT_PASSWORD) {
            mStage = Stage.LOGIN;
            updateStage();
            return;
        } else if (mStage == Stage.FINGERPRINT) {
            navigateToHome();
        } else if (mStage == Stage.LOGIN && RxFingerprint.isAvailable(getActivity())) {
            mStage = Stage.FINGERPRINT;
            updateStage();
            return;
        } else if (mStage == Stage.LOGIN && RxFingerprint.isUnavailable(getActivity())) {
            navigateToHome();
        }
        dismiss();
    }

    @OnClick(R.id.second_dialog_button)
    void onSecondButtonClick() {
        if (mStage == Stage.FINGERPRINT) {
            gotoLogin();
        } else if (mStage == Stage.LOGIN) {
            doLogin();
        } else if (mStage == Stage.FORGOT_PASSWORD) {
            sendForgotPasswordEmail();
        } else {
            gotoLogin();
        }
    }

    @OnClick(R.id.btn_forgot_password)
    void onForgotPasswordClick() {
        if (loginEmail == null) {
            sendForgotPasswordEmail();
        } else {
            gotoForgotPassword();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fingerprintSubscription.unsubscribe();
    }

    private void navigateToHome() {
        ((HomeActivity) getActivity()).getViewPager().setCurrentItem(0);
    }

    private void authenticateFingerprint() {
        setStatusText();
        if (RxFingerprint.isUnavailable(getActivity())) {
            mStage = Stage.LOGIN;
            updateStage();
            return;
        }

        fingerprintSubscription = RxFingerprint.authenticate(getActivity())
                .subscribe(new Subscriber<FingerprintAuthenticationResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR", "authenticate", e);
                    }

                    @Override
                    public void onNext(FingerprintAuthenticationResult fingerprintAuthenticationResult) {
                        switch (fingerprintAuthenticationResult.getResult()) {
                            case FAILED:
                                Toast.makeText(getActivity(), "Fingerprint not recognized, try again!",
                                        Toast.LENGTH_SHORT).show();
                                mFingerprintIcon
                                        .setImageDrawable(
                                                ContextCompat.getDrawable(getActivity(), R.drawable.ic_warning));
                                mFingerprintStatus.setText(getString(R.string.label_fingerprint_error));
                                break;
                            case HELP:
                                Toast.makeText(getActivity(), fingerprintAuthenticationResult.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case AUTHENTICATED:
                                Toast.makeText(getActivity(), "Successfully authenticated!", Toast.LENGTH_SHORT).show();
                                mFingerprintIcon
                                        .setImageDrawable(ContextCompat
                                                .getDrawable(getActivity(), R.drawable.ic_fingerprint_success));
                                mFingerprintStatus.setText(getString(R.string.label_fingerprint_recognized));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismiss();
                                    }
                                }, 1000);
                                break;
                        }
                    }
                });
    }

    private void setStatusText() {
        if (!RxFingerprint.isAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Fingerprint not available", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(), "Touch the sensor!", Toast.LENGTH_SHORT).show();
    }

    private void updateStage() {
        switch (mStage) {
            case FINGERPRINT:
                mDialogTitle.setText(R.string.title_fingerprint_login);
                mFingerprintStatus.setText(R.string.hint_fingerprint);
                mFingerprintIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fingerprint));
                mCancelButton.setText(R.string.label_fingerprint_cancel);
                mSecondDialogButton.setText(R.string.label_use_password);
                mFingerprintContent.setVisibility(View.VISIBLE);
                mLoginContent.setVisibility(View.GONE);
                mForgotPasswordContent.setVisibility(View.GONE);
                break;

            case LOGIN:
                loginEmail = mPreferencesHelper.getLoginEmail();
                if (loginEmail == null) {
                    mDialogTitle.setText(R.string.title_enter_account_info);
                    mEmailTextLogin.setVisibility(View.GONE);
                    mEmailInput.setVisibility(View.VISIBLE);
                } else {
                    mDialogTitle.setText(R.string.title_use_password_to_signin);
                    mEmailInput.setVisibility(View.GONE);
                    mEmailTextLogin.setVisibility(View.VISIBLE);
                    mEmailTextLogin.setText(loginEmail);
                }
                mCancelButton.setText(R.string.label_fingerprint_cancel);
                mSecondDialogButton.setText(R.string.label_fingerprint_continue);
                mLoginContent.setVisibility(View.VISIBLE);
                mFingerprintContent.setVisibility(View.GONE);
                mForgotPasswordContent.setVisibility(View.GONE);
                break;

            case FORGOT_PASSWORD:
                mDialogTitle.setText(R.string.label_fingerprint_forgot_password);
                loginEmail = mPreferencesHelper.getLoginEmail();
                if (loginEmail == null) {
                    mEmailTextForgotPassword.setText(mEmailEdit.getText().toString().trim());
                } else {
                    mEmailTextForgotPassword.setText(loginEmail);
                }
                mCancelButton.setText(R.string.label_fingerprint_cancel);
                mSecondDialogButton.setText(R.string.label_send_email);
                mForgotPasswordContent.setVisibility(View.VISIBLE);
                mFingerprintContent.setVisibility(View.GONE);
                mLoginContent.setVisibility(View.GONE);
                break;
        }
    }

    private void gotoLogin() {
        mStage = Stage.LOGIN;
        updateStage();
    }

    private void gotoForgotPassword() {
        mStage = Stage.FORGOT_PASSWORD;
        updateStage();
    }

    void sendForgotPasswordEmail() {
        boolean isValidEmail;
        String emailText = mEmailEdit.getText().toString().trim();
        if (loginEmail != null) {
            emailText = loginEmail;
        }
        if (emailText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            return;
        } else {
            isValidEmail = validateEmail(emailText);
            if (isValidEmail) {
                loginEmail = emailText;
            }
        }

        if (mStage == Stage.FORGOT_PASSWORD && isValidEmail) {
            showProgress();
            mApiService.getSessionConfirmationNumber()
                    .subscribeOn(Schedulers.io())
                    .onErrorReturn(new Func1<Throwable, SessionConfNumberResponse>() {
                        @Override
                        public SessionConfNumberResponse call(Throwable throwable) {
                            return mPreferencesHelper.getSessionConfirmationResponse();
                        }
                    })
                    .flatMap(new Func1<SessionConfNumberResponse, Observable<String>>() {
                        @Override
                        public Observable<String> call(SessionConfNumberResponse sessionConfNumberResponse) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            }

                            return mApiService
                                    .forgotPassword(new ForgotPasswordRequest(loginEmail, sessionConfNumber))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.v("onError", e.getMessage());
                            Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            hideProgress();
                        }

                        @Override
                        public void onNext(String s) {

                            Log.v("response", s);
                            Toast.makeText(getActivity(), "response" +
                                    s, Toast.LENGTH_SHORT).show();
                            hideProgress();
                            mTextEmailSent.setText(getString(R.string.label_email_sent));
                            mSecondDialogButton.setText(getString(R.string.label_enter_password));
                            mStage = Stage.FORGOT_PASSWORD;
                            updateStage();
                        }
                    });

        } else if (mStage == Stage.LOGIN && isValidEmail) {
            onForgotPasswordClick();
        } else {
            setEmailError(getString(R.string.accountSettingsEmailInvalidError));
        }
    }

    private void doLogin() {

        boolean isValidEmail, isValidPassword;
        String emailText = mEmailEdit.getText().toString().trim();
        String passwordText = mPasswordEdit.getText().toString().trim();

        if (loginEmail != null) {
            emailText = loginEmail;
        }
        if (emailText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            return;
        } else {
            isValidEmail = validateEmail(emailText);
            if (isValidEmail) {
                loginEmail = emailText;
            }
        }

        if (passwordText.isEmpty()) {
            setPasswordError(getString(R.string.accountSettingsPasswordEmptyError));
            return;
        } else {
            isValidPassword = validatePassword(passwordText);
        }

        if (isValidEmail && isValidPassword) {
            showProgress();
            mApiService.getSessionConfirmationNumber()
                    .subscribeOn(Schedulers.io())
                    .onErrorReturn(new Func1<Throwable, SessionConfNumberResponse>() {
                        @Override
                        public SessionConfNumberResponse call(Throwable throwable) {
                            return mPreferencesHelper.getSessionConfirmationResponse();
                        }
                    })
                    .flatMap(new Func1<SessionConfNumberResponse, Observable<LoginResponse>>() {
                        @Override
                        public Observable<LoginResponse> call(SessionConfNumberResponse sessionConfNumberResponse) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            }

                            return mApiService
                                    .login(new LoginRequest(loginEmail, mPasswordEdit.getText().toString(),
                                            sessionConfNumber))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        }
                    })
                    .subscribe(new Subscriber<LoginResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.v("onError", e.getMessage());
                            Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            hideProgress();
                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {

                            Log.v("login response", loginResponse.getStatus().getCode());
                            hideProgress();
                            if (loginResponse.getStatus().getCode().equals("SUCCESS")) {
                                mPreferencesHelper.setIsNewUser(false);
                                dismiss();
                            } else {
                                Toast.makeText(getActivity(),
                                        "Error: " + loginResponse.getStatus().getErrorMessages().get(0),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else if (!isValidEmail) {
            setEmailError(getString(R.string.accountSettingsEmailInvalidError));
        } else {
            setPasswordError(getString(R.string.accountSettingsPasswordInvalidError));
        }
    }

    private void setEmailError(String errorString) {
        mEmailInput.setError(errorString);
    }

    private void setPasswordError(String errorString) {
        mPasswordInput.setError(errorString);
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validatePassword(String password) {
        return password.length() >= PASSWORD_LENGTH;
    }

    /**
     * Enumeration to indicate which authentication method the user is trying to authenticate with.
     */
    public enum Stage {
        FINGERPRINT,
        LOGIN,
        FORGOT_PASSWORD
    }
}
