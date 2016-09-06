package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.ForgotPasswordRequest;
import com.petmeds1800.model.entities.ForgotPasswordResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.mvp.ForgotPasswordTask.ForgotPasswordContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Digvijay on 8/22/2016.
 */
public class ForgotPasswordFragment extends AbstractFragment implements ForgotPasswordContract.View, TextWatcher {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.email_input)
    TextInputLayout mEmailInput;

    @BindView(R.id.email_edit)
    EditText mEmailText;

    @BindView(R.id.email_my_password_button)
    Button mEmailPasswordButton;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @BindView(R.id.errorText)
    TextView errorText;
    @BindView(R.id.errorTextOnEmail)
    TextView errorTextOnEmail;

    private ForgotPasswordContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, view);
        mEmailText.addTextChangedListener(this);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (errorText.getVisibility() != View.INVISIBLE)
            errorText.setVisibility(View.INVISIBLE);
        if (errorTextOnEmail.getVisibility() != View.INVISIBLE) {
            errorTextOnEmail.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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
        errorTextOnEmail.setVisibility(View.VISIBLE);
        errorTextOnEmail.setText(errorString);
        errorText.setVisibility(View.VISIBLE);


    }

    @Override
    public void setPresenter(ForgotPasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.email_my_password_button)
    public void emailMyPassword() {

        boolean isValidEmail;
        String emailText = mEmailText.getText().toString().trim();

        if (emailText.isEmpty()) {
            setEmailError(getString(R.string.accountSettingsEmailEmptyError));
            return;
        } else {
            isValidEmail = mPresenter.validateEmail(emailText);
        }

        if (isValidEmail) {

            showProgress();
            mApiService.getSessionConfirmationNumber()
                    .subscribeOn(Schedulers.io())
                    .onErrorReturn(new Func1<Throwable, SessionConfNumberResponse>() {
                        @Override
                        public SessionConfNumberResponse call(Throwable throwable) {
                            return mPreferencesHelper.getSessionConfirmationResponse();
                        }
                    })
                    .flatMap(new Func1<SessionConfNumberResponse, Observable<ForgotPasswordResponse>>() {
                        @Override
                        public Observable<ForgotPasswordResponse> call(SessionConfNumberResponse sessionConfNumberResponse) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            }

                            return mApiService
                                    .forgotPassword(new ForgotPasswordRequest("api-demo@gmail.com", sessionConfNumber))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        }
                    })
                    .subscribe(new Subscriber<ForgotPasswordResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.v("onError", e.getMessage());
                            hideProgress();
                        }

                        @Override
                        public void onNext(ForgotPasswordResponse response) {

                            Log.v("response", response.getStatus().getErrorMessages().get(0));
                            Toast.makeText(getActivity(), "response" + response.getStatus().getErrorMessages().get(0), Toast.LENGTH_SHORT).show();
                            hideProgress();
                            mEmailPasswordButton.setText(getString(R.string.label_email_sent));
                        }
                    });

        } else {
            setEmailError(getString(R.string.accountSettingsEmailInvalidError));
        }
    }
}
