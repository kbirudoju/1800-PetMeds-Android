package com.petmeds1800.ui.checkout.confirmcheckout;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.Constants;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 10-10-2016.
 */

public class ConfirmationReceiptRootFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_confirm_receipt, container, false);
        ButterKnife.bind(this, view);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment_receipt, ConfirmationReceiptFragment.newInstance(
                (CommitOrderResponse) getArguments().getSerializable(Constants.CONFIRMATION_ORDER_RESPONSE)),
                ConfirmationReceiptFragment.class.getSimpleName());
        transaction.commit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            ((HomeActivity)getActivity()).setToolBarTitle(getString(R.string.label_confirmation_and_receipt));
        }
    }

    @OnClick(R.id.btn_start_new_order)
    public void startNewOrder(){
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.scrollViewPager(0,false);
    }
}
