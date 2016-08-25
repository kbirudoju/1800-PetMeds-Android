package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.petmeds1800.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenderDialogFragment extends DialogFragment implements OnClickListener {

    private static final String TAG = GenderDialogFragment.class.getSimpleName();

    @BindView(R.id.ok_button)
    Button mOkButton;
    @BindView(R.id.cancel_button)
    Button mCancelButton;
    @BindView(R.id.male_rb)
    RadioButton maleRadioButton;
    @BindView(R.id.female_rb)
    RadioButton femaleRadioButton;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;

    private GenderSetListener genderSetListener;
        String selectedGender;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_gender, container, false);
        ButterKnife.bind(this, rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setSelection();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.ok_button:
                String selected = null;
                if (genderSetListener != null)
                    switch (genderRadioGroup.getCheckedRadioButtonId()) {
                        case R.id.male_rb:
                           selected = getString(R.string.male_txt);
                            break;
                        case R.id.female_rb:
                           selected = getString(R.string.female_txt);
                            break;

                    }
                genderSetListener.onGenderSet(selected);
                dismiss();
                break;
        }
    }

  @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        final Fragment fragment = getTargetFragment();
        if (fragment != null && fragment instanceof GenderSetListener) {
            genderSetListener = (GenderSetListener) fragment;
        }
        if (getActivity() instanceof GenderSetListener) {
            genderSetListener = (GenderSetListener) getActivity();
        }
    }

    void setSelection() {
        if (selectedGender == null)
            return;
        if (selectedGender.equals(getString(R.string.male_txt))) {
            maleRadioButton.setChecked(true);
            return;
        }
        if (selectedGender.equals(getString(R.string.female_txt))) {
            femaleRadioButton.setChecked(true);
            return;
        }


    }



    @Override
    public void onDetach() {
        super.onDetach();
        genderSetListener = null;
    }

    public void show(final FragmentManager manager) {
        manager.beginTransaction().add(this, TAG).commitAllowingStateLoss();
    }




    public interface GenderSetListener {
     void onGenderSet(String gender);

 }

    public void setGenderSetListener(GenderSetListener genderSetListener) {
        this.genderSetListener = genderSetListener;
    }
}
