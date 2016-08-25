package com.petmeds1800.ui.pets.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.Pets;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/23/2016.
 */
public class PetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Pets> petList;
    private Context mContext;
    View.OnClickListener onClickListener;
    final static int NORMAL_VIEW_TYPE = 1;
    final static int ADD_VIEW_TYPE = 2;
    private boolean isFooterEnabled = true;
    public PetListAdapter(Context context,View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.mContext=context;

    }

    public void setData(List<Pets> petList) {
        this.petList = petList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;

        if (viewType == NORMAL_VIEW_TYPE) {
            int resource = R.layout.view_pet_row;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new PetViewHolder(v);
           // v.setOnClickListener(onClickListener);
        } else {
            int resource = R.layout.view_add_pet_item;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new AddPetViewHolder(v);
            v.setOnClickListener(onClickListener);
        }
        return viewHolder;




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof AddPetViewHolder) {

        } else {
            final PetViewHolder petViewHolder = (PetViewHolder) holder;
            final Pets pets = (Pets) getItemAt(position);
            petViewHolder.petNameLabel.setText(pets.getPetName());

            Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + pets.getPictureURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(petViewHolder.petImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    petViewHolder.petImage.setImageDrawable(circularBitmapDrawable);
                }
            });

        }
    }



    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && (petList == null || position >= petList.size())) ? ADD_VIEW_TYPE : NORMAL_VIEW_TYPE;

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private Pets getItem(int position) {
        return petList.get(position-1);

    }


    public Pets getItemAt(int position) {
        return petList.get(position);
    }

    @Override
    public int getItemCount() {
        if (petList == null ) {
            if (isFooterEnabled) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (isFooterEnabled) {
                return petList.size() + 1;
            } else {
                return petList.size();
            }
        }
    }


    public static class PetViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pet_image)
        ImageView petImage;
        @BindView(R.id.pet_name_label)
        TextView petNameLabel;
        public PetViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public static class AddPetViewHolder extends RecyclerView.ViewHolder {

        public AddPetViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

}
