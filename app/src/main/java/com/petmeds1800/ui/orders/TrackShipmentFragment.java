package com.petmeds1800.ui.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/6/2016.
 */
public class TrackShipmentFragment extends AbstractFragment {

    @BindView(R.id.trackingNumber_label)
    TextView mTrackingNumberLabel;

    @BindView(R.id.companyName_label)
    TextView mCompanyNameLabel;

    @BindView(R.id.companyNameView)
    RelativeLayout mCompanyNameView;

    String mTrackingId;
    String mVendorName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_shipment, null);
        ((AbstractActivity) getActivity()).setToolBarTitle(getString(R.string.title_track_shipment));
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrackingId=getArguments().getString("trackingId");
        mVendorName=getArguments().getString("vendorName");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCompanyNameLabel.setText(getString(R.string.track_via_txt)+ mVendorName);
        mTrackingNumberLabel.setText(mTrackingId);
        mCompanyNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle shippingBundle = new Bundle();
                shippingBundle.putString(CommonWebviewFragment.TITLE_KEY,getString(R.string.title_track_shipment));
                shippingBundle.putString(CommonWebviewFragment.URL_KEY,getActivity().getString(R.string.server_endpoint)+"rsTrack.jsp?TrackID="+mTrackingId+"&TrackType="+mVendorName);
                replaceAccountFragmentWithBundle(new CommonWebviewFragment(), shippingBundle);

            }
        });
    }
}
