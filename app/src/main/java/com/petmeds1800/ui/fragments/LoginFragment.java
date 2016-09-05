package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
public class LoginFragment extends AbstractFragment implements LoginContract.View {

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
    public void navigateToHome() {
        startActivity(new HomeIntent(getActivity()));
        getActivity().finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @OnClick(R.id.log_in_button)
    public void login() {

        boolean isValidEmail, isValidPassword;
        String emailText = mEmailEdit.getText().toString().trim();
        String passwordText = mPasswordEdit.getText().toString().trim();
        if (emailText.isEmpty() && passwordText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            setPasswordError(getString(R.string.accountSettingsPasswordEmptyError));
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
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<LoginResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            doLogin();
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
                                .login(new LoginRequest(mEmailEdit.getText().toString(),
                                        mPasswordEdit.getText().toString(), sessionConfNumber))

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
                        Toast.makeText(getActivity(), "Error logging in..", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {

                        Log.v("login response", loginResponse.getStatus().getCode());
                        hideProgress();
                        if (loginResponse.getStatus().getCode().equals("SUCCESS")) {
                            mPreferencesHelper.setIsNewUser(false);
                            navigateToHome();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Error: " + loginResponse.getStatus().getErrorMessages().get(0),
                                    Toast.LENGTH_SHORT).show();
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

}
