package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.R;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShippingAddress;
import com.petmeds1800.model.shoppingcart.response.ShippingGroups;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.LoginActivity;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.util.InputValidationUtil;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 04-10-2016.
 */


public class GuestStepOneRootFragment extends AbstractFragment
        implements GuestStepOneRootContract.AddGuestNewAddressFragmentInteractionListener,
        GuestStepOneRootContract.View {

    @BindView(R.id.login_navigator)
    Button mLoginNavigator;

    @BindView(R.id.email_edit)
    EditText mEmailEdit;

    @BindView(R.id.emailInputLayout)
    TextInputLayout mEmailInputLayout;

    @BindView(R.id.confirm_email_edit)
    EditText mConfirmEmailEdit;

    @BindView(R.id.confirmEmailInputLayout)
    TextInputLayout mConfirmEmailInputLayout;

    @BindView(R.id.createpasswordHeader)
    TextView mCreatepasswordHeader;

    @BindView(R.id.password_edit)
    EditText mPasswordEdit;

    @BindView(R.id.passwordInputLayout)
    TextInputLayout mPasswordInputLayout;

    @BindView(R.id.confirm_password_edit)
    EditText mConfirmPasswordEdit;

    @BindView(R.id.confirmPasswordInputLayout)
    TextInputLayout mConfirmPasswordInputLayout;

    @BindView(R.id.defaultShippingAddress_switch)
    Switch mDefaultShippingAddressSwitch;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    private String mStepName;

    public static final int REQUEST_CODE = 7;

    private ShoppingCartListResponse mShoppingCartListResponse;

    private String mshippingAddressId;

    private GuestStepOneRootContract.Presenter mPresentor;

    private Address mAddress;

    public boolean validateEmail(EditText auditEditText, TextInputLayout auditTextInputLayout) {
        if (!Patterns.EMAIL_ADDRESS.matcher(auditEditText.getText().toString()).matches()) {
            auditTextInputLayout.setError(getContext().getString(R.string.error_invalid_email));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    public boolean validatePassword(EditText auditEditText, TextInputLayout auditTextInputLayout) {
        if (!auditEditText.getText().toString().matches(InputValidationUtil.passwordPattern)) {
            auditTextInputLayout.setError(getContext().getString(R.string.accountSettingsPasswordInvalidError));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    public boolean confirmValidation(EditText auditEditText, EditText confirmEditText,
            TextInputLayout auditTextInputLayout, int errorStringId) {
        if (!Utils.checkConfirmFields(auditEditText, confirmEditText)) {
            auditTextInputLayout.setError(getContext().getString(errorStringId));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    public static GuestStepOneRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse,
            String stepName) {
        GuestStepOneRootFragment f = new GuestStepOneRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        args.putString(CheckOutActivity.STEP_NAME, stepName);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStepName = getArguments().getString(CheckOutActivity.STEP_NAME);
        mShoppingCartListResponse = (ShoppingCartListResponse) getArguments()
                .getSerializable(CartFragment.SHOPPING_CART);
        mPresentor = new GuestStepOneRootPresentor(this);

        //start address extraction
        mPresentor.extractAddress(mShoppingCartListResponse.getShoppingCart().getShippingGroups());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_step_one_fragment, container, false);
        ((CheckOutActivity) getActivity()).setActiveStep(mStepName);
        ButterKnife.bind(this, view);

        populateEmail();

        return view;
    }

    private void populateEmail() {
        ArrayList<ShippingGroups> shippingGroups = mShoppingCartListResponse.getShoppingCart().getShippingGroups();
        if(shippingGroups != null || shippingGroups.size() > 0) {

            ShippingAddress shippingAddress = shippingGroups.get(0)
                    .getShippingAddress();

            if(shippingAddress != null) {

                if(shippingAddress.getEmail() != null) {
                    mEmailEdit.setText(
                            shippingAddress.getEmail());
                    mConfirmEmailEdit.setText(
                            shippingAddress.getEmail());
                }

            }
        }
    }


    private AddEditAddressFragment getFragmentReference() {
        return (AddEditAddressFragment) getChildFragmentManager().findFragmentById(R.id.billingAddressfragment);

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

    public boolean validateFields() {
        boolean invalidEmail = false;
        boolean invalidConfirmEmail = false;
        boolean invalidPassword = false;
        boolean invalidConfirmPassword = false;
        invalidEmail = checkAndShowError(mEmailEdit, mEmailInputLayout, R.string.errorEmailFieldRequired)
                || validateEmail(mEmailEdit, mEmailInputLayout);

        invalidConfirmEmail =
                checkAndShowError(mConfirmEmailEdit, mConfirmEmailInputLayout, R.string.confirmFieldRequired)
                        || confirmValidation(mEmailEdit, mConfirmEmailEdit, mConfirmEmailInputLayout,
                        R.string.errorConfirmEmail);
        if (!mPasswordEdit.getText().toString().isEmpty()) {
            invalidPassword = validatePassword(mPasswordEdit, mPasswordInputLayout);
            invalidConfirmPassword =
                    checkAndShowError(mConfirmPasswordEdit, mConfirmPasswordInputLayout, R.string.error_field_required)
                            ||
                            confirmValidation(mPasswordEdit, mConfirmPasswordEdit, mConfirmPasswordInputLayout,
                                    R.string.error_confirm_password_must_match);
        } else {
            mPasswordInputLayout.setError(null);
            mPasswordInputLayout.setErrorEnabled(false);
            mConfirmPasswordInputLayout.setError(null);
            mConfirmPasswordInputLayout.setErrorEnabled(false);
        }
        if (invalidEmail || invalidConfirmEmail || invalidPassword || invalidConfirmPassword) {
            return false;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceStepRootChildFragment(AddEditAddressFragment.newInstance(mAddress, GuestStepOneRootFragment.REQUEST_CODE),
                R.id.billingAddressfragment);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);

    }

    private void postShippingAddressData() {
        ShippingAddressRequest shippingAddressAttribute = getFragmentReference().getShippingAddressAttribute();
        shippingAddressAttribute.setEmail(mEmailEdit.getText().toString());
        shippingAddressAttribute.setConfirmEmail(mConfirmEmailEdit.getText().toString());
        shippingAddressAttribute.setUseShippingAddressAsDefault(mDefaultShippingAddressSwitch.isChecked());
        shippingAddressAttribute
                .setPassword(mPasswordEdit.getText().toString().isEmpty() ? null : mPasswordEdit.getText().toString());
        shippingAddressAttribute.setConfirmPassword(
                mConfirmPasswordEdit.getText().toString().isEmpty() ? null : mConfirmPasswordEdit.getText().toString());
        ((CheckOutActivity) getActivity()).showProgress();
        mPresentor.saveGuestShippingAddressData(shippingAddressAttribute);
    }

    @OnClick({R.id.login_navigator, R.id.shippingNavigator})
    public void onClick(View view) {
        boolean isValidateFromActivity = validateFields();
        boolean isValidateFromFragment = getFragmentReference().validateFields();
        switch (view.getId()) {
            case R.id.login_navigator:
                LoginIntent loginIntent = new LoginIntent(getActivity());
                loginIntent.setAction(LoginActivity.START_CHECKOUT);
                loginIntent.putExtra(CartFragment.SHOPPING_CART,mShoppingCartListResponse);
                startActivity(loginIntent);
//                getActivity().finishAffinity();
                break;
            case R.id.shippingNavigator:
                if (!isValidateFromActivity || !isValidateFromFragment) {
                    return;
                }
                postShippingAddressData();
                break;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void navigateOnSuccess(ShoppingCartListResponse response) {
        ((CheckOutActivity) getActivity()).hideProgress();
        ((CheckOutActivity) getActivity()).moveToNext(mStepName, response);

    }

    @Override
    public void onError(String errorMessage) {
        ((CheckOutActivity) getActivity()).hideProgress();
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        ((CheckOutActivity) getActivity()).hideProgress();
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }

    @Override
    public void setAddress(Address address) {
        mAddress = address;
        if(getFragmentReference() != null && address != null) {
            getFragmentReference().populateData(address);
        }
    }

    @Override
    public void setPresenter(GuestStepOneRootContract.Presenter presenter) {
        mPresentor = presenter;
    }
}
