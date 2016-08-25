package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.ui.account.AccountSettingsFragment;
import com.petmeds1800.ui.address.AddAddressFragment;
import com.petmeds1800.ui.orders.MyOrderFragment;
import com.petmeds1800.ui.payment.SavedCardsListFragment;
import com.petmeds1800.ui.pets.PetListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/2/2016.
 */
public class AccountFragment extends AbstractFragment implements View.OnClickListener {

    @BindView(R.id.myOrder)
    TextView myOrderView;

    @BindView(R.id.accountSettings_label)
    TextView mAccountSettings;

    @BindView(R.id.managePayment_label)
    TextView mManagePaymentLabel;

    @BindView(R.id.manageAddress_label)
    TextView mManageAddressLabel;

    @BindView(R.id.my_pets_label)
    TextView mPetsLabel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
        myOrderView.setOnClickListener(this);
        mAccountSettings.setOnClickListener(this);
        mManagePaymentLabel.setOnClickListener(this);
        mManageAddressLabel.setOnClickListener(this);
        mPetsLabel.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account,container,false);
        ButterKnife.bind(this, view);
        return view;

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myOrder:
              replaceAndAddToBackStack(new MyOrderFragment() , MyOrderFragment.class.getName());
            break;

            case R.id.accountSettings_label:
                replaceAndAddToBackStack(new AccountSettingsFragment() , AccountSettingsFragment.class.getName());
                break;

            case R.id.managePayment_label:
                replaceAndAddToBackStack(new SavedCardsListFragment() , SavedCardsListFragment.class.getName());
                break;

            case R.id.manageAddress_label:
                replaceAndAddToBackStack(new AddAddressFragment(), AddAddressFragment.class.getName());
                break;
            case R.id. my_pets_label:
                replaceAndAddToBackStack(new PetListFragment(),PetListFragment.class.getName());

                break;
            default:
                break;

        }
    }
}
