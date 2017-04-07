package com.petmeds1800.ui.resetpassword;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.AddUpdateMedicationRemindersIntent;
import com.petmeds1800.intent.ForgotPasswordIntent;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.model.entities.CheckResetPasswordTokenRequest;
import com.petmeds1800.model.entities.PasswordResetResponse;
import com.petmeds1800.model.entities.SaveResetPasswordRequest;
import com.petmeds1800.model.entities.TokenInfo;
import com.petmeds1800.ui.ResetPasswordActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.medicationreminders.service.MedicationReminderResultReceiver;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.petmeds1800.util.Constants.RESULT_VALUE;
import static com.petmeds1800.util.Constants.SUCCESS;

public class ResetPasswordFragment extends AbstractFragment implements ResetPasswordContract.View, MedicationReminderResultReceiver.Receiver, EditText.OnEditorActionListener {

    public static final String CLICK = "Click";
    @BindView(R.id.password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout mPasswordInputLayout;
    @BindView(R.id.confirm_password_edit)
    EditText mConfirmPasswordEdit;
    @BindView(R.id.confirmPasswordInputLayout)
    TextInputLayout mConfirmPasswordInputLayout;
    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;
    @BindView(R.id.resetLinkFailureMsg)
    TextView mResetLinkFailureMsg;
    @BindView(R.id.resetPasswordContainer)
    RelativeLayout mResetPasswordContainer;
    private ResetPasswordContract.Presenter mPresenter;
    private SaveResetPasswordRequest mSaveResetPasswordRequest;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    private MedicationReminderResultReceiver mMedicationReminderResultReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        mSaveResetPasswordRequest = new SaveResetPasswordRequest();
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        mConfirmPasswordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mConfirmPasswordEdit.setOnEditorActionListener(this);
        mPresenter = new ResetPasswordPresenter(this);
        if (bundle != null) {
            if (getActivity() != null) {
                ((ResetPasswordActivity) getActivity()).showProgress();
            }
            mPresenter.checkResetPasswordLinkValidity(new CheckResetPasswordTokenRequest(bundle.getString(Constants.RESET_TOKEN_KEY)));

        }
        return view;
    }


    private void performClickableLinkOperation() {
        String tokenInvalidMessage = getString(R.string.reset_password_token_expired_message);
        int clickableLinkIndex = tokenInvalidMessage.indexOf(CLICK);
        SpannableString spannableString = new SpannableString(tokenInvalidMessage);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new ForgotPasswordIntent(getContext()));
                if (!mPreferencesHelper.getIsUserLoggedIn()) {
                    if (getActivity() != null)
                        getActivity().finishAffinity();
                }
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, clickableLinkIndex, clickableLinkIndex + CLICK.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mResetLinkFailureMsg.setText(spannableString);
        mResetLinkFailureMsg.setMovementMethod(LinkMovementMethod.getInstance());
        mResetLinkFailureMsg.setHighlightColor(Color.TRANSPARENT);
    }

    public static ResetPasswordFragment newInstance(String tokenInfo) {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESET_TOKEN_KEY, tokenInfo);
        resetPasswordFragment.setArguments(bundle);
        return resetPasswordFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveAndValidate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndValidate() {
        boolean proceedUpdate = true;
        mPasswordInputLayout.setError(null);
        mConfirmPasswordInputLayout.setError(null);
        String passwordText = mPasswordEdit.getText().toString().trim();
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
        if (proceedUpdate) {
            if (getActivity() != null) {
                ((ResetPasswordActivity) getActivity()).showProgress();
            }
            mSaveResetPasswordRequest.setConfirmNewPassword(confirmPasswordText);
            mSaveResetPasswordRequest.setNewPassword(passwordText);
            mPresenter.saveResetPasswordDetails(mSaveResetPasswordRequest);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void onSuccess(PasswordResetResponse passwordResetResponse) {
        if (getActivity() != null) {
            ((ResetPasswordActivity) getActivity()).hideProgress();
        }
        if (passwordResetResponse != null) {
            TokenInfo tokenInfo = passwordResetResponse.getTokenInfo();
            if (tokenInfo != null) {
                if (tokenInfo.isValidToken()) {
                    setHasOptionsMenu(true);
                    mSaveResetPasswordRequest.setMatchedProfileId(tokenInfo.getMatchedProfileId());
                    mSaveResetPasswordRequest.setPtk(tokenInfo.getToken());
                    mPasswordInputLayout.setVisibility(View.VISIBLE);
                    mConfirmPasswordInputLayout.setVisibility(View.VISIBLE);
                    mResetLinkFailureMsg.setVisibility(View.GONE);
                } else {
                    onTokenExpired();
                }
            }
        }
    }

    @Override
    public void onSavedResetPasswordDetailsSuccess() {

        if (mPreferencesHelper.getIsUserLoggedIn()) {
            mMedicationReminderResultReceiver = new MedicationReminderResultReceiver(new Handler());
            // This is where we specify what happens when data is received from the service
            mMedicationReminderResultReceiver.setReceiver(this);
            AddUpdateMedicationRemindersIntent addUpdateMedicationRemindersIntent
                    = new AddUpdateMedicationRemindersIntent(getContext(), true);
            addUpdateMedicationRemindersIntent
                    .putExtra("medicationResultReceiver", mMedicationReminderResultReceiver);
            getContext().startService(addUpdateMedicationRemindersIntent);
        } else {
            onSignOutSuccess(false);
        }
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        if (getActivity() != null) {
            ((ResetPasswordActivity) getActivity()).hideProgress();
        }
        Utils.displayCrouton(getActivity(), message.toString(), mResetPasswordContainer);
    }

    @Override
    public void onTokenExpired() {
        performClickableLinkOperation();
        mPasswordInputLayout.setVisibility(View.GONE);
        mConfirmPasswordEdit.setVisibility(View.GONE);
        mResetLinkFailureMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignOutSuccess(boolean isForcedSignOut) {
        if (isForcedSignOut)
            mPreferencesHelper.setIsUserLoggedIn(false);
        if (getActivity() != null) {
            ((ResetPasswordActivity) getActivity()).hideProgress();
        }
        Toast.makeText(getContext(), R.string.password_set_success_message, Toast.LENGTH_LONG).show();
        startActivity(new LoginIntent(getContext()));
        if (getActivity() != null)
            getActivity().finishAffinity();
    }

    @Override
    public void setPresenter(ResetPasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == RESULT_OK) {
            String resultValue = resultData.getString(RESULT_VALUE);
            if (resultValue.equals(SUCCESS)) {
                //mPresenter.signout();
                onSignOutSuccess(true);
            } else {
                Utils.displayCrouton(getActivity(), resultValue, mResetPasswordContainer);
            }

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            saveAndValidate();
        }
        return false;
    }
}
