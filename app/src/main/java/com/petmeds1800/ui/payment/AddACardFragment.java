package com.petmeds1800.ui.payment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.address.AddressSelectionListFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.MonthYearPicker;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddACardFragment extends AbstractFragment implements AddACardContract.View {

    @BindView(R.id.cardNumberLayout)
    TextInputLayout mCardNumberLayout;

    @BindView(R.id.expirationDateInputLayout)
    TextInputLayout mExpirationDateInputLayout;

    @BindView(R.id.cvvInputLayout)
    TextInputLayout mCvvInputLayout;

    @BindView(R.id.cardNumber_edit)
    EditText mCardNumberEdit;

    @BindView(R.id.expirationDate_edit)
    EditText mExpirationDateEdit;

    @BindView(R.id.cvv_edit)
    EditText mCvvEdit;

    @BindView(R.id.defaultPayment_switch)
    Switch mDefaultPaymentSwitch;

    @BindView(R.id.addressName_label)
    TextView mAddressNameLabel;

    @BindView(R.id.addressLine1_label)
    TextView mAddressLine1Label;

    @BindView(R.id.addressLine2_label)
    TextView mAddressLine2Label;

    @BindView(R.id.phoneNumber_label)
    TextView mPhoneNumberLabel;

    @BindView(R.id.country_label)
    TextView mCountryLabel;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.addressContainer_layout)
    RelativeLayout mAddressContainerLayout;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private AddACardContract.Presenter mPresenter;

    private AlertDialog mAlertDialog;

    private static final int DISMISS_APPROVAL_DIALOG = 1;

    private static final long APPROVAL_DIALOG_DURATION = 1000;

    public static final String FIRST_ARG = "firstArg";

    private int mSelectedExpirationMonth;
    private int mSelectedExpirationYear;
    private Address mAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddACardPresenter(this);
        setHasOptionsMenu(true);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_a_card, container, false);
        ButterKnife.bind(this, view);
        ((AbstractActivity)getActivity()).enableBackButton();
        ((AbstractActivity)getActivity()).setToolBarTitle(getContext().getString(R.string.addACardTitle));
        //diasble editing on the expiration date edittext. We will show up a expiration date dialog
        mExpirationDateEdit.setFocusableInTouchMode(false);

        //check if mAddress is available
        Bundle bundle = getArguments();
        if(bundle != null){
            mAddress = (Address) bundle.getSerializable(FIRST_ARG);
            if(mAddress != null) {
                displayAddress(mAddress);
            }
        }

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

            String cardNumber = mCardNumberEdit.getText().toString();
            String expirationDate = mExpirationDateEdit.getText().toString();
            String cvv = mCvvEdit.getText().toString();
            boolean isDefaultPayment = mDefaultPaymentSwitch.isChecked();

            boolean invalidCreditCardNumber;
            boolean invalidExpirationDate;
            boolean invalidCvv;

            ///// Negative flow /////
            //card number validation
            if (!mPresenter.isCreditCardNumberValid(mCardNumberEdit.getText().toString())) {
                mCardNumberLayout.setError(getContext().getString(R.string.invalidCreditCardError));
                invalidCreditCardNumber = true;
            } else {
                mCardNumberLayout.setError(null);
                //following line would help to keep the view size intact
                mCardNumberLayout.setErrorEnabled(false);
                invalidCreditCardNumber = false;
            }

            //expiration date validation
            if (!mPresenter.isExpirationDateValid(mSelectedExpirationMonth, mSelectedExpirationYear)) {
                mExpirationDateInputLayout.setError(getContext().getString(R.string.invalidExpirationDateError));
                invalidExpirationDate = true;
            } else {
                mExpirationDateInputLayout.setError(null);
                mExpirationDateInputLayout.setErrorEnabled(false);
                invalidExpirationDate = false;
            }

            //cvv validation
            if (!mPresenter.isCvvValid(mCvvEdit.getText().toString())) {
                mCvvInputLayout.setError(getContext().getString(R.string.invalidCvvNumberError));
                invalidCvv = true;
            } else {
                mCvvInputLayout.setError(null);
                mCvvInputLayout.setErrorEnabled(false);
                invalidCvv = false;
            }
            //return if needed
            if (invalidCreditCardNumber || invalidExpirationDate || invalidCvv) {
                return super.onOptionsItemSelected(item);
            }

            mProgressBar.setVisibility(View.VISIBLE);
            CardRequest card = new CardRequest(
                    cardNumber
                    , String.valueOf(mSelectedExpirationMonth)
                    , String.valueOf(mSelectedExpirationYear)
                    , String.valueOf(isDefaultPayment)
                    , cvv
                    , mAddress != null ? mAddress.getAddressId() : ""
                    , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

            mPresenter.saveCard(card);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void paymentMethodApproved() {
        mProgressBar.setVisibility(View.GONE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.cardSavedInAccount).setCancelable(false);
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        mHandler.sendEmptyMessageDelayed(DISMISS_APPROVAL_DIALOG, APPROVAL_DIALOG_DURATION);
    }

    @Override
    public void paymentMethodDisapproved(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mCardNumberEdit, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(AddACardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == DISMISS_APPROVAL_DIALOG) {
                mAlertDialog.dismiss();
                popBackStack();
            }
            return false;
        }
    });

    @OnClick(R.id.addressSelection_label)
    public void selectAddress(){
        replaceAndAddToBackStack(new AddressSelectionListFragment() , AddressSelectionListFragment.class.getSimpleName());
    }

    @Override
    public void displayAddress(Address address) {
        mAddress = address;
        mAddressContainerLayout.setVisibility(View.VISIBLE);
        mAddressNameLabel.setText(address.getFirstName() + " " + address.getLastName());
        mAddressLine1Label.setText(address.getAddress1());
        mAddressLine2Label.setText(address.getAddress2() + " " + address.getState() + " " + address.getPostalCode());
        mCountryLabel.setText(address.getCountry());
        mPhoneNumberLabel.setText(address.getPhoneNumber());

    }


    @OnClick(R.id.expirationDate_edit)
    void showExpirationDate(){

        final MonthYearPicker myp = new MonthYearPicker(getActivity());
        myp.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mExpirationDateEdit.setText(myp.getSelectedMonthShortName() + " " + myp.getSelectedYear());
                //putting the values in the fields so that we can use them directly avoiding the need to calculate it further
                mSelectedExpirationMonth = myp.getSelectedMonth();
                mSelectedExpirationYear = myp.getSelectedYear();
            }
        },null);

        myp.show();
    }
}
