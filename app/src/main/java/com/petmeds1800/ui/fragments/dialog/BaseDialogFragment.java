package com.petmeds1800.ui.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by laetitia on 1/6/16.
 */
public class BaseDialogFragment extends DialogFragment {

    public static final String TAG = BaseDialogFragment.class.getSimpleName();

    protected static final String SAVED_TITLE = "savedTitle";
    protected static final String SAVED_MESSAGE = "savedMessage";
    protected static final String SAVED_POSITIVE = "savedPositive";
    protected static final String SAVED_NEGATIVE = "saveNegative";
    protected static final String DEFAULT_VALUE = "";

    protected String mTitle = DEFAULT_VALUE;
    protected String mMessage = DEFAULT_VALUE;
    protected String mPositive = DEFAULT_VALUE;
    protected String mNegative = DEFAULT_VALUE;

    protected DialogButtonsListener mPositiveListener;
    protected DialogButtonsListener mNegativeListener;

    public static BaseDialogFragment newInstance(String title, String message, String positiveButton, String negativeButton) {
        BaseDialogFragment fragment = new BaseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SAVED_TITLE, title);
        bundle.putString(SAVED_MESSAGE, message);
        bundle.putString(SAVED_POSITIVE, positiveButton);
        bundle.putString(SAVED_NEGATIVE, negativeButton);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle data;
        if (savedInstanceState != null) {
            data = savedInstanceState;
        } else {
            data = getArguments();
        }

        if (data.containsKey(SAVED_TITLE)) {
            mTitle = data.getString(SAVED_TITLE);
        }

        if (data.containsKey(SAVED_MESSAGE)) {
            mMessage = data.getString(SAVED_MESSAGE);
        }

        if (data.containsKey(SAVED_POSITIVE)) {
            mPositive = data.getString(SAVED_POSITIVE);
        }

        if (data.containsKey(SAVED_NEGATIVE)) {
            mNegative = data.getString(SAVED_NEGATIVE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!mMessage.equals(DEFAULT_VALUE)) {

            builder.setTitle(mTitle);
            builder.setMessage(mMessage);

            if (!mPositive.equals(DEFAULT_VALUE)) {
                builder.setPositiveButton(mPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mPositiveListener != null) {
                            mPositiveListener.onDialogButtonClick(BaseDialogFragment.this, mPositive);
                        }
                    }
                });
            }

            if (!mNegative.equals(DEFAULT_VALUE)) {
                builder.setNegativeButton(mNegative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mNegativeListener != null) {
                            mNegativeListener.onDialogButtonClick(BaseDialogFragment.this, mNegative);
                        }
                    }
                });
            }

        }
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!mTitle.equals(DEFAULT_VALUE)) {
            outState.putString(SAVED_TITLE, mTitle);
        }

        if (!mMessage.equals(DEFAULT_VALUE)) {
            outState.putString(SAVED_MESSAGE, mMessage);
        }

        if (!mPositive.equals(DEFAULT_VALUE)) {
            outState.putString(SAVED_POSITIVE, mPositive);
        }

        if (!mNegative.equals(DEFAULT_VALUE)) {
            outState.putString(SAVED_NEGATIVE, mNegative);
        }
    }

    public void show(FragmentManager manager) {
       // show(manager, getDialogTag());
        manager.beginTransaction().add(this,getDialogTag()).commitAllowingStateLoss();
    }

    public void setPositiveListener(DialogButtonsListener listener) {
        mPositiveListener = listener;
    }

    public void setNegativeListener(DialogButtonsListener listener) {
        mNegativeListener = listener;
    }

    public String getDialogTag() {
        return TAG;
    }

    public interface DialogButtonsListener {
        void onDialogButtonClick(DialogFragment dialog, String buttonName);
    }
}
