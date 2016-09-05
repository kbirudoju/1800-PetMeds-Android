package com.petmeds1800.ui.account;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.User;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

public class AccountSettingsFragment extends AbstractFragment implements AccountSettingsContract.View {


    @BindView(R.id.emailInputLayout)
    TextInputLayout mEmailInputLayout;

    @BindView(R.id.passwordInputLayout)
    TextInputLayout mPasswordInputLayout;

    @BindView(R.id.email_edit)
    EditText mEmailText;

    @BindView(R.id.password_edit)
    EditText mPasswordText;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    private AccountSettingsContract.Presenter mPresenter;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private MenuItem mEditMenuItem;

    private MenuItem mDoneMenuItem;

    private String mUserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new AccountSettingsPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        ButterKnife.bind(this, view);
        ((AbstractActivity)getActivity()).enableBackButton();
        ((AbstractActivity)getActivity()).setToolBarTitle(getContext().getString(R.string.accountSettingsTitle));
        enableEditTexts(false);

        //handle keyboard input
        mPasswordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateAndUpdate();
                    handled = true;
                }
                return handled;

            }
        });
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
            //clear the password field since we dont store the user's password
            mPasswordText.setText("");

        } else if (id == R.id.action_done) {

            validateAndUpdate();


        }

        return super.onOptionsItemSelected(item);
    }

    private void validateAndUpdate() {
        boolean invalidEmail;
        boolean invalidPassword;

        ///// Negative flow /////
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
        if (mPasswordText.getText().toString().isEmpty()) {
            mPasswordInputLayout.setError(getContext().getString(R.string.accountSettingsPasswordEmptyError));
            invalidPassword = true;
        } else if (!mPresenter.validatePassword(mPasswordText.getText().toString())) {
            mPasswordInputLayout.setError(getContext().getString(R.string.accountSettingsPasswordInvalidError));
            invalidPassword = true;
        } else {
            mPasswordInputLayout.setError(null);
            mPasswordInputLayout.setErrorEnabled(false);
            invalidPassword = false;
        }
        //return if needed
        if (invalidEmail || invalidPassword) {
        // do nothing as we have already prompted for the errors
        }
        else {
            //// Start the Positve flow ////
            mProgressBar.setVisibility(View.VISIBLE);
            //following should be executable after validation success
            enableEditTexts(false);
            enableEditAction();
            mPresenter.saveSettings(new UpdateAccountSettingsRequest(
                    mEmailText.getText().toString()
                    , mUserId
                    , mPasswordText.getText().toString()
                    , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()));

        }
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
            mEmailText.setEnabled(true);
            mPasswordText.setEnabled(true);
        } else {
            mEmailText.setEnabled(false);
            mPasswordText.setEnabled(false);
        }
    }

    @Override
    public void setUserData(User user) {
        mProgressBar.setVisibility(View.GONE);

        mEmailText.setText(user.getEmail());
        //we will never receive password as part of the response in User model
        mPasswordText.setText("*******");
        //intialize the userId to use while changing the settings
        mUserId = user.getUserId();

    }

    @Override
    public void showSuccess() {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mPasswordText, R.string.accountSettingsChangedSuccessfully, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mPasswordText, error, Snackbar.LENGTH_SHORT).show();
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
