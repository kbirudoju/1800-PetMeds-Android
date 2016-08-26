package com.petmeds1800.ui.address;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddAddressFragment extends AbstractFragment implements AddAddressContract.View {


    @BindView(R.id.firstNameLayout)
    TextInputLayout mfirstNameLayout;
    @BindView(R.id.lastNameInputLayout)
    TextInputLayout mLastNameInputLayout;
    @BindView(R.id.addressLine1InputLayout)
    TextInputLayout mAddressLine1InputLayout;
    @BindView(R.id.aptOrSuiteInputLayout)
    TextInputLayout mAptOrSuiteInputLayout;
    @BindView(R.id.cityInputLayout)
    TextInputLayout mCityInputLayout;
    @BindView(R.id.stateOrProvinceOrRegionInputLayout)
    TextInputLayout mStateOrProvinceOrRegionInputLayout;
    @BindView(R.id.zipCodeInputLayout)
    TextInputLayout mZipCodeInputLayout;
    @BindView(R.id.phoneNumberInputLayout)
    TextInputLayout mPhoneNumberInputLayout;
    @BindView(R.id.countryNameInputLayout)
    TextInputLayout mCountryNameInputLayout;


    @BindView(R.id.firstName_edit)
    EditText mFirstNameEdit;
    @BindView(R.id.lastName_edit)
    EditText mLastNameEdit;
    @BindView(R.id.addressLine1_edit)
    EditText mAddressLine1Edit;
    @BindView(R.id.aptOrSuit_edit)
    EditText mAptOrSuiteEdit;
    @BindView(R.id.city_edit)
    EditText mCityEdit;
    @BindView(R.id.stateOrProvinceOrRegion_edit)
    EditText mStateOrProvinceOrRegionEdit;
    @BindView(R.id.zipCode_edit)
    EditText mZipCodeEdit;
    @BindView(R.id.phoneNumber_edit)
    EditText mPhoneNumberEdit;
    @BindView(R.id.countryName_edit)
    EditText mCountryNameEdit;

    @BindView(R.id.defaultBillingAddress_switch)
    Switch mDefaultBillingAddressSwitch;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private AddAddressContract.Presenter mPresenter;
    private AlertDialog mAlertDialog;

    private static final int DISMISS_APPROVAL_DIALOG = 1;
    private static final long APPROVAL_DIALOG_DURATION = 1000;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddAddressPresenter(this);
        setHasOptionsMenu(true);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {

            boolean invalidFirstName;
            boolean invalidLastName;
            boolean invalidAddressLine1;
            boolean invalidAptOrSuiteNumber;
            boolean invalidCity;
            boolean invalidStateOrProvinceOrRegion;
            boolean invalidZipcode;
            boolean invalidPhoneNumber;
            boolean invalidCountry;

            //return in-case of any error
            invalidFirstName = checkAndShowError(mFirstNameEdit , mfirstNameLayout, R.string.errorFirstNameRequired);
            invalidLastName = checkAndShowError(mLastNameEdit , mLastNameInputLayout , R.string.errorLastNameIsRequired);
            invalidAddressLine1 = checkAndShowError(mAddressLine1Edit , mAddressLine1InputLayout , R.string.errorAddressLine1Required);
            invalidAptOrSuiteNumber = checkAndShowError(mAptOrSuiteEdit , mAptOrSuiteInputLayout , R.string.errorAptOrSuiteRequired);
            invalidCity = checkAndShowError(mCityEdit , mCityInputLayout , R.string.errorCityRequired);
            invalidStateOrProvinceOrRegion = checkAndShowError(mStateOrProvinceOrRegionEdit , mStateOrProvinceOrRegionInputLayout , R.string.errorStateOrProvinceOrRegionRequired);
            invalidZipcode = checkAndShowError(mZipCodeEdit , mZipCodeInputLayout , R.string.errorZipCodeRequired);
            invalidPhoneNumber = checkAndShowError(mPhoneNumberEdit , mPhoneNumberInputLayout , R.string.errorPhoneNumberRequired);
            invalidCountry = checkAndShowError(mCountryNameEdit , mCountryNameInputLayout , R.string.errorCountryNameRequired);

            if(invalidFirstName ||
                    invalidLastName ||
                    invalidAddressLine1 ||
                    invalidAptOrSuiteNumber ||
                    invalidStateOrProvinceOrRegion ||
                    invalidCity ||
                    invalidAptOrSuiteNumber ||
                    invalidZipcode ||
                    invalidPhoneNumber ||
                    invalidCountry)
                return false;
            }

            AddressRequest addressRequest = new AddressRequest(
                     String.valueOf(mDefaultBillingAddressSwitch.isChecked())
                    ,mLastNameEdit.getText().toString()
                    ,mStateOrProvinceOrRegionEdit.getText().toString()
                    ,mAddressLine1Edit.getText().toString()
                    ,mAddressLine1Edit.getText().toString()  //TODO we need to check if AddressLine2 should be included in the designs as its available in the API response
                    ,mCountryNameEdit.getText().toString()
                    ,mCityEdit.getText().toString()
                    ,mZipCodeEdit.getText().toString()
                    ,mPhoneNumberEdit.getText().toString()
                    ,String.valueOf(mDefaultBillingAddressSwitch.isChecked())
                    ,mFirstNameEdit.getText().toString()
                    ,mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

            mPresenter.saveAddress(addressRequest);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkAndShowError(EditText auditEditText , TextInputLayout auditTextInputLayout , int errorStringId) {
        if (auditEditText.getText().toString().isEmpty()) {
            auditTextInputLayout.setError(getContext().getString(errorStringId));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }


    }
    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void addressAdded() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.cardSavedInAccount).setCancelable(false);
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        mHandler.sendEmptyMessageDelayed(DISMISS_APPROVAL_DIALOG,APPROVAL_DIALOG_DURATION);
    }

    @Override
    public void addressAdditionFailed(String errorMessage) {
        Snackbar.make(mAddressLine1Edit, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(AddAddressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == DISMISS_APPROVAL_DIALOG){
                mAlertDialog.dismiss();
                popBackStack();
            }
            return false;
        }
    });


}
