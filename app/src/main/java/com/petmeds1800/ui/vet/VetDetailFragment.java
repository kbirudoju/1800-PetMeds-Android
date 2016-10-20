package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/18/2016.
 */
public class VetDetailFragment extends AbstractFragment {
    private VetList mVetDetail;
    @BindView(R.id.clinicNameLabel)
    TextView mClinicNameLabel;
    @BindView(R.id.addressLine1_label)
    TextView mAddressLine1label;
    @BindView(R.id.addressLine2_label)
    TextView mAddressLine2Label;
    @BindView(R.id.phone_number_label)
    TextView mPhoneNumberLabel;
    @BindView(R.id.distanceLabel)
    TextView mDistanceLabel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_vet_detail,null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVetDetail=(VetList)getArguments().getSerializable("vet_detail");
        if(mVetDetail!=null){
            ((AbstractActivity) getActivity()).setToolBarTitle(getString(R.string.near_vet_txt)+mVetDetail.getZip());
            mClinicNameLabel.setText(mVetDetail.getClinic());
            mAddressLine1label.setText(mVetDetail.getAddress());
            mAddressLine2Label.setText(mVetDetail.getCity()+" , "+mVetDetail.getState());
            mPhoneNumberLabel.setText(getString(R.string.phone_title_txt)+mVetDetail.getPhone());
            mDistanceLabel.setText(mVetDetail.getDistanceFromZip()+" "+getString(R.string.miles_txt)+" "+getString(R.string.from_txt)+" "+mVetDetail.getZip());
        }
    }
}
