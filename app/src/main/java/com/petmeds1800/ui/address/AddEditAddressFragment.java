package com.petmeds1800.ui.address;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.RemoveAddressRequest;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.steponerootfragment.GuestStepOneRootFragment;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootFragment;
import com.petmeds1800.ui.checkout.stepthreefragment.GuestStepThreeRootContract;
import com.petmeds1800.ui.checkout.stepthreefragment.GuestStepThreeRootFragment;
import com.petmeds1800.ui.checkout.stepthreefragment.StepThreeRootFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.CommonDialogFragment;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.petmeds1800.R.id.firstNameLayout;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddEditAddressFragment extends AbstractFragment
        implements AddEditAddressContract.View, View.OnClickListener, CommonDialogFragment.ValueSelectedListener,
        DialogInterface.OnClickListener, Switch.OnCheckedChangeListener,
        GuestStepThreeRootContract.AddAddressInteractionListener {

    public static final int ADD_ADDRESS_REQUEST = 3;

    public static final int EDIT_ADDRESS_REQUEST = 4;

    private static final int USA_STATE_LIST_REQUEST = 1;

    private static final int COUNTRY_LIST_REQUEST = 2;

    private static final String ADDRESS = "address";

    private static final String REQUEST_CODE = "requestCode";

    private static final int DISMISS_APPROVAL_DIALOG = 1;

    private static final long APPROVAL_DIALOG_DURATION = 1000;

    @BindView(firstNameLayout)
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

    @BindView(R.id.defaultShippingAddress_switch)
    Switch mDefaultShippingAddressSwitch;

    @BindView(R.id.removeAddress_button)
    Button mRemoveAddressButton;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.addEditAddress_container)
    LinearLayout mContainerLayout;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @BindView(R.id.useMyShippingAddress_switch)
    Switch mUseMyShippingAddressSwitch;

    @BindView(R.id.useMyShippingAddress_switchview)
    View mUseMyShippingAddressSwitchview;

    @BindView(R.id.defaultShippingAddress_switchview)
    View mDefaultShippingAddressSwitchview;




    private AddEditAddressContract.Presenter mPresenter;

    private AlertDialog mAlertDialog;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == DISMISS_APPROVAL_DIALOG) {
                mAlertDialog.dismiss();
                if (mRequestCode == StepOneRootFragment.REQUEST_CODE) {
                    getActivity().finish();
                } else {
                    popBackStack();
                }

            }
            return false;
        }
    });

    private String mUsaStateCode;

    private String mCountryCode;

    private Address mAddress;

    private int mRequestCode;

    private Bundle mBundle;

    private AddressRequest mAddressRequest;

    public static AddEditAddressFragment newInstance(Address updateAddress, int requestCode) {
        Bundle bundle = new Bundle();
        if (requestCode == EDIT_ADDRESS_REQUEST || requestCode == StepThreeRootFragment.LOGGED_IN_REQUEST_CODE
                || requestCode == StepOneRootFragment.REQUEST_CODE
                || requestCode == GuestStepOneRootFragment.REQUEST_CODE
                || requestCode == GuestStepThreeRootFragment.REQUEST_CODE) {
            bundle.putSerializable(ADDRESS, updateAddress);
            bundle.putInt(REQUEST_CODE, requestCode);
            AddEditAddressFragment addEditAddressFragment = new AddEditAddressFragment();
            addEditAddressFragment.setArguments(bundle);
            return addEditAddressFragment;
        }

        return new AddEditAddressFragment();
    }

    public void intitalizeViewsForCheckOutBillingAddress() {
        mUseMyShippingAddressSwitch.setVisibility(View.VISIBLE);
        mUseMyShippingAddressSwitchview.setVisibility(View.VISIBLE);
        mDefaultBillingAddressSwitch.setVisibility(View.GONE);
        mDefaultShippingAddressSwitch.setVisibility(View.GONE);
        mDefaultShippingAddressSwitchview.setVisibility(View.GONE);
        mUseMyShippingAddressSwitch.setOnCheckedChangeListener(this);

    }

    public void intitalizeViewsForGuestCheckOutBillingAddress() {
        mDefaultBillingAddressSwitch.setVisibility(View.GONE);
        mDefaultShippingAddressSwitch.setVisibility(View.GONE);
        mDefaultShippingAddressSwitchview.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddAddressPresenter(this);
        mBundle = getArguments();
        if (mBundle != null) {
            mRequestCode = mBundle.getInt(REQUEST_CODE);
        }
        if (mRequestCode != StepThreeRootFragment.LOGGED_IN_REQUEST_CODE
                && mRequestCode != StepThreeRootFragment.GUEST_REQUEST_CODE
                && mRequestCode != GuestStepOneRootFragment.REQUEST_CODE
                && mRequestCode != GuestStepThreeRootFragment.REQUEST_CODE) {
            setHasOptionsMenu(true);
        }
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        ButterKnife.bind(this, view);

        //diasble editing on the state & country edittext. We will show up pickers
        mStateOrProvinceOrRegionEdit.setFocusableInTouchMode(false);
        mCountryNameEdit.setFocusableInTouchMode(false);

        //get the arguments and set views for address updation/edit request

        if (mBundle != null) {
            if (mRequestCode == EDIT_ADDRESS_REQUEST) {
                mAddress = (Address) mBundle.getSerializable(ADDRESS);
                populateData(mAddress);
                //show the remove button
                mRemoveAddressButton.setVisibility(View.VISIBLE);
                ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.editAddressTitle));
                new AnalyticsUtil().trackScreen(getString(R.string.label_update_shipping_analytics_title));
            } else if (mRequestCode == StepThreeRootFragment.LOGGED_IN_REQUEST_CODE) {
                intitalizeViewsForCheckOutBillingAddress();
                mAddress = (Address) mBundle.getSerializable(ADDRESS);
                populateData(mAddress);
            } else if (mRequestCode == GuestStepOneRootFragment.REQUEST_CODE) {
                intitalizeViewsForGuestCheckOutBillingAddress();
            } else if (mRequestCode == GuestStepThreeRootFragment.REQUEST_CODE) {
                intitalizeViewsForCheckOutBillingAddress();
                mAddress = (Address) mBundle.getSerializable(ADDRESS);
                populateData(mAddress);
            } else {
                ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.addAddressTitle));
            }
        } else {

            ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.addAddressTitle));

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).enableBackButton();
    }

    public void populateData(Address address) {
        mAddress = address;

        mUseMyShippingAddressSwitch.setEnabled(true);
        mFirstNameEdit.setText(address != null ? address.getFirstName() : "");
        mLastNameEdit.setText(address != null ? address.getLastName() : "");
        mAddressLine1Edit.setText(address != null ? address.getAddress1() : "");
        mAptOrSuiteEdit.setText(address != null ? address.getAddress2() : "");
        mCityEdit.setText(address != null ? address.getCity() : "");
        mStateOrProvinceOrRegionEdit.setText(address != null ? address.getState() : "");
        mZipCodeEdit.setText(address != null ? address.getPostalCode() : "");
        mPhoneNumberEdit.setText(address != null ? address.getPhoneNumber() : "");
        mCountryNameEdit.setText(address != null ? address.getCountry() : "");
        mDefaultBillingAddressSwitch
                .setChecked(address != null ? Boolean.valueOf(address.getIsDefaultBillingAddress()) : false);
        mDefaultShippingAddressSwitch
                .setChecked(address != null ? Boolean.valueOf(address.getIsDefaultShippingAddress()) : false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStateOrProvinceOrRegionEdit.setOnClickListener(this);
        mCountryNameEdit.setOnClickListener(this);

        if (mRequestCode == EDIT_ADDRESS_REQUEST) {
            mRemoveAddressButton.setOnClickListener(this);
        }
        if (mRequestCode == StepThreeRootFragment.LOGGED_IN_REQUEST_CODE) {
            intitalizeViewsForCheckOutBillingAddress();
        }
        if (mRequestCode == StepOneRootFragment.REQUEST_CODE) {
            mDefaultBillingAddressSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean validateFields() {
        boolean invalidFirstName;
        boolean invalidLastName;
        boolean invalidAddressLine1;
        boolean invalidAptOrSuiteNumber;
        boolean invalidCity;
        boolean invalidStateOrProvinceOrRegion;
        boolean invalidZipcode;
        boolean invalidPhoneNumber;
        boolean invalidCountry;
        boolean invalidEmail = false;
        boolean invalidConfirmEmail = false;
        boolean invalidPassword = false;
        boolean invalidConfirmPassword = false;

        //return in-case of any error
        invalidFirstName = checkAndShowError(mFirstNameEdit, mfirstNameLayout, R.string.errorFirstNameRequired);
        invalidLastName = checkAndShowError(mLastNameEdit, mLastNameInputLayout, R.string.errorLastNameIsRequired);
        invalidAddressLine1 = checkAndShowError(mAddressLine1Edit, mAddressLine1InputLayout,
                R.string.errorAddressLine1Required);
        invalidCity = checkAndShowError(mCityEdit, mCityInputLayout, R.string.errorCityRequired);
        invalidStateOrProvinceOrRegion = checkAndShowError(mStateOrProvinceOrRegionEdit,
                mStateOrProvinceOrRegionInputLayout, R.string.errorStateOrProvinceOrRegionRequired);
        invalidZipcode = checkAndShowError(mZipCodeEdit, mZipCodeInputLayout, R.string.errorZipCodeRequired);
        invalidPhoneNumber = checkAndShowError(mPhoneNumberEdit, mPhoneNumberInputLayout,
                R.string.errorPhoneNumberRequired);
        invalidCountry = checkAndShowError(mCountryNameEdit, mCountryNameInputLayout,
                R.string.errorCountryNameRequired);

        if (invalidFirstName ||
                invalidLastName ||
                invalidAddressLine1 ||
                invalidStateOrProvinceOrRegion ||
                invalidCity ||
                invalidZipcode ||
                invalidPhoneNumber ||
                invalidCountry) {
            return false;
        }
        return true;
    }

    @Override
    public void initializeGuestAddressRequest() {

        mAddressRequest = new AddressRequest(
                mDefaultShippingAddressSwitch.isChecked()
                , mLastNameEdit.getText().toString()
                , mUsaStateCode == null ? mAddress.getState() : mUsaStateCode
                //TODO the way usastatecode can be retrived depend upon api.Api should return the state name as well along with state code
                , mAddressLine1Edit.getText().toString()
                , mAptOrSuiteEdit.getText().toString()
                , mCountryCode == null ? mAddress.getCountry() : mCountryCode
                , mCityEdit.getText().toString()
                , mZipCodeEdit.getText().toString()
                , mPhoneNumberEdit.getText().toString()
                , mDefaultBillingAddressSwitch.isChecked()
                , mFirstNameEdit.getText().toString()
                , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

        //setting addressId is must for the editAddress API call
        mAddressRequest.setAddressId(mAddress.getAddressId());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            if (!validateFields()) {
                return super.onOptionsItemSelected(item);
            }
        }

        mProgressBar.setVisibility(View.VISIBLE);

        if (mRequestCode == EDIT_ADDRESS_REQUEST) {
            mAddressRequest = new AddressRequest(
                    mDefaultShippingAddressSwitch.isChecked()
                    , mLastNameEdit.getText().toString()
                    , mUsaStateCode == null ? mAddress.getState() : mUsaStateCode
                    //TODO the way usastatecode can be retrived depend upon api.Api should return the state name as well along with state code
                    , mAddressLine1Edit.getText().toString()
                    , mAptOrSuiteEdit.getText().toString()
                    , mCountryCode == null ? mAddress.getCountry() : mCountryCode
                    , mCityEdit.getText().toString()
                    , mZipCodeEdit.getText().toString()
                    , mPhoneNumberEdit.getText().toString()
                    , mDefaultBillingAddressSwitch.isChecked()
                    , mFirstNameEdit.getText().toString()
                    , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

            //setting addressId is must for the editAddress API call
            mAddressRequest.setAddressId(mAddress.getAddressId());

            mPresenter.updateAddress(mAddressRequest);
        } else {
            mAddressRequest = new AddressRequest(
                    mDefaultShippingAddressSwitch.isChecked()
                    , mLastNameEdit.getText().toString()
                    , mUsaStateCode
                    , mAddressLine1Edit.getText().toString()
                    , mAptOrSuiteEdit.getText().toString()
                    , mCountryCode
                    , mCityEdit.getText().toString()
                    , mZipCodeEdit.getText().toString()
                    , mPhoneNumberEdit.getText().toString()
                    , mDefaultBillingAddressSwitch.isChecked()
                    , mFirstNameEdit.getText().toString()
                    , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

            mPresenter.saveAddress(mAddressRequest);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId) {
        if (auditEditText.getText().toString().isEmpty()) {
            auditTextInputLayout.setError(getContext().getString(errorStringId));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }


    }

    public ShippingAddressRequest getShippingAddressAttribute() {
        ShippingAddressRequest shippingAddressRequest = new ShippingAddressRequest();
        shippingAddressRequest
                .setDynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        shippingAddressRequest.setShippingAddress1(mAddressLine1Edit.getText().toString());
        shippingAddressRequest.setShippingAddressFirstName(mFirstNameEdit.getText().toString());
        shippingAddressRequest.setShippingAddressLastName(mLastNameEdit.getText().toString());
        shippingAddressRequest.setShippingAddressCity(mCityEdit.getText().toString());
        shippingAddressRequest.setShippingAddressPostalCode(mZipCodeEdit.getText().toString());
        shippingAddressRequest.setShippingAddressPhoneNumber(mPhoneNumberEdit.getText().toString());
        shippingAddressRequest.setShippingAddressCountry(mCountryCode == null ? mAddress.getCountry() : mCountryCode);
        shippingAddressRequest.setShippingAddressState(mCountryCode == null ? mAddress.getState() : mUsaStateCode);
        return shippingAddressRequest;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public AddressRequest getAddressRequest() {
        return mAddressRequest;
    }

    @Override
    public void addressAdded() {
        mProgressBar.setVisibility(View.GONE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.addressSavedInAccount).setCancelable(false);
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        mHandler.sendEmptyMessageDelayed(DISMISS_APPROVAL_DIALOG, APPROVAL_DIALOG_DURATION);
    }

    @Override
    public void addressUpdated() {
        mProgressBar.setVisibility(View.GONE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.addressUpdatedInAccount).setCancelable(false);
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        mHandler.sendEmptyMessageDelayed(DISMISS_APPROVAL_DIALOG, APPROVAL_DIALOG_DURATION);
    }

    @Override
    public void addressRemoved() {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mAddressLine1Edit, R.string.addressRemovedMessage, Snackbar.LENGTH_LONG).show();
        popBackStack();

    }

    @Override
    public void showErrorMessage(String errorMessage) {
        hideProgress();
        Snackbar.make(mAddressLine1Edit, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        hideProgress();
        if (span) {
            Utils.displayCrouton(getActivity(), (Spanned) message, mContainerLayout);
        } else {
            Utils.displayCrouton(getActivity(), (String) message, mContainerLayout);
        }
    }

    @Override
    public void setPresenter(AddEditAddressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.stateOrProvinceOrRegion_edit:
                showProgress();
                mPresenter.getUsaStatesList();
                break;

            case R.id.countryName_edit:
                showProgress();
                mPresenter.getCountryList();
                break;

            case R.id.removeAddress_button:
                showDeletionConfirmationDialog();
                break;

        }
    }

    private void showProgress() {
        //we will show the activity loader if its an checkoutActivity becoz in-screen progressBar is not visible many times
        if(getActivity() instanceof CheckOutActivity){
            ((CheckOutActivity)getActivity()).showProgress();
        }
        else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        //we will show the activity loader if its an checkoutActivity becoz in-screen progressBar is not visible many times
        if(getActivity() instanceof CheckOutActivity){
            ((CheckOutActivity)getActivity()).hideProgress();
        }

        mProgressBar.setVisibility(View.GONE);

    }
    @Override
    public void showDeletionConfirmationDialog() {
        AlertDialog alertDialog = Utils.showAlertDailog(getActivity(),
                String.format(getString(R.string.areYouSure), getString(R.string.application_name)),
                getString(R.string.confirmAddressDeletion), R.style.StyleForNotification)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialogDeleteButton).toUpperCase(), this)
                .setNegativeButton(getString(R.string.cancelTextOnDialog).toUpperCase(), this)
                .create();
        alertDialog.show();
    }


    @Override
    public void usaStatesListReceived(String[] usaStateArray) {
        hideProgress();
        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment statesDialogFragment = CommonDialogFragment
                .newInstance(usaStateArray, getActivity().getString(R.string.choose_state_txt), USA_STATE_LIST_REQUEST);
        statesDialogFragment.setValueSetListener(this);
        statesDialogFragment.show(fragManager);

    }

    @Override
    public void countryListReceived(String[] countryArray) {
        hideProgress();
        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment statesDialogFragment = CommonDialogFragment
                .newInstance(countryArray, getActivity().getString(R.string.choose_country_txt), COUNTRY_LIST_REQUEST);
        statesDialogFragment.setValueSetListener(this);
        statesDialogFragment.show(fragManager);
    }

    @Override
    public void onValueSelected(String value, int requestCode) {

        switch (requestCode) {

            case USA_STATE_LIST_REQUEST:
                //initialize the usaStateCode so that it can be passed onto the API
                mUsaStateCode = mPresenter.getUsaStateCode(value);
                if (mUsaStateCode != null) {
                    mStateOrProvinceOrRegionEdit.setText(value);
                } else {
                    showErrorMessage("Cant load States List.Bad Data");
                }
                break;

            case COUNTRY_LIST_REQUEST:
                //initialize the coutryCode so that it can be passed onto the API
                mCountryCode = mPresenter.getCountryCode(value);
                if (mCountryCode != null) {
                    mCountryNameEdit.setText(value);
                } else {
                    showErrorMessage("Cant get country code.Bad Data");
                }
                break;


        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.removeAddress(new RemoveAddressRequest(
                        mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                        mAddress.getAddressId()));

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.useMyShippingAddress_switch:
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        if (mBundle != null) {
                            mAddress = (Address) mBundle.getSerializable(ADDRESS);
                            populateData(mAddress);
                        }
                    } else {
                        populateData(null);

                        if (mRequestCode == StepThreeRootFragment.LOGGED_IN_REQUEST_CODE) {
                            buttonView.setEnabled(false);
                            if (getParentFragment() != null) {
                                ((StepThreeRootFragment) getParentFragment()).getBillingAddressId();
                            }
                        } else if (mRequestCode == GuestStepThreeRootFragment.REQUEST_CODE) {
                            // do nothing . We only need to make all the address fields as blank
                        }
                    }

                }
                break;
        }

    }
}
