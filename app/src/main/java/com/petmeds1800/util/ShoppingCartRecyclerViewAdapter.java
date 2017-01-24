package com.petmeds1800.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sarthak on 9/26/2016.
 */

public class ShoppingCartRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingCartAdapterViewHolder> {

    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_ITEM = 1;

    private ArrayList<CommerceItems> mCommerceItemsesCollection;
    private View mItemFooter;
    private Context mContext;
    private Handler mHandler;
    private Handler mTextChangeHandler;
    private Runnable mTextChangeRunnable;
    private ArrayList<CommerceItems> mCommerceItemsesCollection_previous;

    public ShoppingCartRecyclerViewAdapter(ArrayList<CommerceItems> commerceItemsesCollection, View itemFooter, Context context, Handler handler) {
        this.mCommerceItemsesCollection = commerceItemsesCollection;
        this.mItemFooter = itemFooter;
        this.mContext = context;
        this.mHandler = handler;
        mCommerceItemsesCollection_previous = null;
    }

//    public void updateShoppingCartRecyclerViewAdapter(){
//        Applog.getInstance().Enter();
//
//        mCommerceItemsesCollection = null;
//        mCommerceItemsesCollection = new ArrayList<>();
//        mCommerceItemsesCollection.addAll(mCommerceItemsesCollection_previous);
//        mCommerceItemsesCollection_previous = null;
//        notifyDataSetChanged();
//
//        Applog.getInstance().Enter();
//    }

    @Override
    public ShoppingCartAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchItemView = null;

