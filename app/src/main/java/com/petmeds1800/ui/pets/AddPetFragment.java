package com.petmeds1800.ui.pets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.AgeRangeDialogFragment;
import com.petmeds1800.ui.fragments.dialog.GenderDialogFragment;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/23/2016.
 */
public class AddPetFragment extends AbstractFragment implements View.OnClickListener, GenderDialogFragment.GenderSetListener,AgeRangeDialogFragment.ValueSelectedListener {
    @BindView(R.id.pet_gender_edit)
    EditText mPetGenderText;
    @BindView(R.id.pet_birthday_edit)
    EditText mPetBirthdayText;
    @BindView(R.id.pet_age_edit)
    EditText mPetAgeText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_pet,container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPetGenderText.setOnClickListener(this);
        mPetBirthdayText.setOnClickListener(this);
        mPetAgeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pet_gender_edit:
                FragmentManager fm = getFragmentManager();
                GenderDialogFragment dialogFragment = new GenderDialogFragment ();
                dialogFragment.setGenderSetListener(this);
                dialogFragment.show(fm);
                break;
            case R.id. pet_birthday_edit:
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date()).setIndicatorColor(getActivity().getColor(R.color.pattern_blue))
                        .build()
                        .show();
                break;
            case R.id.pet_age_edit:
                FragmentManager fragManager = getFragmentManager();
                AgeRangeDialogFragment ageRangeDialogFragment = AgeRangeDialogFragment.newInstance(getActivity().getResources().getStringArray(R.array.age_range),getActivity().getString(R.string.choose_range_txt));
                ageRangeDialogFragment.setValueSetListener(this);
                ageRangeDialogFragment.show(fragManager);
                break;
        }
    }

    @Override
    public void onGenderSet(String gender) {
        mPetGenderText.setText(gender);
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            mPetBirthdayText.setText(date.toString());
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    @Override
    public void onValueSelected(String value) {
        mPetAgeText.setText(value);
    }
}
