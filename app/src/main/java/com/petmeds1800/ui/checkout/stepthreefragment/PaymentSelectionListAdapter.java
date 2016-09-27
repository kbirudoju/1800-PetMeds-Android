package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.R;
import com.petmeds1800.model.Card;
import com.petmeds1800.util.Utils;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sdixit on 27-09-2016.
 */

public class PaymentSelectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private PaymentSelectionListFragment mPaymentSelectionListFragment;

    private Context mContext;

    private int mRequestCode;

    private List<Card> mCards;

    private int mSelectedPosition;

    public PaymentSelectionListAdapter(PaymentSelectionListFragment paymentSelectionListFragment,
            Context context, int requestCode) {

        this.mPaymentSelectionListFragment = paymentSelectionListFragment;
        this.mContext = context;
        this.mRequestCode = requestCode;

    }

    public void clearData() {
        if (mCards != null) {
            mCards.clear();
            notifyDataSetChanged();
        }
    }


    public void setData(List<Card> myCards) {
        this.mCards = myCards;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.payment_selection_list;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new PaymentItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("position is", position + ">>>>" + mCards.size());

        PaymentSelectionListAdapter.PaymentItemViewHolder viewHolder
                = (PaymentSelectionListAdapter.PaymentItemViewHolder) holder;
        final Card myCard = getItemAt(position);

        String expirationText = String.format(mContext.getString(R.string.cardExpirationValue), Utils
                .getShortMonthName(Integer.parseInt(myCard.getExpirationMonth())), myCard.getExpirationYear());

        viewHolder.mCardExpirationLabel.setText(expirationText);
        viewHolder.mCardNameAndNumberLabel.setText(myCard.getCardType() + " : " + myCard.getCardNumber());
        if (myCard.isCardIsDefault()) {
            viewHolder.mIsAddressSetDefaultLabel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mIsAddressSetDefaultLabel.setVisibility(View.GONE);
        }
        if (position == mSelectedPosition) {
            viewHolder.mPaymentSelectionRadio.setChecked(true);
        } else {
            viewHolder.mPaymentSelectionRadio.setChecked(false);
        }

        viewHolder.mPaymentSelectionRadio.setClickable(false);
        viewHolder.mItemContainerLayout.setTag(position);
        viewHolder.mItemContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPosition = (Integer) v.getTag();
                notifyDataSetChanged();

            }
        });
    }

    public Card getItemAt(int position) {
        return mCards.get(position);
    }

    @Override
    public int getItemCount() {
        if (mCards == null) {
            return 0;
        } else {
            return mCards.size();
        }
    }

    public static class PaymentItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.paymentSelection_radio)
        AppCompatRadioButton mPaymentSelectionRadio;

        @BindView(R.id.cardName_and_Number_label)
        TextView mCardNameAndNumberLabel;

        @BindView(R.id.cardExpiration_label)
        TextView mCardExpirationLabel;

        @BindView(R.id.isAddressSetDefault_label)
        TextView mIsAddressSetDefaultLabel;

        @BindView(R.id.itemContainerLayout)
        RelativeLayout mItemContainerLayout;

        public PaymentItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
