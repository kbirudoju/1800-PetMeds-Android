package com.petmeds1800.ui.checkout.stepfour.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import com.petmeds1800.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.ui.checkout.stepfour.StepFourRootFragment;
import com.petmeds1800.ui.vet.support.VetListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/27/2016.
 */
public class PetVetInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private ArrayList<CommerceItems> commerceItems;
    private Context mContext;
    private View.OnClickListener listener;
    private CompoundButton.OnCheckedChangeListener checkListener;
    Fragment mFragment;
    private boolean isShowPrescriptionView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate (R.layout.view_pet_vet_info_header, parent, false);
            return new HeaderViewHolder (v);
        } else if(viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.view_pet_vet_info_footer, parent, false);
            RecyclerView.ViewHolder footerViewHolder=new VetListAdapter.FooterViewHolder(v);
            Switch mailOptionSwitch=(Switch)v.findViewById(R.id.mail_option_switch);
            mailOptionSwitch.setOnCheckedChangeListener(checkListener);
            return new FooterViewHolder (v);
        } else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.view_pet_vet_info_item, parent, false);
            RecyclerView.ViewHolder viewHolder= new ItemViewHolder(v);
            EditText petNameView=(EditText)v.findViewById(R.id.pet_name_edit);
            EditText vetNameView=(EditText)v.findViewById(R.id.vet_name_edit);
            petNameView.setOnClickListener(listener);
            Log.d("tag set",viewHolder.getAdapterPosition()+">>>>");
            //  vetNameView.setTag(viewHolder.getAdapterPosition());
            vetNameView.setOnClickListener(listener);

            return viewHolder;
        }
        return null;
    }

    public PetVetInfoAdapter(Context context,View.OnClickListener listener,CompoundButton.OnCheckedChangeListener checkListener,Fragment fragment) {
        this.mContext = context;
        this.listener = listener;
        this.checkListener=checkListener;
        this.mFragment=fragment;
    }

    public void setData( ArrayList<CommerceItems> commerceItems) {
        this.commerceItems = commerceItems;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        } else if(holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            if(isShowPrescriptionView){
                footerHolder.prescriptionView.setVisibility(View.VISIBLE);
            }else{
                footerHolder.prescriptionView.setVisibility(View.GONE);
            }

        } else if(holder instanceof ItemViewHolder) {
            CommerceItems commerceItem = getItem (position - 1);
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.productNameLabel.setText(commerceItem.getProductDisplayName());
            Glide.with(mContext).load(mContext.getString(R.string.server_endpoint)+commerceItem.getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(itemViewHolder.productImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    itemViewHolder.productImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            itemViewHolder.quantityLabel.setText(commerceItem.getSkuDisplayName());
            itemViewHolder.petNameEdit.setTag(position);
            itemViewHolder.vetNameEdit.setTag(position);
            StepFourRootFragment fragment = (StepFourRootFragment)mFragment.getParentFragment();

           //Condition to show error if pet/vet is empty
            if(commerceItem.getPetName()!=null && !commerceItem.getPetName().isEmpty()) {
                itemViewHolder.petNameInputLayout.setError(null);
                itemViewHolder.petNameEdit.setText(commerceItem.getPetName());
            }else{
                if(fragment.isEmpty)
                itemViewHolder.petNameInputLayout.setError(mContext.getString(R.string.empty_pet_name));
            }
            if(commerceItem.getVetClinic()!=null && !commerceItem.getVetClinic().isEmpty()){
                itemViewHolder.vetNameInputLayout.setError(null);
                itemViewHolder.vetNameEdit.setText(commerceItem.getVetClinic());

            }else{
                if(fragment.isEmpty) {
                    itemViewHolder.vetNameInputLayout.setError(mContext.getString(R.string.empty_vet_name));
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        if(commerceItems==null){
            return 0;
        }else {
            return commerceItems.size() + 2;
        }
    }

    private CommerceItems getItem (int position) {
        return commerceItems.get (position);
    }

    @Override
    public int getItemViewType (int position) {
        if(isPositionHeader (position)) {
            return TYPE_HEADER;
        } else if(isPositionFooter (position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public void setPrescriptionOption(boolean isShowPrescriptionView){
        this.isShowPrescriptionView=isShowPrescriptionView;
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    private boolean isPositionFooter (int position) {
        return position == commerceItems.size () + 1;

    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
          @BindView(R.id.mail_option_switch)
          Switch mailOptionSwitch;
        @BindView(R.id.prescription_detail_view)
        LinearLayout prescriptionView;
        public FooterViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_image)
        ImageView productImage;
        @BindView(R.id.product_name_label)
        TextView productNameLabel;
        @BindView(R.id.quantity_label)
        TextView quantityLabel;

        @BindView(R.id.pet_name_edit)
        EditText petNameEdit;
        @BindView(R.id.vet_name_edit)
        EditText vetNameEdit;

        @BindView(R.id.petNameInputLayout)
        TextInputLayout petNameInputLayout;

        @BindView(R.id.vetNameInputLayout)
        TextInputLayout vetNameInputLayout;

        public ItemViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
