package com.petmeds1800.ui.orders.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommerceItems;
import com.petmeds1800.model.entities.OrderDetailHeader;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.model.entities.PaymentGroup;
import com.petmeds1800.model.entities.ShippingGroup;
import com.petmeds1800.model.entities.WebViewHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pooja on 8/17/2016.
 */
public abstract class CustomOrderDetailRecyclerAdapter extends RecyclerView.Adapter{
    private List<Object> mData;
    private boolean mNotifyOnChange;
    private Context context;
    public static final int CANCEL_ORDER_ROW_ID=0;
    public static final int REORDER_ENTIRE_ORDER_ROW_ID=1;
    public static final int REORDER_ITEM_ROW_ID=2;
    public static final int REVIEW_ROW_ID=3;
    public static final int TRACK_ROW_ID=4;

    public CustomOrderDetailRecyclerAdapter(Context context) {
        mData = new ArrayList<Object>();
        mNotifyOnChange=true;
        this.context=context;
    }

    public List<Object> setData(OrderList orderList){
        //Add orderdetail header
        mData.add(new OrderDetailHeader(context.getString(R.string.order_detail_header)));
        //Add allCommerce Item in list
        int commerceItemSize= orderList.getCommerceItems().size();
        mData.add(orderList);
        //Add webview Items
        //Cancel row will be shown only if iscancelleble is true
        if(orderList.getIsCancellable().equals("true"))
        mData.add(new WebViewHeader(context.getString(R.string.cancel_order_header),CANCEL_ORDER_ROW_ID,orderList.getOrderId(),orderList.getDisplayOrderId(),0));
        mData.add(new WebViewHeader(context.getString(R.string.reorder_entire),REORDER_ENTIRE_ORDER_ROW_ID,orderList.getOrderId(),orderList.getDisplayOrderId(),0));

        //Add item header
        mData.add(new OrderDetailHeader(context.getString(R.string.item_header)));

        for(int commerceItemCount=0; commerceItemCount< commerceItemSize;commerceItemCount++){
            CommerceItems commerceItem=orderList.getCommerceItems().get(commerceItemCount);
            mData.add(commerceItem);
            mData.add(new WebViewHeader(context.getString(R.string.reorder_item),REORDER_ITEM_ROW_ID,commerceItem.getSkuId(),commerceItem.getProductId(),Integer.parseInt(commerceItem.getQuantity())));
            mData.add(new WebViewHeader(context.getString(R.string.write_review_header),REVIEW_ROW_ID,commerceItem.getSkuId(), commerceItem.getProductId(),Integer.parseInt(commerceItem.getQuantity())));
        }

        //Add shipment header
        mData.add(new OrderDetailHeader(context.getString(R.string.shipment_header)));

        //Add all ShippingDetail in List
       int shippingDetailSize=orderList.getShippingGroups().size();
        for(int shippingItemCount=0;shippingItemCount<shippingDetailSize;shippingItemCount++){
            ShippingGroup shippingGroup=orderList.getShippingGroups().get(shippingItemCount);
            mData.add(shippingGroup);
        }
        if(orderList.getStatus().equalsIgnoreCase(context.getString(R.string.OrderStatus_OrderGone))&&orderList.getShippingGroups().get(0).getTrackingNumber()!=null && !orderList.getShippingGroups().get(0).getTrackingNumber().isEmpty() )
        mData.add(new WebViewHeader(context.getString(R.string.track_shipment),TRACK_ROW_ID,orderList.getShippingGroups().get(0).getTrackingNumber(),orderList.getShippingGroups().get(0).getCompanyName(),0));

        //Add payment header
        mData.add(new OrderDetailHeader(context.getString(R.string.payment_header)));

        //Add all PaymentDetail in List
        int paymentDetailSize=orderList.getPaymentGroups().size();
        for(int paymentItemCount=0;paymentItemCount<paymentDetailSize;paymentItemCount++){
            PaymentGroup paymentGroup=orderList.getPaymentGroups().get(paymentItemCount);
            mData.add(paymentGroup);
        }

        if (mNotifyOnChange)
            notifyItemRangeInserted(0, mData.size());

        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Object getItemAt(int position) {
        Log.d("mdata size", mData.size() + ">>>");
        return mData.get(position);
    }
    public void add(Object item) {
        if (item != null) {
            mData.add(item);
            if (mNotifyOnChange)
                notifyItemChanged(mData.size() - 1);
        }
    }
    public void refreshData(OrderList data) {
        clear();
        setData(data);
    }
    /**
     * Clear all items and groups.
     */
    public void clear() {
        if (mData.size() > 0) {
            int size = mData.size();
            mData.clear();
            if (mNotifyOnChange)
                notifyItemRangeRemoved(0, size);
        }
    }
    public void insert(int position, Object item) {
        mData.add(position, item);
        if (mNotifyOnChange)
            notifyItemInserted(position);
    }
}
