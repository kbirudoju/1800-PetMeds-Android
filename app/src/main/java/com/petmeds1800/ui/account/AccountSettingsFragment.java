package com.petmeds1800.ui.account;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.User;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.confirm_password_edit)
    EditText mConfirmPasswordEdit;

    @BindView(R.id.confirmPasswordInputLayout)
    TextInputLayout mConfirmPasswordInputLayout;

    private AccountSettingsContract.Presenter mPresenter;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private MenuItem mEditMenuItem;

    private MenuItem mDoneMenuItem;

    private String mUserId;

    private static final String BLANK = "";

    @BindView(R.id.account_setting_container)
    LinearLayout mAccountSettingContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new AccountSettingsPresenter(this,getActivity());
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        new AnalyticsUtil().trackScreen(getString(R.string.label_edit_email_password_analytics_title));
        ButterKnife.bind(this, view);
        ((AbstractActivity) getActivity()).enableBackButton();
        ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.accountSettingsTitle));
        enableEditTexts(false);

        //handle keyboard input
        mConfirmPasswordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
            mPasswordText.setText(BLANK);
            mConfirmPasswordEdit.setText(BLANK);

        } else if (id == R.id.action_done) {

            validateAndUpdate();


        }

        return super.onOptionsItemSelected(item);
    }

    private void validateAndUpdate() {

        boolean proceedUpdate = true;

        mEmailInputLayout.setError(null);
        mPasswordInputLayout.setError(null);
        mConfirmPasswordInputLayout.setError(null);

        String emailText = mEmailText.getText().toString().trim();
        if (emailText.isEmpty()) {
            mEmailInputLayout.setError(getString(R.string.accountSettingsEmailEmptyError));
            proceedUpdate = false;
        } else if (!mPresenter.validateEmail(emailText)) {
            mEmailInputLayout.setError(getString(R.string.accountSettingsEmailInvalidError));
            proceedUpdate = false;
        }

        String passwordText = mPasswordText.getText().toString().trim();
        if (passwordText.isEmpty()) {
            mPasswordInputLayout.setError(getString(R.string.accountSettingsPasswordEmptyError));
            proceedUpdate = false;
        } else if (!mPresenter.validatePassword(passwordText)) {
            mPasswordInputLayout.setError(getString(R.string.accountSettingsPasswordInvalidError));
            proceedUpdate = false;
        }

        String confirmPasswordText = mConfirmPasswordEdit.getText().toString().trim();
        if (confirmPasswordText.isEmpty()) {
            mConfirmPasswordInputLayout.setError(getString(R.string.accountSettingsPasswordEmptyError));
            proceedUpdate = false;
        } else if (!passwordText.equals(confirmPasswordText)) {
            mConfirmPasswordInputLayout.setError(getString(R.string.error_confirm_password_must_match));
            proceedUpdate = false;
        }

        if(proceedUpdate){
            mProgressBar.setVisibility(View.VISIBLE);
            //following should be executable after validation success
            enableEditTexts(false);
            enableEditAction();
            mPresenter.saveSettings(new UpdateAccountSettingsRequest(
                    emailText
                    , mUserId
                    , passwordText
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
            mConfirmPasswordInputLayout.setVisibility(View.VISIBLE);
        } else {
            mEmailText.setEnabled(false);
            mPasswordText.setEnabled(false);
            mConfirmPasswordInputLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserData(User user) {
        mProgressBar.setVisibility(View.GONE);

        mEmailText.setText(user.getEmail());
        //we will never receive password as part of the response in User model
        mPasswordText.setText("********");
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
        Utils.displayCrouton(getActivity(),error,mAccountSettingContainer);
      //  Snackbar.make(mPasswordText, error, Snackbar.LENGTH_SHORT).show();
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
