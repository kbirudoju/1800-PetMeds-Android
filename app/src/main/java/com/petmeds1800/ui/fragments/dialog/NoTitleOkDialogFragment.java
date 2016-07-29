package com.petmeds1800.ui.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.petmeds1800.R;

/**
 * Created by laetitia on 1/6/16.
 */
public class NoTitleOkDialogFragment extends BaseDialogFragment {

    public static final String TAG = BaseDialogFragment.class.getSimpleName();

    public static NoTitleOkDialogFragment newInstance(String message) {
        NoTitleOkDialogFragment fragment = new NoTitleOkDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SAVED_MESSAGE, message);
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

        if (data.containsKey(SAVED_MESSAGE)) {
            mMessage = data.getString(SAVED_MESSAGE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!mMessage.equals(DEFAULT_VALUE)) {

            builder.setMessage(mMessage);

            builder.setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mPositiveListener != null) {
                        mPositiveListener.onDialogButtonClick(NoTitleOkDialogFragment.this, mPositive);
                    }
                }
            });
        }
        return builder.create();
    }
}
