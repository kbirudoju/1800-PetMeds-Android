package com.petmeds1800.ui.address;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.Address;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 8/4/2016.
 */
public class SavedAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    boolean blankView = false;

    SavedAddressListFragment mSavedAddressListFragment;

    private List<Address> mAddresses;


    public SavedAddressAdapter(boolean blankView, SavedAddressListFragment savedAddressListFragment, Context context) {
        this.blankView = blankView;
        this.mSavedAddressListFragment = savedAddressListFragment;
        this.mContext = context;

    }

    public void clearData() {
        if ( mAddresses !=null ) {
          mAddresses.clear();
            notifyDataSetChanged();
        }
    }


    public void setData(List<Address> myCards) {
        this.mAddresses = myCards;
        notifyDataSetChanged();
    }

    public void addData(List<Address> myCard) {
        //write code to add add which is fetched on load more
            notifyDataSetChanged();
        }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;

        int resource = R.layout.view_address_list;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new AddressItemViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("position is", position + ">>>>" + mAddresses.size());

            AddressItemViewHolder orderViewHolder = (AddressItemViewHolder) holder;
            final Address myAddress = getItemAt(position);

//            String expirationText = String.format(mContext.getString(R.string.cardExpirationValue),myCard.getExpirationMonth(),myCard.getExpirationYear());
            //TODO Need to check if 2 separate firstname and lastname fields are still required
            orderViewHolder.mNameOnAddressLabel.setText(myAddress.getFirstName() + " " + myAddress.getLastName());
            orderViewHolder.mAddressLine1Label.setText(myAddress.getAddress1());
            orderViewHolder.mAddressLine2Label.setText(myAddress.getAddress2());
            orderViewHolder.mCountryLabel.setText(myAddress.getCountry());
            orderViewHolder.mPhoneNumberLabel.setText(myAddress.getPhoneNumber());

            orderViewHolder.mEditAddressButton.setTag(position);
            orderViewHolder.mEditAddressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSavedAddressListFragment.startAddressUpdate(getItemAt((Integer) v.getTag()));
                }
            });

        //TODO need to change this string comparision to a boolean value once backend do the change
        if(myAddress.getIsDefaultBillingAddress().equals("true")){
            orderViewHolder.mIsdefaultShippingAddress.setVisibility(View.VISIBLE);
        }
        else {
            orderViewHolder.mIsdefaultShippingAddress.setVisibility(View.GONE);
        }
    }

    public Address getItemAt(int position) {
        return mAddresses.get(position);
    }

    @Override
    public int getItemCount() {
        if(mAddresses == null){
            return 0;
        }
        else {
            return mAddresses.size();
        }

    }


    public static class AddressItemViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.editAddress_button)
        Button mEditAddressButton;

        public AddressItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

}