        if (viewType == TYPE_ITEM) {
            searchItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_each_item_layout, parent, false);
        } else if (viewType == TYPE_FOOTER) {
            searchItemView = mItemFooter;
        }

        return new ShoppingCartAdapterViewHolder(searchItemView,viewType);
    }

    @Override
    public void onBindViewHolder(final ShoppingCartAdapterViewHolder holder, final int position) {

        if (isFooter(position)) {
            return;
        }

        holder.mItemTitle.setText(mCommerceItemsesCollection.get(position).getProductDisplayName());
        holder.mItemDescription.setText(mCommerceItemsesCollection.get(position).getSkuDisplayName());
        holder.mItemCost.setText(mContext.getResources().getString(R.string.dollar_placeholder) + String.format(mContext.getResources().getConfiguration().locale ,"%.2f", mCommerceItemsesCollection.get(position).getSellingPrice()));

        Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + mCommerceItemsesCollection.get(position).getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.itemImage) {

            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.itemImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        holder.mDeleteItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = Message.obtain(null, Constants.DELETE_ITEM_REQUEST_SHOPPINGCART);
                Bundle b = new Bundle();
                b.putString(Constants.COMMERCE_ITEM_ID, mCommerceItemsesCollection.get(position).getCommerceItemId());
                msg.setData(b);

                try {
                    mHandler.sendMessage(msg);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (true == mCommerceItemsesCollection.get(position).isRxItem()) {

            holder.mEditItemLayout.setVisibility(View.VISIBLE);
            holder.mEditItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Message msg = Message.obtain(null, Constants.CLICK_ITEM_UPDATE_SHOPPINGCART);
                    Bundle b = new Bundle();
                    b.putString(CommonWebviewFragment.TITLE_KEY, mCommerceItemsesCollection.get(position).getProductDisplayName());
                    b.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.server_endpoint) + mCommerceItemsesCollection.get(position).getProductPageUrl()+"&ci="+mCommerceItemsesCollection.get(position).getCommerceItemId());
                    msg.setData(b);

                    try {
                        mHandler.sendMessage(msg);
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            holder.mEditItemLayout.setVisibility(View.GONE);
        }

        if (null != mCommerceItemsesCollection.get(position) && !mCommerceItemsesCollection.get(position).getQuantity().isEmpty()) {
            holder.mItemQuantityDescription.getEditText().setText(mCommerceItemsesCollection.get(position).getQuantity());

            holder.mItemQuantityDescription.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_DONE && !holder.mItemQuantityDescription.getEditText().getText().toString().isEmpty() && !(mCommerceItemsesCollection.get(position).getQuantity()).equalsIgnoreCase(holder.mItemQuantityDescription.getEditText().getText().toString().trim())) {
                        Message msg = Message.obtain(null, Constants.UPDATE_ITEM_QUANTITY_SHOPPINGCART);
                        Bundle b = new Bundle();

                        mCommerceItemsesCollection_previous = null;
                        mCommerceItemsesCollection_previous = new ArrayList<>();
                        mCommerceItemsesCollection_previous.addAll(mCommerceItemsesCollection);

                        HashMap<String, String> commerceID_QuantityMap = new HashMap<String, String>();
                        for (int i = 0; i < mCommerceItemsesCollection.size(); i++) {
                            commerceID_QuantityMap.put(mCommerceItemsesCollection.get(i).getCommerceItemId(), (mCommerceItemsesCollection.get(i).getQuantity()));
                        }
                        commerceID_QuantityMap.put(mCommerceItemsesCollection.get(position).getCommerceItemId(), holder.mItemQuantityDescription.getEditText().getText().toString().trim());
//                        mCommerceItemsesCollection.get(position).setQuantity(holder.mItemQuantityDescription.getEditText().getText().toString().trim());
                        b.putSerializable(Constants.QUANTITY_MAP, commerceID_QuantityMap);
                        msg.setData(b);

                        try {
                            holder.mItemQuantityDescription.getEditText().addTextChangedListener(null);
                            CartFragment.sPreviousScrollPosition = position;
                            mHandler.sendMessage(msg);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(holder.mItemQuantityDescription.getEditText().getWindowToken(), 0);

                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            });

            holder.mItemQuantityDescription.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus){
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(holder.mItemQuantityDescription.getEditText().getWindowToken(), 0);
                    }

                }
            });

            holder.mItemQuantityDescription.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (mTextChangeRunnable != null && mTextChangeHandler != null){
                        mTextChangeHandler.removeCallbacks(mTextChangeRunnable);
                        mTextChangeRunnable = null;
                        mTextChangeHandler = null;
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {

                    if (mTextChangeRunnable != null && mTextChangeHandler != null){
                        mTextChangeHandler.removeCallbacks(mTextChangeRunnable);
                        mTextChangeRunnable = null;
                        mTextChangeHandler = null;
                    }

                    if (!holder.mItemQuantityDescription.getEditText().getText().toString().isEmpty() && !(mCommerceItemsesCollection.get(position).getQuantity()).equalsIgnoreCase(holder.mItemQuantityDescription.getEditText().getText().toString().trim())) {

                        mTextChangeRunnable = new Runnable() {
                            @Override
                            public void run() {

                                Message msg = Message.obtain(null, Constants.UPDATE_ITEM_QUANTITY_SHOPPINGCART);
                                Bundle b = new Bundle();

                                mCommerceItemsesCollection_previous = null;
                                mCommerceItemsesCollection_previous = new ArrayList<>();
                                mCommerceItemsesCollection_previous.addAll(mCommerceItemsesCollection);

                                HashMap<String, String> commerceID_QuantityMap = new HashMap<String, String>();
                                for (int i = 0; i < mCommerceItemsesCollection.size(); i++) {
                                    commerceID_QuantityMap.put(mCommerceItemsesCollection.get(i).getCommerceItemId(), (mCommerceItemsesCollection.get(i).getQuantity()));
                                }
                                commerceID_QuantityMap.put(mCommerceItemsesCollection.get(position).getCommerceItemId(), holder.mItemQuantityDescription.getEditText().getText().toString().trim());
//                                mCommerceItemsesCollection.get(position).setQuantity(holder.mItemQuantityDescription.getEditText().getText().toString().trim());
                                b.putSerializable(Constants.QUANTITY_MAP, commerceID_QuantityMap);
                                msg.setData(b);

                                try {
                                    CartFragment.sPreviousScrollPosition = position;
                                    mHandler.sendMessage(msg);
                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(holder.mItemQuantityDescription.getEditText().getWindowToken(), 0);
                                }
                            }
                        };
                        mTextChangeHandler = new Handler();
                        mTextChangeHandler.postDelayed(mTextChangeRunnable,2000);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCommerceItemsesCollection.size()+1;
    }

    public boolean isFooter(int position) {
        return position == mCommerceItemsesCollection.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ?
                TYPE_FOOTER : TYPE_ITEM;
    }
}