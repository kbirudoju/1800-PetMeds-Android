package com.petmeds1800.ui.orders.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.OrderList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/4/2016.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    boolean blankView = false;

    final static int NORMAL_VIEW_TYPE = 1;
    final static int LOADING_VIEW_TYPE = 2;
    View.OnClickListener onClickListener;

    private boolean isFooterEnabled = false;
    private List<OrderList> myOrder;
    private Context mContext;

    public MyOrderAdapter(boolean blankView, Context context, View.OnClickListener onClickListener) {
        this.blankView = blankView;
        this.onClickListener = onClickListener;
        this.mContext = context;

    }


    public void clearData() {
        if (myOrder != null) {
            myOrder.clear();
            notifyDataSetChanged();
        }
    }


    public void setData(List<OrderList> myOrder) {
        this.myOrder = myOrder;
        notifyDataSetChanged();
    }

    public void addData(List<MyOrder> myOrder) {
        //write code to add add which is fetched on load more
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        if (viewType == NORMAL_VIEW_TYPE) {
            int resource = R.layout.view_order_list;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new MyOrderItemViewHolder(v);
            v.setOnClickListener(onClickListener);
        } else {
            int resource = R.layout.view_order_loading;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new ProgressViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("position is", position + ">>>>" + myOrder.size());
        if (holder instanceof ProgressViewHolder) {

        } else {
            final MyOrderItemViewHolder orderViewHolder = (MyOrderItemViewHolder) holder;
            final OrderList myOrder = (OrderList) getItemAt(position);
            if (myOrder != null) {
                orderViewHolder.tv_order_date.setText(myOrder.getSubmittedDate());
                orderViewHolder.tv_order_number.setText("#" + myOrder.getOrderId());
                orderViewHolder.tv_order_status.setText(myOrder.getStatus());
                //temporary hardcoded value to check layout, it will be changed after confirmation from backend
                if (myOrder.getStatus() != null && myOrder.getStatus().equalsIgnoreCase("PROCESSING")) {
                    orderViewHolder.tv_order_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_shipping, 0, 0, 0);
                    orderViewHolder.tv_order_status.setBackgroundResource(R.drawable.yellow_rounded_button);
                } else if (myOrder.getStatus() != null && myOrder.getStatus().equalsIgnoreCase("Cancelled")) {
                    orderViewHolder.tv_order_status.setBackgroundResource(R.drawable.red_rounded_button);
                    orderViewHolder.tv_order_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_cancelled, 0, 0, 0);

                } else {
                    orderViewHolder.tv_order_status.setBackgroundResource(R.drawable.green_rounded_button);
                }
            }
            //hardcoded the position value as not sure what we have to do in case of multiple CommerceItems

            if (myOrder.getCommerceItems().size() > 0) {
                if (myOrder.getCommerceItems().size() == 1) {
                    orderViewHolder.iv_product_img.setVisibility(View.VISIBLE);
                    orderViewHolder.productCountlabel.setVisibility(View.INVISIBLE);
                    orderViewHolder.productSecondImage.setVisibility(View.INVISIBLE);
                    orderViewHolder.productThirdImage.setVisibility(View.INVISIBLE);
                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + myOrder.getCommerceItems().get(0).getSkuImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(orderViewHolder.iv_product_img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            orderViewHolder.iv_product_img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                } else if (myOrder.getCommerceItems().size() == 2) {
                    orderViewHolder.productThirdImage.setVisibility(View.VISIBLE);
                    orderViewHolder.productSecondImage.setVisibility(View.VISIBLE);
                    orderViewHolder.iv_product_img.setVisibility(View.INVISIBLE);
                    orderViewHolder.productCountlabel.setVisibility(View.INVISIBLE);
                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + myOrder.getCommerceItems().get(0).getSkuImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(orderViewHolder.productThirdImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            orderViewHolder.productThirdImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + myOrder.getCommerceItems().get(1).getSkuImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(orderViewHolder.productSecondImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            orderViewHolder.productSecondImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                } else {
                    orderViewHolder.productCountlabel.setVisibility(View.VISIBLE);

                    Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + myOrder.getCommerceItems().get(0).getSkuImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(orderViewHolder.iv_product_img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            orderViewHolder.iv_product_img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    orderViewHolder.productSecondImage.setVisibility(View.INVISIBLE);
                    orderViewHolder.productThirdImage.setVisibility(View.INVISIBLE);
                    orderViewHolder.productCountlabel.setText("+" + (myOrder.getCommerceItems().size() - 1));
                }
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && (myOrder == null || position >= myOrder.size())) ? LOADING_VIEW_TYPE : NORMAL_VIEW_TYPE;
    }

    public OrderList getItemAt(int position) {
        return myOrder.get(position);
    }

    @Override
    public int getItemCount() {
        if (myOrder == null) {
            if (isFooterEnabled) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (isFooterEnabled) {
                return myOrder.size() + 1;
            } else {
                return myOrder.size();
            }
        }
    }

    public void enableFooter(boolean isEnabled) {
        this.isFooterEnabled = isEnabled;
        notifyDataSetChanged();
    }


    public static class MyOrderItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_number_label)
        TextView tv_order_number;
        @BindView(R.id.order_status_label)
        TextView tv_order_status;
        @BindView(R.id.order_date_label)
        TextView tv_order_date;
        @BindView(R.id.product_image)
        ImageView iv_product_img;
        @BindView(R.id.product_second_image)
        ImageView productSecondImage;
        @BindView(R.id.product_image_three)
        ImageView productThirdImage;
        @BindView(R.id.product_count_label)
        TextView productCountlabel;


        public MyOrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar)
        ProgressBar progressbar;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }


}
