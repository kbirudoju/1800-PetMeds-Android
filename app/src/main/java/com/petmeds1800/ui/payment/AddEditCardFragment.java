package com.petmeds1800.ui.payment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.RemoveCardRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.UpdateCardRequest;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.address.AddressSelectionListFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.MonthYearPicker;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddEditCardFragment extends AbstractFragment
        implements AddACardContract.View, DialogInterface.OnClickListener {

    public static final int EDIT_CARD_REQUEST = 1;

    public static final String FIRST_ARG = "firstArg";

    public static final String REQUEST_CODE = "request_code";

    private static final String CARD = "card";

    private static final int DISMISS_APPROVAL_DIALOG = 1;

    private static final long APPROVAL_DIALOG_DURATION = 1000;

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

    @BindView(R.id.error_layout)
    LinearLayout mErrorLayout;

    @BindView(R.id.error_Text)
    TextView mErrorLabel;

    @BindView(R.id.addACard_form)
    ScrollView mCardContainerScrollView;

    @BindView(R.id.removeCard_button)
    Button mRemoveCardButton;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private AddACardContract.Presenter mPresenter;

    private AlertDialog mAlertDialog;

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

    private int mSelectedExpirationMonth;

    private int mSelectedExpirationYear;

    private Address mAddress;

    private Card mCard;

    private int mRequestCode;

    public static AddEditCardFragment newInstance(Card updatedCard, int requestCode) {

        Bundle bundle = new Bundle();

        if (requestCode == EDIT_CARD_REQUEST) {
            bundle.putSerializable(CARD, updatedCard);
        }

        bundle.putInt(REQUEST_CODE, requestCode);

        AddEditCardFragment addEditCardFragment = new AddEditCardFragment();
        addEditCardFragment.setArguments(bundle);

        return addEditCardFragment;

    }

    public static AddEditCardFragment newInstance(Address address, int requestCode) {

        Bundle bundle = new Bundle();

        bundle.putSerializable(FIRST_ARG, address);
        bundle.putInt(REQUEST_CODE, requestCode);

        AddEditCardFragment addEditCardFragment = new AddEditCardFragment();
        addEditCardFragment.setArguments(bundle);

        return addEditCardFragment;

    }

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
        ((AbstractActivity) getActivity()).enableBackButton();

        //diasble editing on the expiration date edittext. We will show up a expiration date dialog
        mExpirationDateEdit.setFocusableInTouchMode(false);

        //check if mAddress is available
        Bundle bundle = getArguments();
        if (bundle != null) {

            //check if this is a EDIT_CARD request.If yes, then we need to get the card and populate the date
            mRequestCode = bundle.getInt(REQUEST_CODE);

            if (mRequestCode == EDIT_CARD_REQUEST) {
                //hide the cardNumber and CVV view as it card number,CVV can not be edited
                mCardNumberLayout.setVisibility(View.GONE);
                mCvvInputLayout.setVisibility(View.GONE);
                //show the remove button
                mRemoveCardButton.setVisibility(View.VISIBLE);
                //get the card
                mCard = (Card) bundle.getSerializable(CARD);
                populateData(mCard);
                ((AbstractActivity) getActivity()).setToolBarTitle(mCard.getCardType() + " " + mCard.getCardNumber());
            }
            //check if we have an address for the "add a card" request or "edit a card" request
            mAddress = (Address) bundle.getSerializable(FIRST_ARG);
            if (mAddress != null) {
                displayAddress(mAddress);
            }
            ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.addACardTitle));

        } else {
            ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.addACardTitle));
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
            String cvv = mCvvEdit.getText().toString();
            boolean isDefaultPayment = mDefaultPaymentSwitch.isChecked();

            boolean invalidCreditCardNumber = true;
            boolean invalidExpirationDate;
            boolean invalidCvv = false;

            ///// Negative flow /////

            if (mRequestCode
                    != EDIT_CARD_REQUEST) { //card Number and CVV are applicable for the add a card request only
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

                //cvv validation
                if (!mPresenter.isCvvValid(mCvvEdit.getText().toString())) {
                    mCvvInputLayout.setError(getContext().getString(R.string.invalidCvvNumberError));
                    invalidCvv = true;
                } else {
                    mCvvInputLayout.setError(null);
                    mCvvInputLayout.setErrorEnabled(false);
                    invalidCvv = false;
                }
            }

            //expiration date validation is valid for both cases - AddACard and EditACard request
            if (!mPresenter.isExpirationDateValid(mSelectedExpirationMonth, mSelectedExpirationYear)) {
                mExpirationDateInputLayout.setError(getContext().getString(R.string.invalidExpirationDateError));
                invalidExpirationDate = true;
            } else {
                mExpirationDateInputLayout.setError(null);
                mExpirationDateInputLayout.setErrorEnabled(false);
                invalidExpirationDate = false;
            }

            if (mRequestCode == EDIT_CARD_REQUEST) {
                //return if needed
                if (invalidExpirationDate) {
                    return super.onOptionsItemSelected(item);
                }

                mProgressBar.setVisibility(View.VISIBLE);

                UpdateCardRequest updateCardRequest = new UpdateCardRequest(
                        String.valueOf(mSelectedExpirationMonth)
                        , String.valueOf(mSelectedExpirationYear)
                        , isDefaultPayment, mCard.getCardKey()
                        , mAddress.getAddressId()
                        , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

                mPresenter.updateCard(updateCardRequest);
            } else {
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

    @OnClick(R.id.addressSelection_label)
    public void selectAddress() {
        replaceAndAddToBackStack(AddressSelectionListFragment.newInstance(mRequestCode),
                AddressSelectionListFragment.class.getSimpleName());
    }

    @Override
    public void populateData(Card card) {
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getAddress(card.getBillingAddress().getRepositoryId());

        //till the time we get the full Address , populate the other fields
        mExpirationDateEdit.setText(
                Utils.getShortMonthName(Integer.parseInt(card.getExpirationMonth())) + " " + card.getExpirationYear());
        mDefaultPaymentSwitch.setChecked(card.isCardIsDefault());

        //initialize the expiration month and year
        mSelectedExpirationMonth = Integer.parseInt(card.getExpirationMonth());
        mSelectedExpirationYear = Integer.parseInt(card.getExpirationYear());
    }

    @Override
    public void showErrorInMiddle(String errorMessage) {
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorLabel.setText(errorMessage);

    }

    @OnClick(R.id.retry_Button)
    public void retry() {
        mErrorLayout.setVisibility(View.GONE);
        //show progress
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getAddress(mCard.getBillingAddress().getRepositoryId());

    }

    @Override
    public void displayAddress(Address address) {
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);

        mAddress = address;
        mAddressContainerLayout.setVisibility(View.VISIBLE);
        mAddressNameLabel.setText(address.getFirstName() + " " + address.getLastName());
        mAddressLine1Label.setText(address.getAddress1());

        if (address.getAddress2() == null || address.getAddress2().isEmpty()) {
            mAddressLine2Label.setText(address.getCity() + ", " + address.getState() + " " + address.getPostalCode());
        } else {
            mAddressLine2Label.setText(
                    address.getAddress2() + ", " + address.getCity() + ", " + address.getState() + " " + address
                            .getPostalCode());
        }

        mCountryLabel.setText(address.getCountry());
        mPhoneNumberLabel.setText(
                String.format(getContext().getString(R.string.phoneNumberInAddress), address.getPhoneNumber()));

    }


    @OnClick(R.id.expirationDate_edit)
    void showExpirationDate() {

        final MonthYearPicker myp = new MonthYearPicker(getActivity());
        myp.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mExpirationDateEdit.setText(myp.getSelectedMonthShortName() + " " + myp.getSelectedYear());
                //putting the values in the fields so that we can use them directly avoiding the need to calculate it further
                mSelectedExpirationMonth = myp.getSelectedMonth();
                mSelectedExpirationYear = myp.getSelectedYear();
            }
        }, null);

        myp.show();
    }

    @OnClick(R.id.removeCard_button)
    public void showDeletionConfirmationDialog() {
        AlertDialog alertDialog = Utils.showAlertDailog(getActivity(),
                String.format(getString(R.string.areYouSure), getString(R.string.application_name)),
                getString(R.string.confirmCardDeletion), R.style.StyleForNotification)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialogDeleteButton).toUpperCase(), this)
                .setNegativeButton(getString(R.string.cancelTextOnDialog).toUpperCase(), this)
                .create();
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.removeCard(new RemoveCardRequest(
                        mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                        mCard.getCardKey()));

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void cardRemoved() {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mCardContainerScrollView, R.string.cardRemovedMessage, Snackbar.LENGTH_LONG).show();
        popBackStack();

    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        mProgressBar.setVisibility(View.GONE);
        if (span) {
            Utils.displayCrouton(getActivity(), (Spanned) message, mCardContainerScrollView);
        }
        Utils.displayCrouton(getActivity(), (String) message, mCardContainerScrollView);
    }
}
