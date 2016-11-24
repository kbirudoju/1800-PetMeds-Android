package com.petmeds1800.ui.vet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.AddVetRequest;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.checkout.AddNewEntityActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/3/2016.
 */
public class CantFindVetFragment extends AbstractFragment implements CantFindVetContract.View {


    @BindView(R.id.clinicNameInputLayout)
    TextInputLayout mClinicNameInputLayout;


    @BindView(R.id.inputFieldInputLayout)
    TextInputLayout mInputFieldInputLayout;


    @BindView(R.id.phoneInputLayout)
    TextInputLayout mPhoneInputLayout;

    @BindView(R.id.clinic_name_edit)
    EditText mClinicNameEdit;

    @BindView(R.id.input_field_edit)
    EditText mInputFieldEdit;

    @BindView(R.id.phone_number_edit)
    EditText mPhoneNumberEdit;

    private CantFindVetContract.Presenter mPresenter;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private Activity mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cant_find_vet, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mPresenter=new CantFindVetPresenter(this);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.cannot_find_vet_txt));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).enableBackButton();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_settings, menu);
        MenuItem editMenuItem = menu.findItem(R.id.action_edit);
        MenuItem doneMenuItem = menu.findItem(R.id.action_done);
        editMenuItem.setVisible(false);
        doneMenuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id == R.id.action_done) {
            boolean isValidPetName;
            boolean isValidClinicName;
            boolean isValidPhoneNumber;

            isValidPetName = checkAndShowError(mInputFieldEdit, mInputFieldInputLayout, R.string.empty_pet_name);
            isValidClinicName = checkAndShowError(mClinicNameEdit, mClinicNameInputLayout, R.string.empty_vet_name);
            isValidPhoneNumber = checkAndShowError(mPhoneNumberEdit, mPhoneInputLayout, R.string.error_validPhone);


            if (isValidPetName ||
                    isValidClinicName ||
                    isValidPhoneNumber
                   ) {
               // Utils.displayCrouton(getActivity(), getString(R.string.errorMsgForEmail), mContainerLayout);
                return super.onOptionsItemSelected(item);
            }
                try {
                    ((AbstractActivity)getActivity()).startLoadingGif(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            AddVetRequest addVetRequest=new AddVetRequest(mInputFieldEdit.getText().toString(),mClinicNameEdit.getText().toString(),mPhoneNumberEdit.getText().toString()
            ,mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
            mPresenter.addVetData(addVetRequest);
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

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(Vet vet) {
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();

        if(mCallback instanceof AddNewEntityActivity) {
            ((AddNewEntityActivity) mCallback).setVet(vet);
        }else if(mCallback instanceof HomeActivity){
            VetListFragment vetListFragment= (VetListFragment) ((HomeActivity) mCallback).getSupportFragmentManager().findFragmentByTag(VetListFragment.class.getName());
            vetListFragment.setVet(vet);
        }
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mClinicNameEdit, errorMessage, Snackbar.LENGTH_LONG).show();
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(CantFindVetContract.Presenter presenter) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (context instanceof AddNewEntityActivity) {
                mCallback = (AddNewEntityActivity) context;
            }else if(context instanceof HomeActivity){
                mCallback=(HomeActivity) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    );
        }

    }
}
