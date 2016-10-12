package com.petmeds1800.ui.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.petmeds1800.R;

/**
 * Created by pooja on 10/12/2016.
 */
public class OkCancelDialogFragment extends BaseDialogFragment {

    public static final String TAG = BaseDialogFragment.class.getSimpleName();

    public static OkCancelDialogFragment newInstance(String message,String title) {
        OkCancelDialogFragment fragment = new OkCancelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SAVED_MESSAGE, message);
        bundle.putString(SAVED_TITLE, title);
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

        if(data.containsKey(SAVED_TITLE)){
            mTitle=data.getString(SAVED_TITLE);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!mMessage.equals(DEFAULT_VALUE)) {

            builder.setMessage(mMessage);
            builder.setTitle(mTitle);

            builder.setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mPositiveListener != null) {
                        mPositiveListener.onDialogButtonClick(OkCancelDialogFragment.this, mPositive);
                    }
                }
            });

            builder.setNegativeButton(R.string.dialog_cancel_button,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mNegativeListener != null) {
                        mNegativeListener.onDialogButtonClick(OkCancelDialogFragment.this, mNegative);
                    }
                }
            });
        }
        return builder.create();
    }
}

