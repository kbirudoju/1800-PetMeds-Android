package com.petmeds1800.ui.fragments;

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

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.intent.ForgotPasswordIntent;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.intent.SignUpIntent;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.mvp.LoginTask.LoginContract;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.GetSessionCookiesHack;
import com.petmeds1800.util.Utils;

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

    private static final String IS_FROM_HOME_ACTIVITY = "isFromHomeActivity";

    private LoginContract.Presenter mPresenter;

    private GetSessionCookiesHack mGetSessionCookiesHack;

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
        mPasswordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        mPasswordEdit.setOnEditorActionListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Temp Hack for login
        mGetSessionCookiesHack = new GetSessionCookiesHack() {
            @Override
            public void getSessionCookiesOnFinish(boolean doLogin, Throwable e) {
                int errorId = RetrofitErrorHandler.getErrorMessage(e);
                if (errorId == R.string.noInternetConnection) {
                    showErrorCrouton(getString(errorId), false);
                    hideProgress();
                } else {
                    if(doLogin){
                        doLogin();
                    }
                    else {
                        initializeSessionConfirmationNumber();
                    }

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
        } else {
            Utils.displayCrouton(getActivity(), (String) message, mContainerLayout);
        }
    }

    @Override
    public void navigateToHome() {
        HomeIntent intent = new HomeIntent(getActivity());
        intent.putExtra(IS_FROM_HOME_ACTIVITY, true);
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

        String emailText = mEmailEdit.getText().toString().trim();
        if (emailText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            return;
        } else if (!mPresenter.validateEmail(emailText)) {
            setEmailError(getString(R.string.accountSettingsEmailInvalidError));
            return;
        }

        String passwordText = mPasswordEdit.getText().toString().trim();
        if (passwordText.isEmpty()) {
            setPasswordError(getString(R.string.accountSettingsPasswordEmptyError));
            return;
        } else if (!mPresenter.validatePassword(passwordText)) {
            setPasswordError(getString(R.string.accountSettingsPasswordInvalidError));
            return;
        }

        mGetSessionCookiesHack.doHackForGettingSessionCookies(true, mApiService);
    }

    private void doLogin() {

        final String emailText = mEmailEdit.getText().toString().trim();
        final String passwordText = mPasswordEdit.getText().toString().trim();

        showProgress();
        mApiService.getSessionConfirmationNumber()
                .observeOn(AndroidSchedulers.mainThread())
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
                                    .login(new LoginRequest(emailText, passwordText, sessionConfNumber))
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
                                mPreferencesHelper.setIsUserLoggedIn(true);
                                mPreferencesHelper.setLoginEmail(loginResponse.getProfile().getEmail());
                                mPreferencesHelper.setLoginPassword(passwordText);
                                navigateToHome();
                            } else {
                                showErrorCrouton(Html.fromHtml(loginResponse.getStatus().getErrorMessages().get(0)), true);
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.label_skip)
    public void skipLoginSignUp() {
        SessionConfNumberResponse sessionConfNumberResponse = mPreferencesHelper.getSessionConfirmationResponse();
        if (sessionConfNumberResponse != null && sessionConfNumberResponse.getSessionConfirmationNumber() != null) {
            navigateToHome();
        } else {
            showProgress();
            mGetSessionCookiesHack.doHackForGettingSessionCookies(false, mApiService);
        }

    }

    private void initializeSessionConfirmationNumber() {
        mApiService.getSessionConfirmationNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SessionConfNumberResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        hideProgress();
                        showErrorCrouton(e.getLocalizedMessage(), false);
                    }

                    @Override
                    public void onNext(SessionConfNumberResponse sessionConfNumberResponse) {
                        hideProgress();
                        if (sessionConfNumberResponse != null) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                                navigateToHome();
                            } else {
                                //TODO Have to change the message
                                showErrorCrouton("Session number not generated", false);
                            }
                        } else {
                            //TODO Have to change the message
                            showErrorCrouton("Session response not generated", false);
                        }

                    }
                });
    }


    @OnClick(R.id.label_forgot_password)
    public void forgotPassword() {
        startActivity(new ForgotPasswordIntent(getActivity()));
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
        startActivity(new SignUpIntent(getActivity()));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            login();
        }
        return false;
    }
}
