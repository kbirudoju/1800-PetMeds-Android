package com.petmeds1800.ui.fragments.dialog;

import com.petmeds1800.R;
import com.petmeds1800.ui.pets.support.CustomValuePicker;

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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/24/2016.
 */
public class CommonDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String VALUE = "value";

    public static final String TITLE = "title";

    public static final String REQUEST_CODE = "requestCode";

    public static final String DEFAULT_VALUE_CODE = "defaultValue";

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

    private static final String TAG = CommonDialogFragment.class.getSimpleName();

    private ValueSelectedListener valueSetListener;

    private int mRequestCode;

    private int mDefaultValue;


    public static CommonDialogFragment newInstance(String[] mValue, String title, int requestCode, int defaultValue) {
        CommonDialogFragment f = new CommonDialogFragment();
        // Supply value input as an argument.
        Bundle args = new Bundle();
        args.putStringArray(VALUE, mValue);
        args.putString(TITLE, title);
        args.putInt(REQUEST_CODE, requestCode);
        args.putInt(DEFAULT_VALUE_CODE, defaultValue);
        f.setArguments(args);
        return f;
    }

    public static CommonDialogFragment newInstance(String[] mValue, String title, int requestCode) {
        CommonDialogFragment f = new CommonDialogFragment();

        // Supply value input as an argument.
        Bundle args = new Bundle();
        args.putStringArray(VALUE, mValue);
        args.putString(TITLE, title);
        args.putInt(REQUEST_CODE, requestCode);
        f.setArguments(args);

        return f;
    }

    public static CommonDialogFragment newInstance(HashMap<String, String> mValue, String title, int requestCode) {
        CommonDialogFragment f = new CommonDialogFragment();

        // Get array of values  input as an argument.
        Bundle args = new Bundle();
        args.putStringArray(VALUE, mValue.values().toArray(new String[mValue.size()]));
        args.putString(TITLE, title);
        args.putInt(REQUEST_CODE, requestCode);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrStrValue = getArguments().getStringArray(VALUE);
        mTitle = getArguments().getString(TITLE);
        mRequestCode = getArguments().getInt(REQUEST_CODE);
        mDefaultValue = getArguments().getInt(DEFAULT_VALUE_CODE);

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

    private void setUp() {
        mAgePicker.setValues(mArrStrValue, mDefaultValue);
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
                valueSetListener.onValueSelected(selected, mRequestCode);
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

        void onValueSelected(String value, int requestCode);

    }

    public void setValueSetListener(ValueSelectedListener valueSetListener) {
        this.valueSetListener = valueSetListener;
    }
}
