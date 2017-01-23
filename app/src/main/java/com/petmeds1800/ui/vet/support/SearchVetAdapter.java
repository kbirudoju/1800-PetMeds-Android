package com.petmeds1800.ui.vet.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.VetList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/18/2016.
 */
public class SearchVetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private ArrayList<VetList> mVetList;
    private Context mContext;
    View.OnClickListener onClickListener;

    public SearchVetAdapter(Context mContext,View.OnClickListener onClickListener) {

        this.mContext = mContext;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;
            int resource = R.layout.view_find_vet_row;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new VetInfoViewHolder(v);
               v.setOnClickListener(onClickListener);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final VetInfoViewHolder vetViewHolder = (VetInfoViewHolder) holder;
        final VetList vetInfo = (VetList) getItemAt(position);
        vetViewHolder.clinic_name_label.setText(vetInfo.getClinic());
        vetViewHolder.address_first_label.setText(vetInfo.getAddress());
        vetViewHolder.address_second_label.setText(vetInfo.getCity()+","+vetInfo.getState()+" "+vetInfo.getZip());
        vetViewHolder.distancelabel.setText(vetInfo.getDistanceFromZip() +" "+mContext.getString(R.string.miles_txt));
    }

    @Override
    public int getItemCount() {
        if(mVetList==null){
            return  0;
        }else{
            return mVetList.size();
        }

    }

    public void setData(ArrayList<VetList> vetList) {
        this.mVetList = vetList;
        notifyDataSetChanged();
    }

    public VetList getItemAt(int position) {
        return mVetList.get(position);
    }

    public static class VetInfoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.addressLine2_label)
        TextView address_second_label;
        @BindView(R.id.clinicNameLabel)
        TextView clinic_name_label;
        @BindView(R.id.addressLine1_label)
        TextView address_first_label;
        @BindView(R.id.distanceLabel)
        TextView distancelabel;




        public VetInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
