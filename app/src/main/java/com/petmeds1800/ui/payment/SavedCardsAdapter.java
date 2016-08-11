package com.petmeds1800.ui.payment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.Card;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 8/4/2016.
 */
public class SavedCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    boolean blankView = false;

    View.OnClickListener onClickListener;

    private List<Card> mCards;


    public SavedCardsAdapter(boolean blankView, View.OnClickListener onClickListener) {
        this.blankView = blankView;
        this.onClickListener = onClickListener;

    }

    public void clearData() {
        if ( mCards !=null ) {
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
            final Card myCard = (Card) getItemAt(position);
            orderViewHolder.mCardExpirationLabel.setText(myCard.getmExpiryDate());
            orderViewHolder.mCardTypelabel.setText(myCard.getmCardType());
            orderViewHolder.mCardNumberLabel.setText(myCard.getmCardNumber());

        if(myCard.ismDefaultPayment()){
            orderViewHolder.mIsCardDefaultLabel.setVisibility(View.VISIBLE);
        }
        else {
            orderViewHolder.mIsCardDefaultLabel.setVisibility(View.GONE);
        }
    }

    public Card getItemAt(int position) {
        return mCards.get(position);
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }


    public static class CardItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardType_label)
        TextView mCardTypelabel;
        @BindView(R.id.cardNumber_label)
        TextView mCardNumberLabel;
        @BindView(R.id.cardExpiration_label)
        TextView mCardExpirationLabel;
        @BindView(R.id.isCardSetDefault_label)
        TextView mIsCardDefaultLabel;

        public CardItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

}
