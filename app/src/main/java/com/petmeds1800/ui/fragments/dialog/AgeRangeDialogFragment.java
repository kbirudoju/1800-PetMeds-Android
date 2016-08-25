package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.ui.pets.support.CustomValuePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/24/2016.
 */
public class AgeRangeDialogFragment extends DialogFragment implements View.OnClickListener{
    @BindView(R.id.ok_button)
    Button mOkButton;
    @BindView(R.id.cancel_button)
    Button mCancelButton;
    @BindView(R.id.valuePicker)
    CustomValuePicker mAgePicker;
    String[] mArrStrValue;
    @BindView(R.id.title_label)
    TextView titleLabel;
    String mTitle;
    private static final String TAG = AgeRangeDialogFragment.class.getSimpleName();

    private ValueSelectedListener valueSetListener;
    public static AgeRangeDialogFragment newInstance(String[] mValue, String title) {
        AgeRangeDialogFragment f = new AgeRangeDialogFragment();

        // Supply value input as an argument.
        Bundle args = new Bundle();
        args.putStringArray("value",mValue);
        args.putString("title",title);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrStrValue = getArguments().getStringArray("value");
        mTitle=getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_pet_age, container, false);
        ButterKnife.bind(this, rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        setUp();
    }

    private void setUp(){
        mAgePicker.setValues(mArrStrValue);
        titleLabel.setText(mTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.ok_button:
                String selected = mArrStrValue[mAgePicker.getValue()];
                valueSetListener.onValueSelected(selected);
                dismiss();
                break;
        }
    }
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        final Fragment fragment = getTargetFragment();
        if (fragment != null && fragment instanceof ValueSelectedListener) {
            valueSetListener = (ValueSelectedListener) fragment;
        }
        if (getActivity() instanceof ValueSelectedListener) {
            valueSetListener = (ValueSelectedListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        valueSetListener = null;
    }
    public void show(final FragmentManager manager) {
        manager.beginTransaction().add(this, TAG).commitAllowingStateLoss();
    }

    public interface ValueSelectedListener {
        void onValueSelected(String value);

    }

    public void setValueSetListener(ValueSelectedListener valueSetListener) {
        this.valueSetListener = valueSetListener;
    }
}
