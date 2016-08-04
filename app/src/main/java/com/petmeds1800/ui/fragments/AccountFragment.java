package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.ui.orders.MyOrderFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 8/2/2016.
 */
public class AccountFragment extends AbstractFragment implements View.OnClickListener {

    @BindView(R.id.myOrder)
    TextView myOrderView;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
        myOrderView.setOnClickListener(this);


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
              replaceAndAddToBackStack(new MyOrderFragment());
            break;
            default:
                break;

        }
    }
}
