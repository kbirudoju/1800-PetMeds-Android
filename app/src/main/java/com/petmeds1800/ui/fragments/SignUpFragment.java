package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.entities.SignUpRequest;
import com.petmeds1800.model.entities.SignUpResponse;
import com.petmeds1800.mvp.SignupTask.SignUpContract;
import com.petmeds1800.ui.fragments.dialog.CommonDialogFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpFragment extends AbstractFragment
        implements SignUpContract.View, View.OnClickListener, CommonDialogFragment.ValueSelectedListener {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.container_sign_up)
    FrameLayout mContainerLayout;

    @BindView(R.id.email_input)
    TextInputLayout mEmailInput;

    @BindView(R.id.email_edit)
    EditText mEmailEdit;

    @BindView(R.id.confirm_email_input)
    TextInputLayout mConfirmEmailInput;

    @BindView(R.id.confirm_email_edit)
    EditText mConfirmEmailEdit;

    @BindView(R.id.password_input)
    TextInputLayout mPasswordInput;

    @BindView(R.id.password_edit)
    EditText mPasswordEdit;

    @BindView(R.id.confirm_password_input)
    TextInputLayout mConfirmPasswordInput;

    @BindView(R.id.confirm_password_edit)
    EditText mConfirmPasswordEdit;

    @BindView(R.id.first_name_input)
    TextInputLayout mFirstNameInput;

    @BindView(R.id.first_name_edit)
    EditText mFirstNameEdit;

    @BindView(R.id.last_name_input)
    TextInputLayout mLastNameInput;

    @BindView(R.id.last_name_edit)
    EditText mLastNameEdit;

    @BindView(R.id.address_input)
    TextInputLayout mAddressLine1Input;

    @BindView(R.id.address_edit)
    EditText mAddressLine1Edit;

    @BindView(R.id.apartment_input)
    TextInputLayout mApartmentInput;

    @BindView(R.id.apartment_edit)
    EditText mApartmentEdit;

    @BindView(R.id.city_input)
    TextInputLayout mCityInput;

    @BindView(R.id.city_edit)
    EditText mCityEdit;

    @BindView(R.id.state_input)
    TextInputLayout mStateInput;

    @BindView(R.id.state_edit)
    EditText mStateEdit;

    @BindView(R.id.zip_input)
    TextInputLayout mZipInput;

    @BindView(R.id.zip_edit)
    EditText mZipEdit;

    @BindView(R.id.phone_input)
    TextInputLayout mPhoneInput;

    @BindView(R.id.phone_edit)
    EditText mPhoneEdit;

    @BindView(R.id.country_input)
    TextInputLayout mCountryInput;

    @BindView(R.id.country_edit)
    EditText mCountryEdit;

    @BindView(R.id.switch_same_addresses)
    SwitchCompat mSameAddressesSwitch;

    @BindView(R.id.container_shipping_linear)
    LinearLayout mShippingViewsContainer;

    @BindView(R.id.first_name_shipping_input)
    TextInputLayout mFirstNameShippingInput;

    @BindView(R.id.first_name_shipping_edit)
    EditText mFirstNameShippingEdit;

    @BindView(R.id.last_name_shipping_input)
    TextInputLayout mLastNameShippingInput;

    @BindView(R.id.last_name_shipping_edit)
    EditText mLastNameShippingEdit;

    @BindView(R.id.address_shipping_input)
    TextInputLayout mAddressLine1ShippingInput;

    @BindView(R.id.address_shipping_edit)
    EditText mAddressLine1ShippingEdit;

    @BindView(R.id.apartment_shipping_input)
    TextInputLayout mApartmentShippingInput;

    @BindView(R.id.apartment_shipping_edit)
    EditText mApartmentShippingEdit;

    @BindView(R.id.city_shipping_input)
    TextInputLayout mCityShippingInput;

    @BindView(R.id.city_shipping_edit)
    EditText mCityShippingEdit;

    @BindView(R.id.state_shipping_input)
    TextInputLayout mStateShippingInput;

    @BindView(R.id.state_shipping_edit)
    EditText mStateShippingEdit;

    @BindView(R.id.zip_shipping_input)
    TextInputLayout mZipShippingInput;

    @BindView(R.id.zip_shipping_edit)
    EditText mZipShippingEdit;

    @BindView(R.id.phone_shipping_input)
    TextInputLayout mPhoneShippingInput;

    @BindView(R.id.phone_shipping_edit)
    EditText mPhoneShippingEdit;

    @BindView(R.id.country_shipping_input)
    TextInputLayout mCountryShippingInput;

    @BindView(R.id.country_shipping_edit)
    EditText mCountryShippingEdit;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private SignUpContract.Presenter mPresenter;

    private String emailText, confirmEmailText, passwordText, confirmPasswordText, firstNameText, lastNameText,
            addressText, apartmentText, cityText, stateText, zipText, phoneText, countryText, firstNameShippingText,
            lastNameShippingText, addressShippingText, apartmentShippingText, cityShippingText, zipShippingText,
            stateShippingText, phoneShippingText, countryShippingText;

    private static final int USA_STATE_LIST_REQUEST = 1;

    private static final int COUNTRY_LIST_REQUEST = 2;

    private static EditText mCurrentListDialogInput;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        showHideShipping();
        mStateEdit.setOnClickListener(this);
        mStateEdit.setFocusableInTouchMode(false);
        mCountryEdit.setOnClickListener(this);
        mCountryEdit.setFocusableInTouchMode(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sign_up, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            signUp();
        } else if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
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
    public void setErrorOnView(String errorString, int viewId) {
        TextInputLayout view = (TextInputLayout) getActivity().findViewById(viewId);
        view.setError(errorString);
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
//        intent.putExtra(IS_FROM_HOME_ACTIVITY, true);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onStatesListReceived(String[] statesArray) {
        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment statesDialogFragment = CommonDialogFragment
                .newInstance(statesArray, getActivity().getString(R.string.choose_city_txt), USA_STATE_LIST_REQUEST);
        statesDialogFragment.setValueSetListener(this);
        statesDialogFragment.show(fragManager);
    }

    @Override
    public void onCountryListReceived(String[] countryArray) {
        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment statesDialogFragment = CommonDialogFragment
                .newInstance(countryArray, getActivity().getString(R.string.choose_country_txt), COUNTRY_LIST_REQUEST);
        statesDialogFragment.setValueSetListener(this);
        statesDialogFragment.show(fragManager);
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.switch_same_addresses)
    public void showHideShipping() {
        if (mSameAddressesSwitch.isChecked()) {
            mShippingViewsContainer.setVisibility(View.GONE);
        } else {
            mShippingViewsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void signUp() {

        clearErrorsOnInputs();

        emailText = mEmailEdit.getText().toString().trim();
        if (emailText.isEmpty()) {
            setErrorOnView(getString(R.string.accountSettingsEmailEmptyError), mEmailInput.getId());
            return;
        } else if (!mPresenter.validateEmail(emailText)) {
            setErrorOnView(getString(R.string.accountSettingsEmailInvalidError), mEmailInput.getId());
            return;
        }

        confirmEmailText = mConfirmEmailEdit.getText().toString().trim();
        if (confirmEmailText.isEmpty()) {
            setErrorOnView(getString(R.string.accountSettingsEmailEmptyError), mConfirmEmailInput.getId());
            return;
        } else if (!emailText.equals(confirmEmailText)) {
            setErrorOnView(getString(R.string.error_confirm_email_must_match), mConfirmEmailInput.getId());
            return;
        }

        passwordText = mPasswordEdit.getText().toString().trim();
        if (passwordText.isEmpty()) {
            setErrorOnView(getString(R.string.accountSettingsPasswordEmptyError), mPasswordInput.getId());
            return;
        } else if (!mPresenter.validatePassword(passwordText)) {
            setErrorOnView(getString(R.string.accountSettingsPasswordInvalidError), mPasswordInput.getId());
            return;
        }

        confirmPasswordText = mConfirmPasswordEdit.getText().toString().trim();
        if (confirmPasswordText.isEmpty()) {
            setErrorOnView(getString(R.string.accountSettingsPasswordEmptyError), mConfirmPasswordInput.getId());
            return;
        } else if (!passwordText.equals(confirmPasswordText)) {
            setErrorOnView(getString(R.string.error_confirm_password_must_match), mConfirmPasswordInput.getId());
            return;
        }

        firstNameText = mFirstNameEdit.getText().toString().trim();
        if (firstNameText.isEmpty()) {
            setErrorOnView(getString(R.string.error_first_name_required), mFirstNameInput.getId());
            return;
        } else if (!mPresenter.validateUserName(firstNameText)) {
            setErrorOnView(getString(R.string.error_first_name_invalid), mFirstNameInput.getId());
            return;
        }

        lastNameText = mLastNameEdit.getText().toString().trim();
        if (lastNameText.isEmpty()) {
            setErrorOnView(getString(R.string.error_last_name_required), mLastNameInput.getId());
            return;
        } else if (!mPresenter.validateUserName(lastNameText)) {
            setErrorOnView(getString(R.string.error_last_name_invalid), mLastNameInput.getId());
            return;
        }

        addressText = mAddressLine1Edit.getText().toString().trim();
        if (addressText.isEmpty()) {
            setErrorOnView(getString(R.string.error_address_required), mAddressLine1Input.getId());
            return;
        } else if (!mPresenter.validateAddress(addressText)) {
            setErrorOnView(getString(R.string.error_address_invalid), mAddressLine1Input.getId());
            return;
        }

        apartmentText = mApartmentEdit.getText().toString().trim();
        if (apartmentText.isEmpty()) {
            setErrorOnView(getString(R.string.error_apartment_required), mApartmentInput.getId());
            return;
        } else if (!mPresenter.validateAddress(apartmentText)) {
            setErrorOnView(getString(R.string.error_apartment_invalid), mApartmentInput.getId());
            return;
        }

        cityText = mCityEdit.getText().toString().trim();
        if (cityText.isEmpty()) {
            setErrorOnView(getString(R.string.error_city_required), mCityInput.getId());
            return;
        } else if (!mPresenter.validateCity(cityText)) {
            setErrorOnView(getString(R.string.error_city_invalid), mCityInput.getId());
            return;
        }

        stateText = mStateEdit.getText().toString().trim();
        if (stateText.isEmpty()) {
            setErrorOnView(getString(R.string.error_state_required), mStateInput.getId());
            return;
        }

        zipText = mZipEdit.getText().toString().trim();
        if (zipText.isEmpty()) {
            setErrorOnView(getString(R.string.error_zip_required), mZipInput.getId());
            return;
        } else if (!mPresenter.validatePostalCode(zipText)) {
            setErrorOnView(getString(R.string.error_zip_invalid), mZipInput.getId());
            return;
        }

        phoneText = mPhoneEdit.getText().toString().trim();
        if (phoneText.isEmpty()) {
            setErrorOnView(getString(R.string.error_phone_required), mPhoneInput.getId());
            return;
        } else if (!mPresenter.validatePhoneNumber(phoneText)) {
            setErrorOnView(getString(R.string.error_phone_invalid), mPhoneInput.getId());
            return;
        }

        countryText = mCountryEdit.getText().toString().trim();
        if (countryText.isEmpty()) {
            setErrorOnView(getString(R.string.error_country_required), mCountryInput.getId());
            return;
        }

        if (mShippingViewsContainer.getVisibility() == View.VISIBLE) {

            firstNameShippingText = mFirstNameShippingEdit.getText().toString().trim();
            if (firstNameShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_first_name_required), mFirstNameShippingInput.getId());
                return;
            } else if (!mPresenter.validateUserName(firstNameShippingText)) {
                setErrorOnView(getString(R.string.error_first_name_invalid), mFirstNameShippingInput.getId());
                return;
            }

            lastNameShippingText = mLastNameShippingEdit.getText().toString().trim();
            if (lastNameShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_last_name_required), mLastNameShippingInput.getId());
                return;
            } else if (!mPresenter.validateUserName(lastNameShippingText)) {
                setErrorOnView(getString(R.string.error_last_name_invalid), mLastNameShippingInput.getId());
                return;
            }

            addressShippingText = mAddressLine1ShippingEdit.getText().toString().trim();
            if (addressShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_address_required), mAddressLine1ShippingInput.getId());
                return;
            } else if (!mPresenter.validateAddress(addressShippingText)) {
                setErrorOnView(getString(R.string.error_address_invalid), mAddressLine1ShippingInput.getId());
                return;
            }

            apartmentShippingText = mApartmentShippingEdit.getText().toString().trim();
            if (apartmentShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_apartment_required), mApartmentShippingInput.getId());
                return;
            } else if (!mPresenter.validateAddress(apartmentShippingText)) {
                setErrorOnView(getString(R.string.error_apartment_invalid), mApartmentShippingInput.getId());
                return;
            }

            cityShippingText = mCityShippingEdit.getText().toString().trim();
            if (cityShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_city_required), mCityShippingInput.getId());
                return;
            } else if (!mPresenter.validateCity(cityShippingText)) {
                setErrorOnView(getString(R.string.error_city_invalid), mCityShippingInput.getId());
                return;
            }

            stateShippingText = mStateShippingEdit.getText().toString().trim();
            if (stateShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_state_required), mStateShippingInput.getId());
                return;
            }

            zipShippingText = mZipShippingEdit.getText().toString().trim();
            if (zipShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_zip_required), mZipShippingInput.getId());
                return;
            } else if (!mPresenter.validatePostalCode(zipShippingText)) {
                setErrorOnView(getString(R.string.error_zip_invalid), mZipShippingInput.getId());
                return;
            }

            phoneShippingText = mPhoneShippingEdit.getText().toString().trim();
            if (phoneShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_phone_required), mPhoneShippingInput.getId());
                return;
            } else if (!mPresenter.validatePhoneNumber(phoneShippingText)) {
                setErrorOnView(getString(R.string.error_phone_invalid), mPhoneShippingInput.getId());
                return;
            }

            countryShippingText = mCountryEdit.getText().toString().trim();
            if (countryShippingText.isEmpty()) {
                setErrorOnView(getString(R.string.error_country_required), mCountryShippingInput.getId());
                return;
            }
        }

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
                            doSignUp();
                        }
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Log.v("login response", loginResponse.getStatus().getCode());
                    }
                });
    }

    private void doSignUp() {

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
                .flatMap(new Func1<SessionConfNumberResponse, Observable<SignUpResponse>>() {
                    @Override
                    public Observable<SignUpResponse> call(SessionConfNumberResponse sessionConfNumberResponse) {
                        if (sessionConfNumberResponse != null) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            }
                            if (mSameAddressesSwitch.isChecked()) {
                                firstNameShippingText = firstNameText;
                                lastNameShippingText = lastNameText;
                                addressShippingText = addressText;
                                apartmentShippingText = apartmentText;
                                cityShippingText = cityText;
                                stateShippingText = stateText;
                                zipShippingText = zipText;
                                countryShippingText = countryText;
                                phoneShippingText = phoneText;
                            }
                            return mApiService.signUp(new SignUpRequest(emailText, confirmEmailText, passwordText,
                                    confirmPasswordText, firstNameText, lastNameText, addressText, apartmentText,
                                    cityText, stateText, zipText, countryText, phoneText, firstNameShippingText,
                                    lastNameShippingText, addressShippingText, apartmentShippingText, cityShippingText,
                                    stateShippingText, zipShippingText, countryShippingText, phoneShippingText,
                                    sessionConfNumber))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        } else {
                            return null;
                        }
                    }
                })
                .subscribe(new Subscriber<SignUpResponse>() {
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
                    public void onNext(SignUpResponse signUpResponse) {
                        hideProgress();
                        if (signUpResponse != null) {
                            Log.v("login response", signUpResponse.getStatus().getCode());
                            if (signUpResponse.getStatus().getCode().equals("SUCCESS")) {
                                mPreferencesHelper.setIsUserLoggedIn(true);
                                navigateToHome();
                            } else {
                                showErrorCrouton(Html.fromHtml(signUpResponse.getStatus().getErrorMessages().get(0)),
                                        true);
                            }
                        }
                    }
                });
    }

    @Override
    public void onValueSelected(String value, int requestCode) {
        {
            switch (requestCode) {

                case USA_STATE_LIST_REQUEST:
                    //initialize the usaStateCode so that it can be passed onto the API
                    String stateCode = mPresenter.getStateCode(value);
                    if (stateCode != null) {
                        mCurrentListDialogInput.setText(value);
                    } else {
//                        showErrorMessage("Cant load States List.Bad Data");
                    }
                    break;

                case COUNTRY_LIST_REQUEST:
                    //initialize the coutryCode so that it can be passed onto the API
                    String countryCode = mPresenter.getCountryCode(value);
                    if (countryCode != null) {
                        mCurrentListDialogInput.setText(value);
                    } else {
//                        showErrorMessage("Cant get country code.Bad Data");
                    }
                    break;
            }

        }
    }

    private void clearErrorsOnInputs() {
        mEmailInput.setError(null);
        mConfirmEmailInput.setError(null);
        mPasswordInput.setError(null);
        mConfirmPasswordInput.setError(null);
        mFirstNameInput.setError(null);
        mLastNameInput.setError(null);
        mAddressLine1Input.setError(null);
        mApartmentInput.setError(null);
        mCityInput.setError(null);
        mStateInput.setError(null);
        mZipInput.setError(null);
        mPhoneInput.setError(null);
        mCountryInput.setError(null);
        if (mShippingViewsContainer.getVisibility() == View.VISIBLE) {
            mFirstNameShippingInput.setError(null);
            mLastNameShippingInput.setError(null);
            mAddressLine1ShippingInput.setError(null);
            mApartmentShippingInput.setError(null);
            mCityShippingInput.setError(null);
            mStateShippingInput.setError(null);
            mZipShippingInput.setError(null);
            mPhoneShippingInput.setError(null);
            mCountryShippingInput.setError(null);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.state_edit:
                mCurrentListDialogInput = mStateEdit;
                mPresenter.getStatesList();
                break;

            case R.id.country_edit:
                mCurrentListDialogInput = mCountryEdit;
                mPresenter.getCountriesList();
                break;

            case R.id.state_shipping_edit:
                mCurrentListDialogInput = mStateShippingEdit;
                mPresenter.getStatesList();
                break;

            case R.id.country_shipping_edit:
                mCurrentListDialogInput = mCountryShippingEdit;
                mPresenter.getCountriesList();
                break;

            default:
                break;
        }
    }
}
