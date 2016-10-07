package com.petmeds1800.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
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

    public ShoppingCartRecyclerViewAdapter(ArrayList<CommerceItems> commerceItemsesCollection, View itemFooter, Context context, Handler handler) {
        this.mCommerceItemsesCollection = commerceItemsesCollection;
        this.mItemFooter = itemFooter;
        this.mContext = context;
        this.mHandler = handler;
    }

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
        holder.mItemCost.setText(mContext.getResources().getString(R.string.dollar_placeholder) + Float.toString(mCommerceItemsesCollection.get(position).getSellingPrice()));

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
                    b.putString(CommonWebviewFragment.URL_KEY, mContext.getString(R.string.server_endpoint) + mCommerceItemsesCollection.get(position).getProductPageUrl() + "&review=write");
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

            holder.mItemQuantityDescription.getEditText().setFocusable(false);
            holder.mItemQuantityDescription.getEditText().setFocusableInTouchMode(false);
            holder.mItemQuantityDescription.getEditText().setText(mCommerceItemsesCollection.get(position).getQuantity());
            holder.mItemQuantityDescription.getEditText().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setTitle(mContext.getResources().getString(R.string.select_item_quantity));
                        View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_quanity_list, null);
                        ListView mListView = (ListView) view.findViewById(R.id.quantity_available_list);
                        mListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mContext.getResources().getStringArray(R.array.items_quantity_array)));
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                if (pos < 9) {
                                    holder.mItemQuantityDescription.getEditText().setText(mContext.getResources().getStringArray(R.array.items_quantity_array)[pos]);
                                    Message msg = Message.obtain(null, Constants.UPDATE_ITEM_QUANTITY_SHOPPINGCART);
                                    Bundle b = new Bundle();

                                    HashMap<String, String> commerceID_QuantityMap = new HashMap<>();

                                    for (int i = 0; i < mCommerceItemsesCollection.size(); i++) {
                                        commerceID_QuantityMap.put(mCommerceItemsesCollection.get(i).getCommerceItemId(), (mCommerceItemsesCollection.get(i).getQuantity()));
                                    }
                                    commerceID_QuantityMap.put(mCommerceItemsesCollection.get(position).getCommerceItemId(), holder.mItemQuantityDescription.getEditText().getText().toString().trim());
                                    b.putSerializable(Constants.QUANTITY_MAP, commerceID_QuantityMap);
                                    msg.setData(b);

                                    try {
                                        mHandler.sendMessage(msg);
                                    } catch (ClassCastException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        dialog.setOnDismissListener(null);
                                        dialog.dismiss();
                                    }
                                }
                                 else if (pos >= 9) {

                                    dialog.setOnDismissListener(null);
                                    dialog.dismiss();
                                    holder.mItemQuantityDescription.getEditText().setFocusable(true);
                                    holder.mItemQuantityDescription.getEditText().setClickable(true);
                                    holder.mItemQuantityDescription.getEditText().setFocusableInTouchMode(true);
                                    holder.mItemQuantityDescription.getEditText().setOnClickListener(null);
                                    holder.mItemQuantityDescription.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {

                                        @Override
                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                            if (actionId == EditorInfo.IME_ACTION_DONE && !holder.mItemQuantityDescription.getEditText().getText().toString().isEmpty() && !(mCommerceItemsesCollection.get(position).getQuantity()).equalsIgnoreCase(holder.mItemQuantityDescription.getEditText().getText().toString().trim())) {
                                                Message msg = Message.obtain(null, Constants.UPDATE_ITEM_QUANTITY_SHOPPINGCART);
                                                Bundle b = new Bundle();

                                                HashMap<String, String> commerceID_QuantityMap = new HashMap<>();

                                                for (int i = 0; i < mCommerceItemsesCollection.size(); i++) {
                                                    commerceID_QuantityMap.put(mCommerceItemsesCollection.get(i).getCommerceItemId(), (mCommerceItemsesCollection.get(i).getQuantity()));
                                                }
                                                commerceID_QuantityMap.put(mCommerceItemsesCollection.get(position).getCommerceItemId(), holder.mItemQuantityDescription.getEditText().getText().toString().trim());
                                                b.putSerializable(Constants.QUANTITY_MAP, commerceID_QuantityMap);
                                                msg.setData(b);

                                                try {
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
                                            } else return false;
                                        }

                                    });

                                    holder.mItemQuantityDescription.getEditText().requestFocus();
                                    (new Handler()).postDelayed(new Runnable() {

                                        public void run() {
                                            holder.mItemQuantityDescription.getEditText().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                                            holder.mItemQuantityDescription.getEditText().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
                                            holder.mItemQuantityDescription.getEditText().setText(mCommerceItemsesCollection.get(position).getQuantity());
                                            holder.mItemQuantityDescription.getEditText().setSelection(holder.mItemQuantityDescription.getEditText().getText().length());
                                        }
                                    }, 200);
                                }
                            }
                        });
                        dialog.setContentView(view);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Message msg = Message.obtain(null, Constants.UPDATE_ITEM_QUANTITY_SHOPPINGCART);
                                Bundle b = new Bundle();

                                HashMap<String, String> commerceID_QuantityMap = new HashMap<>();

                                for (int i = 0; i < mCommerceItemsesCollection.size(); i++) {
                                    commerceID_QuantityMap.put(mCommerceItemsesCollection.get(i).getCommerceItemId(), (mCommerceItemsesCollection.get(i).getQuantity()));
                                }
                                b.putSerializable(Constants.QUANTITY_MAP, commerceID_QuantityMap);
                                msg.setData(b);

                                try {
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
                        });
                        dialog.show();
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