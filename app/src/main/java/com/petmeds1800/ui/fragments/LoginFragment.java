package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.intent.ForgotPasswordIntent;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.mvp.LoginTask.LoginContract;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Digvijay on 8/3/2016.
 */
public class LoginFragment extends AbstractFragment implements LoginContract.View, EditText.OnEditorActionListener {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.email_input)
    TextInputLayout mEmailInput;

    @BindView(R.id.email_edit)
    EditText mEmailEdit;

    @BindView(R.id.password_input)
    TextInputLayout mPasswordInput;

    @BindView(R.id.password_edit)
    EditText mPasswordEdit;

    @BindView(R.id.container_login)
    RelativeLayout mContainerLayout;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private LoginContract.Presenter mPresenter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPasswordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mPasswordEdit.setOnEditorActionListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setEmailError(String errorString) {
        mEmailInput.setError(errorString);
    }

    @Override
    public void setPasswordError(String errorString) {
        mPasswordInput.setError(errorString);
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        if (span) {
            Utils.displayCrouton(getActivity(), (Spanned) message, mContainerLayout);
        }
        Utils.displayCrouton(getActivity(), (String) message, mContainerLayout);
    }

    @Override
    public void navigateToHome() {

        HomeIntent intent = new HomeIntent(getActivity());
        intent.putExtra("isFromHomeActivity", true);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @OnClick(R.id.log_in_button)
    public void login() {
        mEmailInput.setError(null);
        mPasswordInput.setError(null);
        boolean isValidEmail, isValidPassword;
        String emailText = mEmailEdit.getText().toString().trim();
        String passwordText = mPasswordEdit.getText().toString().trim();
        if (emailText.isEmpty() && passwordText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            setPasswordError(getString(R.string.accountSettingsPasswordEmptyError));
            return;
        }
        if (emailText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            return;
        } else {
            isValidEmail = mPresenter.validateEmail(emailText);
        }

        if (passwordText.isEmpty()) {
            setPasswordError(getString(R.string.accountSettingsPasswordEmptyError));
            return;
        } else {
            isValidPassword = mPresenter.validatePassword(passwordText);
        }

        if (isValidEmail && isValidPassword) {

            showProgress();
            //TODO: remove this temporary hack after backend resolves their problem of cookies
            mApiService.login(new LoginRequest(mEmailEdit.getText().toString(),
                    mPasswordEdit.getText().toString(), "test_test"))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<LoginResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            int errorId = RetrofitErrorHandler.getErrorMessage(e);
                            if (errorId == R.string.noInternetConnection) {
                                showErrorCrouton(getString(errorId), false);
                                hideProgress();
                            } else {
                                doLogin();
                            }
                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            Log.v("login response", loginResponse.getStatus().getCode());
                        }
                    });

        } else if (!isValidEmail) {
            setEmailError(getString(R.string.accountSettingsEmailInvalidError));
        } else {
            setPasswordError(getString(R.string.accountSettingsPasswordInvalidError));
        }
    }

    private void doLogin() {
        mApiService.getSessionConfirmationNumber()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, SessionConfNumberResponse>() {
                    @Override
                    public SessionConfNumberResponse call(Throwable throwable) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(throwable);
                        if (errorId == R.string.noInternetConnection) {
                            hideProgress();
                            showErrorCrouton(getString(errorId), false);
                        } else {
                            return mPreferencesHelper.getSessionConfirmationResponse();
                        }
                        return null;
                    }
                })
                .flatMap(new Func1<SessionConfNumberResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(SessionConfNumberResponse sessionConfNumberResponse) {
                        if (sessionConfNumberResponse != null) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            }
                            return mApiService
                                    .login(new LoginRequest(mEmailEdit.getText().toString(),
                                            mPasswordEdit.getText().toString(), sessionConfNumber))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        } else {
                            return null;
                        }
                    }
                })
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (errorId == R.string.noInternetConnection) {
                            showErrorCrouton(getString(errorId), false);
                        }
                        Log.v("onError", e.getMessage());
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        hideProgress();
                        if (loginResponse != null) {
                            Log.v("login response", loginResponse.getStatus().getCode());
                            if (loginResponse.getStatus().getCode().equals("SUCCESS")) {
                                mPreferencesHelper.setIsUserLoggedIn(false);
                                navigateToHome();
                            } else {
                                showErrorCrouton(Html.fromHtml(loginResponse.getStatus().getErrorMessages().get(0)),
                                        true);
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.label_skip)
    public void skipLoginSignUp() {
        navigateToHome();
    }

    @OnClick(R.id.label_forgot_password)
    public void forgotPassword() {
        startActivity(new ForgotPasswordIntent(getActivity()));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            login();
        }
        return false;
    }
}
