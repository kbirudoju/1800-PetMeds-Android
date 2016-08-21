package com.petmeds1800.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.User;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.LoginFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSettingsFragment extends AbstractFragment implements AccountSettingsContract.View {

    @BindView(R.id.nameInputLayout)
    TextInputLayout mNameInputLayout;

    @BindView(R.id.emailInputLayout)
    TextInputLayout mEmailInputLayout;

    @BindView(R.id.passwordInputLayout)
    TextInputLayout mPasswordInputLayout;

    @BindView(R.id.name_edit)
    EditText mNameText;

    @BindView(R.id.email_edit)
    EditText mEmailText;

    @BindView(R.id.password_edit)
    EditText mNamePasswordText;

    @Inject
    AccountSettingsContract.Presenter mPresenter;
    private MenuItem mEditMenuItem;
    private MenuItem mDoneMenuItem;
    private String mUserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new AccountSettingsPresenter(this);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        ButterKnife.bind(this, view);
        enableEditTexts(false);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.findUserData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_settings, menu);

        mEditMenuItem = menu.findItem(R.id.action_edit);
        mDoneMenuItem = menu.findItem(R.id.action_done);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            enableEditTexts(true);
            enableDoneAction();
        } else if (id == R.id.action_done) {

            boolean invalidName;
            boolean invalidEmail;
            boolean invalidPassword;

            ///// Negative flow /////
            //name validation
            if (!mPresenter.validateName(mNameText.getText().toString())) {
                mNameInputLayout.setError(getContext().getString(R.string.accountSettingsNameEmptyError));
                invalidName = true;
            } else {
                mNameInputLayout.setError(null);
                //following line would help to keep the view size intact
                mNameInputLayout.setErrorEnabled(false);
                invalidName = false;
            }

            //email validation
            if (mEmailText.getText().toString().isEmpty()) {
                mEmailInputLayout.setError(getContext().getString(R.string.accountSettingsEmailEmptyError));
                invalidEmail = true;
            } else if (!mPresenter.validateEmail(mEmailText.getText().toString())) {
                mEmailInputLayout.setError(getContext().getString(R.string.accountSettingsEmailInvalidError));
                invalidEmail = true;
            } else {
                mEmailInputLayout.setError(null);
                mEmailInputLayout.setErrorEnabled(false);
                invalidEmail = false;
            }

            //password validation
            if (mNamePasswordText.getText().toString().isEmpty()) {
                mPasswordInputLayout.setError(getContext().getString(R.string.accountSettingsPasswordEmptyError));
                invalidPassword = true;
            } else if (!mPresenter.validatePassword(mNamePasswordText.getText().toString())) {
                mPasswordInputLayout.setError(getContext().getString(R.string.accountSettingsPasswordInvalidError));
                invalidPassword = true;
            } else {
                mPasswordInputLayout.setError(null);
                mPasswordInputLayout.setErrorEnabled(false);
                invalidPassword = false;
            }
            //return if needed
            if (invalidName || invalidEmail || invalidPassword) {
                return false;
            }

            //// Start the Positve flow ////
            //following should be executable after validation success
            enableEditTexts(false);
            enableEditAction();
            mPresenter.saveSettings(new UpdateAccountSettingsRequest(
                     mNameText.getText().toString()
                    ,""   //need to confirm from the backend system
                    ,mEmailText.getText().toString()
                    ,mUserId+"123"  //TODO need to remove additional 123 as userId.Will do once error handling code is verified in the presenter class
                    ,mNamePasswordText.getText().toString()
                    , LoginFragment.sessionConfirmationNUmber));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void enableDoneAction() {
        mDoneMenuItem.setVisible(true);
        mEditMenuItem.setVisible(false);
    }

    @Override
    public void enableEditAction() {
        mDoneMenuItem.setVisible(false);
        mEditMenuItem.setVisible(true);
    }

    @Override
    public void enableEditTexts(boolean enable) {
        if (enable) {
            mNameText.setEnabled(true);
            mEmailText.setEnabled(true);
            mNamePasswordText.setEnabled(true);
        } else {
            mNameText.setEnabled(false);
            mEmailText.setEnabled(false);
            mNamePasswordText.setEnabled(false);
        }
    }

    @Override
    public void setUserData(User user) {
        mNameText.setText(user.getFirstName());
        mEmailText.setText(user.getEmail());
        //we will never receive password as part of the response in User model
        mNamePasswordText.setText("*******");
        //intialize the userId to use while changing the settings
        mUserId = user.getUserId();

    }

    @Override
    public void showSuccess() {
        Snackbar.make(mNamePasswordText, R.string.accountSettingsChangedSuccessfully, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void setPresenter(AccountSettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
