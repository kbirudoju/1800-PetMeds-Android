package com.petmeds1800.ui.payment;

import com.petmeds1800.R;
import com.petmeds1800.model.Card;
import com.petmeds1800.util.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 8/4/2016.
 */
public class SavedCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    boolean blankView = false;

    SavedCardsListFragment mSavedCardsListFragment;

    private List<Card> mCards;


    public SavedCardsAdapter(boolean blankView, SavedCardsListFragment savedCardListFragment, Context context) {
        this.blankView = blankView;
        this.mSavedCardsListFragment = savedCardListFragment;
        this.mContext = context;

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

    public void addData(List<Card> myCard) {
        //write code to add add which is fetched on load more
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;

        int resource = R.layout.view_cards_list;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new CardItemViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("position is", position + ">>>>" + mCards.size());

        CardItemViewHolder orderViewHolder = (CardItemViewHolder) holder;
        final Card myCard = getItemAt(position);

        String expirationText = String.format(mContext.getString(R.string.cardExpirationValue), Utils
                .getShortMonthName(Integer.parseInt(myCard.getExpirationMonth())), myCard.getExpirationYear());

        orderViewHolder.mCardExpirationLabel.setText(expirationText);
        orderViewHolder.mCardTypelabel.setText(myCard.getCardType());
        orderViewHolder.mCardNumberLabel.setText(myCard.getCardNumber());
        orderViewHolder.mEditCardButton.setTag(position);
        orderViewHolder.mEditCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSavedCardsListFragment.startCardUpdate(getItemAt((Integer) v.getTag()));
            }
        });

        if (myCard.isCardIsDefault()) {
            orderViewHolder.mIsCardDefaultLabel.setVisibility(View.VISIBLE);
        } else {
            orderViewHolder.mIsCardDefaultLabel.setVisibility(View.GONE);
        }
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


    public static class CardItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameOnCard_label)
        TextView mNameOnCardlabel;

        @BindView(R.id.cardType_label)
        TextView mCardTypelabel;

        @BindView(R.id.cardNumber_label)
        TextView mCardNumberLabel;

        @BindView(R.id.cardExpiration_label)
        TextView mCardExpirationLabel;

        @BindView(R.id.isCardSetDefault_label)
        TextView mIsCardDefaultLabel;

        @BindView(R.id.editCard_button)
        TextView mEditCardButton;

        public CardItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
