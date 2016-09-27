package com.petmeds1800.ui.address;

import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootFragment;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 8/4/2016.
 */
public class AddressSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    private final int mRequestCode;

    boolean blankView = false;

    AddressSelectionListFragment mAddressSelectionListFragment;

    private List<Address> mAddresses;

    private int mSelectedPosition;


    public AddressSelectionAdapter(boolean blankView, AddressSelectionListFragment mAddressSelectionListFragment,
            Context context, int requestCode) {
        this.blankView = blankView;
        this.mAddressSelectionListFragment = mAddressSelectionListFragment;
        this.mContext = context;
        this.mRequestCode = requestCode;

    }

    public void clearData() {
        if (mAddresses != null) {
            mAddresses.clear();
            notifyDataSetChanged();
        }
    }


    public void setData(List<Address> myCards) {
        this.mAddresses = myCards;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;

        int resource = R.layout.address_selection_list;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new AddressItemViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("position is", position + ">>>>" + mAddresses.size());

        final AddressItemViewHolder orderViewHolder = (AddressItemViewHolder) holder;
        final Address myAddress = getItemAt(position);

//            String expirationText = String.format(mContext.getString(R.string.cardExpirationValue),myCard.getExpirationMonth(),myCard.getExpirationYear());
        orderViewHolder.mNameOnAddressLabel.setText(myAddress.getFirstName() + " " + myAddress.getLastName());
        orderViewHolder.mAddressLine1Label.setText(myAddress.getAddress1());

        if (myAddress.getAddress2() == null || myAddress.getAddress2()
                .isEmpty()) { //addressline2 / APT # is not mandotory
            orderViewHolder.mAddressLine2Label
                    .setText(myAddress.getCity() + ", " + myAddress.getState() + " " + myAddress.getPostalCode());
        } else {
            orderViewHolder.mAddressLine2Label.setText(
                    myAddress.getAddress2() + ", " + myAddress.getCity() + ", " + myAddress.getState() + " " + myAddress
                            .getPostalCode());
        }

        orderViewHolder.mCountryLabel.setText(myAddress.getCountry());
        orderViewHolder.mPhoneNumberLabel
                .setText(String.format(mContext.getString(R.string.phoneNumberInAddress), myAddress.getPhoneNumber()));

        if (position == mSelectedPosition) {
            orderViewHolder.mAddressSelectionRadio.setChecked(true);
            if (this.mRequestCode == StepOneRootFragment.REQUEST_CODE) {
                orderViewHolder.mSelectAddressButton.setVisibility(View.GONE);
            } else {
                orderViewHolder.mSelectAddressButton.setVisibility(View.VISIBLE);
            }
            mAddressSelectionListFragment.forwardAddressToActivity(myAddress, mRequestCode);
        } else {
            orderViewHolder.mAddressSelectionRadio.setChecked(false);
            orderViewHolder.mSelectAddressButton.setVisibility(View.GONE);
        }

        orderViewHolder.mAddressSelectionRadio.setClickable(false);
        orderViewHolder.mAddressContainerLayout.setTag(position);

        orderViewHolder.mSelectAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddressSelectionListFragment.popBackStackImmediate();


            }
        });

        orderViewHolder.mAddressContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPosition = (Integer) v.getTag();
                notifyDataSetChanged();
                if (orderViewHolder.mAddressSelectionRadio.isChecked()) {
                    mAddressSelectionListFragment.forwardAddressToActivity(myAddress, mRequestCode);
                }
            }
        });

        if (myAddress.getIsDefaultShippingAddress()) {
            orderViewHolder.mIsdefaultShippingAddress.setVisibility(View.VISIBLE);
        } else {
            orderViewHolder.mIsdefaultShippingAddress.setVisibility(View.GONE);
        }
    }

    public Address getItemAt(int position) {
        return mAddresses.get(position);
    }

    @Override
    public int getItemCount() {
        if (mAddresses == null) {
            return 0;
        } else {
            return mAddresses.size();
        }

    }


    public static class AddressItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.addressContainer_layout)
        RelativeLayout mAddressContainerLayout;

        @BindView(R.id.addressSelection_radio)
        AppCompatRadioButton mAddressSelectionRadio;

        @BindView(R.id.addressName_label)
        TextView mNameOnAddressLabel;

        @BindView(R.id.addressLine2_label)
        TextView mAddressLine2Label;

        @BindView(R.id.addressLine1_label)
        TextView mAddressLine1Label;

        @BindView(R.id.country_label)
        TextView mCountryLabel;

        @BindView(R.id.phoneNumber_label)
        TextView mPhoneNumberLabel;

        @BindView(R.id.isAddressSetDefault_label)
        TextView mIsdefaultShippingAddress;

        @BindView(R.id.selectAddress_button)
        Button mSelectAddressButton;

        public AddressItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
