package com.petmeds1800.ui.orders.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import com.petmeds1800.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommerceItems;
import com.petmeds1800.model.entities.OrderDetailHeader;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.model.entities.PaymentGroup;
import com.petmeds1800.model.entities.ShippingGroup;
import com.petmeds1800.model.entities.WebViewHeader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/19/2016.
 */
public class OrderDetailAdapter extends CustomOrderDetailRecyclerAdapter {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_PRODUCT = 1;
    public static final int VIEW_TYPE_SHIPPING = 2;
    public static final int VIEW_TYPE_PAYMENT = 3;
    public static final int VIEW_TYPE_FIXED = 4;
    public static final int VIEW_TYPE_INFO = 5;

    private OrderList orderList;
    private Context context;
    private View.OnClickListener listener;

    public OrderDetailAdapter(Context context, OrderList orderList) {
        super(context);
        this.orderList = orderList;
        this.context = context;
    }

    public OrderDetailAdapter(Context context, OrderList orderList, View.OnClickListener listener) {
        super(context);
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }

   /* public void setIsDefaultLayout(boolean isDefaultLayout) {
        this.isDefaultLayout = isDefaultLayout;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                int resource = R.layout.view_order_detail_header_row;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resource, parent, false);
                viewHolder = new HeaderViewHolder(v);
                break;

            case VIEW_TYPE_PRODUCT:
                int resourceProductSection = R.layout.view_order_detail_product_row;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resourceProductSection, parent, false);
                viewHolder = new ProductViewHolder(v);
                break;

            case VIEW_TYPE_PAYMENT:
                int resourcePaymentSection = R.layout.view_order_detail_payment_row;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resourcePaymentSection, parent, false);
                viewHolder = new PaymentViewHolder(v);
                break;

            case VIEW_TYPE_SHIPPING:
                int resourceShippingSection = R.layout.view_order_detail_shipping_row;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resourceShippingSection, parent, false);
                viewHolder = new ShippingViewHolder(v);
                break;

            case VIEW_TYPE_INFO:
                int resourceInfoSection = R.layout.view_order_detail_info_row;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resourceInfoSection, parent, false);
                viewHolder = new OrderInfoViewHolder(v);
                break;

            case VIEW_TYPE_FIXED:
                int resourceTopView = R.layout.view_order_detail_webview_row;
                v = LayoutInflater.from(parent.getContext())
                        .inflate(resourceTopView, parent, false);
                viewHolder = new WebViewHolder(v);
                v.setOnClickListener(listener);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Log.d("position", position + ">>" + viewType);
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                OrderDetailHeader header = (OrderDetailHeader) getItemAt(position);
                headerHolder.headerLabel.setText(header.getHeader());
                break;

            case VIEW_TYPE_PRODUCT:
                final ProductViewHolder productHolder = (ProductViewHolder) holder;
                CommerceItems commerceItem = (CommerceItems) getItemAt(position);
                productHolder.priceLabel.setText("$" + commerceItem.getAmount());
                productHolder.productNameLabel.setText(commerceItem.getProductName());
                productHolder.quantityLabel.setText(context.getString(R.string.quantity_txt) + commerceItem.getQuantity());
                productHolder.productDescLabel.setText(commerceItem.getSkuName());
                Glide.with(context).load(context.getString(R.string.server_endpoint) + commerceItem.getSkuImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(productHolder.productImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        productHolder.productImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
                if (commerceItem.getPetName() != null && !commerceItem.getPetName().isEmpty()) {
                    productHolder.petNameLabel.setText(context.getString(R.string.pet_txt) + commerceItem.getPetName());

                } else {
                    productHolder.petNameLabel.setVisibility(View.GONE);
                }

                if (commerceItem.getVetName() != null && !commerceItem.getVetName().isEmpty()) {
                    productHolder.vetNameLabel.setText(context.getString(R.string.vet_txt) + commerceItem.getVetName());

                } else {
                    productHolder.vetNameLabel.setVisibility(View.GONE);
                }
                break;

            case VIEW_TYPE_SHIPPING:
                ShippingViewHolder shippingHolder = (ShippingViewHolder) holder;
                ShippingGroup shippingDetail = (ShippingGroup) getItemAt(position);
                shippingHolder.shippingMethodLabel.setText(shippingDetail.getShippingMethod());
                if (shippingDetail.getAddress2() != null && !shippingDetail.getAddress2().isEmpty()) {
                    shippingHolder.shippingAddressLabel.setText(shippingDetail.getAddress1() + "," + shippingDetail.getAddress2());
                } else {
                    shippingHolder.shippingAddressLabel.setText(shippingDetail.getAddress1());
                }
                break;

            case VIEW_TYPE_FIXED:
                WebViewHolder webViewHolder = (WebViewHolder) holder;
                WebViewHeader webViewHeader = (WebViewHeader) getItemAt(position);
                webViewHolder.webViewHeaderLabel.setText(webViewHeader.getWebviewHeader());
                break;

            case VIEW_TYPE_PAYMENT:
                PaymentViewHolder paymentViewHolder = (PaymentViewHolder) holder;
                PaymentGroup paymentInfo = (PaymentGroup) getItemAt(position);
                if (paymentInfo.getAddress2() != null && !paymentInfo.getAddress2().isEmpty()) {
                    paymentViewHolder.billingAddressLabel.setText(paymentInfo.getAddress1() + "," + paymentInfo.getAddress2());
                } else {
                    paymentViewHolder.billingAddressLabel.setText(paymentInfo.getAddress1());
                }
                paymentViewHolder.paymentMethodLabel.setText(paymentInfo.getPaymentMethod());
                break;

            case VIEW_TYPE_INFO:
                OrderInfoViewHolder orderInfoViewHolder = (OrderInfoViewHolder) holder;
                OrderList orderInfo = (OrderList) getItemAt(position);
                orderInfoViewHolder.orderNoLabel.setText(orderInfo.getDisplayOrderId());
                orderInfoViewHolder.orderDateLabel.setText(orderInfo.getSubmittedDate());
                orderInfoViewHolder.orderShipToLabel.setText(orderInfo.getShipTo());
                orderInfoViewHolder.orderTotalLabel.setText("$" + String.valueOf(orderInfo.getTotal()));
                orderInfoViewHolder.orderStatusLabel.setText(orderInfo.getStatus());


                if (orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_processing))) {
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_shipping, 0, 0, 0);
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.yellow_rounded_button);
                } else if (orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_cancelled))) {
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.red_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_cancelled, 0, 0, 0);

                } else if(orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_delivered))){
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.green_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.i_c_n_status_ic_status_completed, 0, 0, 0);
                }else if(orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_shipping))){
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_shipping, 0, 0, 0);
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.yellow_rounded_button);
                }else if(orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_OrderGone))){
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.green_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.i_c_n_status_ic_status_completed, 0, 0, 0);

                }else if(orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_notavetph))){
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.red_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_cancelled, 0, 0, 0);

                }else if(orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_rx))){
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.red_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_cancelled, 0, 0, 0);

                }else if(orderInfo.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_unknown))){
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.red_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_cancelled, 0, 0, 0);

                }
                else {
                    orderInfoViewHolder.orderStatusLabel.setBackgroundResource(R.drawable.green_rounded_button);
                    orderInfoViewHolder.orderStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.i_c_n_status_ic_status_completed, 0, 0, 0);

                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemAt(position) instanceof CommerceItems)
            return VIEW_TYPE_PRODUCT;
        if (getItemAt(position) instanceof ShippingGroup)
            return VIEW_TYPE_SHIPPING;
        if (getItemAt(position) instanceof PaymentGroup)
            return VIEW_TYPE_PAYMENT;
        if (getItemAt(position) instanceof OrderList)
            return VIEW_TYPE_INFO;
        if (getItemAt(position) instanceof OrderDetailHeader)
            return VIEW_TYPE_HEADER;
        if (getItemAt(position) instanceof WebViewHeader)
            return VIEW_TYPE_FIXED;


        return -1;

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header_label)
        TextView headerLabel;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class WebViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.webview_header_label)
        TextView webViewHeaderLabel;

        public WebViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_image)
        ImageView productImage;
        @BindView(R.id.product_price_label)
        TextView priceLabel;
        @BindView(R.id.product_name_label)
        TextView productNameLabel;
        @BindView(R.id.quantity_label)
        TextView quantityLabel;
        @BindView(R.id.pet_name_label)
        TextView petNameLabel;
        @BindView(R.id.vet_name_label)
        TextView vetNameLabel;
        @BindView(R.id.item_description)
        TextView productDescLabel;


        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ShippingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shipping_method_label)
        TextView shippingMethodLabel;

        @BindView(R.id.shipping_address_label)
        TextView shippingAddressLabel;

        public ShippingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class OrderInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_no_label)
        TextView orderNoLabel;

        @BindView(R.id.order_date_label)
        TextView orderDateLabel;

        @BindView(R.id.order_total_label)
        TextView orderTotalLabel;

        @BindView(R.id.ship_to_label)
        TextView orderShipToLabel;

        @BindView(R.id.status_label)
        TextView orderStatusLabel;

        public OrderInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.payment_method_label)
        TextView paymentMethodLabel;

        @BindView(R.id.billing_address_label)
        TextView billingAddressLabel;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
       /* if (isDefaultLayout) {
            return 1;
        } else {*/
        return super.getItemCount();
        //  }
    }
}

