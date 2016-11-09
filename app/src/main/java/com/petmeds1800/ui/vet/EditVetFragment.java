package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.RemoveVetRequest;
import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.BaseDialogFragment;
import com.petmeds1800.ui.fragments.dialog.OkCancelDialogFragment;
import com.petmeds1800.ui.vet.presenter.EditVetPresenter;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/19/2016.
 */
public class EditVetFragment extends AbstractFragment implements EditVetContract.View, OnClickListener {
    @BindView(R.id.vet_name_edit)
    EditText mVetNameEdit;

    @BindView(R.id.clinic_name_edit)
    EditText mClinicNameEdit;

    @BindView(R.id.phone_number_edit)
    EditText mPhoneNumberEdit;

    @BindView(R.id.phoneNumberInputLayout)
    TextInputLayout mPhoneNumberInputLayout;

    @BindView(R.id.clinicNameInputLayout)
    TextInputLayout mClinicNameInputLayout;

    @BindView(R.id.vetNameInputLayout)
    TextInputLayout mVetNameInputLayout;

    private MenuItem mEditMenuItem;

    private MenuItem mDoneMenuItem;

    private EditVetContract.Presenter mPresenter;
    private String mVetId;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    @BindView(R.id.removeVetButton)
    Button mRemoveVetButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_vet, null);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mPresenter = new EditVetPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        mRemoveVetButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Vet vet = (Vet) getArguments().getSerializable("vet_info");
        if (vet != null) {
            ((AbstractActivity) getActivity()).setToolBarTitle((vet.getName()));
            mVetId = vet.getId();
            mVetNameEdit.setText(vet.getName());
            mClinicNameEdit.setText(vet.getClinic());
            mPhoneNumberEdit.setText(vet.getPhoneNumber());

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_settings, menu);
        mEditMenuItem = menu.findItem(R.id.action_edit);
        mDoneMenuItem = menu.findItem(R.id.action_done);
        mEditMenuItem.setVisible(false);
        mDoneMenuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {

            boolean isValidVetName;
            boolean isValidClinicName;
            boolean isValidPhoneNumber;

            isValidVetName = checkAndShowError(mVetNameEdit, mVetNameInputLayout, R.string.error_petname);
            isValidClinicName = checkAndShowError(mClinicNameEdit, mClinicNameInputLayout, R.string.clinic_empty_error);
            isValidPhoneNumber = checkAndShowError(mPhoneNumberEdit, mPhoneNumberInputLayout, R.string.error_phone_required);

            if (isValidVetName ||
                    isValidClinicName ||
                    isValidPhoneNumber
                    ) {
                Utils.displayCrouton(getActivity(), getString(R.string.errorMsgForEmail));
                return super.onOptionsItemSelected(item);
            }

        }
        UpdateVetRequest updateVetRequest = new UpdateVetRequest(mVetId, mClinicNameEdit.getText().toString(), mPhoneNumberEdit.getText().toString(), mVetNameEdit.getText().toString(), mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        try {
            ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPresenter.updateVet(updateVetRequest);
        return super.onOptionsItemSelected(item);
    }


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

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onSuccess(Vet vet) {
        stopProgress();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mClinicNameEdit, errorMessage, Snackbar.LENGTH_LONG).show();
        stopProgress();
    }

    @Override
    public void onPetRemoveSuccess() {
        stopProgress();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void setPresenter(EditVetContract.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {

        final OkCancelDialogFragment okCancelDialogFragment = new OkCancelDialogFragment().newInstance(getString(R.string.areYouSure), getString(R.string.remove_vet_message), getString(R.string.action_remove), getString(R.string.cancelTextOnDialog));
        okCancelDialogFragment.show(((AbstractActivity) getActivity()).getSupportFragmentManager());
        okCancelDialogFragment.setPositiveListener(new BaseDialogFragment.DialogButtonsListener() {
            @Override
            public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                RemoveVetRequest removeVetRequest = new RemoveVetRequest(mVetId, mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                try {
                    ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPresenter.removeVet(removeVetRequest);

            }
        });
        okCancelDialogFragment.setNegativeListener(new BaseDialogFragment.DialogButtonsListener() {
            @Override
            public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                okCancelDialogFragment.dismiss();
            }
        });

    }

    private void stopProgress() {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
