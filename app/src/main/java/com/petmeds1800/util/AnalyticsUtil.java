package com.petmeds1800.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommitOrder;
import com.petmeds1800.model.entities.Item;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by arthur on 04/03/15.
 */
public class AnalyticsUtil {

    @Inject
    Context mContext;

    @Inject
    Tracker mTracker;

    public AnalyticsUtil() {
        PetMedsApplication.getAppComponent().inject(this);
    }

    public void trackScreen(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackEvent(final String category, String action, final String label) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public void trackScreenForProductTransaction(String screenName, CommitOrder commitOrder, Context context) {
        if (commitOrder != null) {
            List<Item> commitOrderItems = commitOrder.getItems();
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder();
            mTracker.setScreenName(screenName);
            mTracker.set("&cu", "USD");
            builder.set("state", commitOrder.getShippingState());
            builder.set("city", commitOrder.getShippingCity());
            builder.set("country", commitOrder.getShippingCountry());
            ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                    .setTransactionId(commitOrder.getOrderId())
                    .setTransactionAffiliation(context.getString(R.string.application_name))
                    .setTransactionRevenue(commitOrder.getOrderTotal())
                    .setTransactionTax(commitOrder.getTaxTotal())
                    .setTransactionShipping(commitOrder.getShippingTotal());
            builder.setProductAction(productAction);
            if (commitOrderItems != null) {
                for (Item item : commitOrderItems) {
                    Product product = new Product()
                            .setId(item.getCommerceItemId())
                            .setCategory(item.getParentCategory())
                            .setName(item.getProductName())
                            .setPrice(item.getSellingPrice())
                            .setQuantity(item.getItemQuantity());
                    builder.addProduct(product);
                }
            }
            mTracker.send(builder.build());
        }
    }


}
