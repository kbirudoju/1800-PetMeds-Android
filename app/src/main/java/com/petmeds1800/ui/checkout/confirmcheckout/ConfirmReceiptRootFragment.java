package com.petmeds1800.ui.checkout.confirmcheckout;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.Constants;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Sdixit on 10-10-2016.
 */

public class ConfirmReceiptRootFragment extends AbstractFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static ConfirmReceiptRootFragment newInstance(CommitOrderResponse commitOrderResponse) {
        ConfirmReceiptRootFragment f = new ConfirmReceiptRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.CONFIRMATION_ORDER_RESPONSE,commitOrderResponse);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_confirm_receipt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
