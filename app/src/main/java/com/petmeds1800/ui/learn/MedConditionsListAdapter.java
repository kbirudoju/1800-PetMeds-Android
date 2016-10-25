package com.petmeds1800.ui.learn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.PetEducationCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 10/20/2016.
 */

public class MedConditionsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PetEducationCategory> mMedConditionsList;

    private OnItemClickListener mOnItemClickListener;

    public MedConditionsListAdapter(List<PetEducationCategory> mMedConditionsList) {
        this.mMedConditionsList = mMedConditionsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item_med_condition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MedConditionsListAdapter.ViewHolder viewHolder = (MedConditionsListAdapter.ViewHolder) holder;
        if (viewHolder != null) {
            final PetEducationCategory medCondition = mMedConditionsList.get(position);
            viewHolder.medConditionName.setText(medCondition.getCategoryName());
        }
    }

    @Override
    public int getItemCount() {
        return mMedConditionsList != null ? mMedConditionsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txv_med_condition)
        TextView medConditionName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
}
