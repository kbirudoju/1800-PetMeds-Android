package com.petmeds1800.ui.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.ReOrderRequest;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.model.entities.WebViewHeader;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.dialog.BaseDialogFragment;
import com.petmeds1800.ui.fragments.dialog.OkCancelDialogFragment;
import com.petmeds1800.ui.orders.presenter.OrderDetailPresenter;
import com.petmeds1800.ui.orders.support.CustomOrderDetailRecyclerAdapter;
import com.petmeds1800.ui.orders.support.OrderDetailAdapter;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/19/2016.
 */
public class OrderDetailFragment extends AbstractFragment implements OrderDetailContract.View{
    @BindView(R.id.order_deatil_recycler_view)
    RecyclerView mOrderDetailRecyclerView;

    private OrderDetailAdapter mOrderDetailAdapter;

    private OrderDetailContract.Presenter mPresenter;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    OrderList orderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, null);
        ButterKnife.bind(this, view);
        mPresenter=new OrderDetailPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderList = (OrderList)bundle.getSerializable("orderlist");

        }
        mOrderDetailAdapter= new OrderDetailAdapter(getActivity(), orderList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("view id is",v.toString());
                int position = mOrderDetailRecyclerView.getChildAdapterPosition(v);
                WebViewHeader webviewRow=(WebViewHeader)mOrderDetailAdapter.getItemAt(position);
                switch(webviewRow.getId()){
                    case CustomOrderDetailRecyclerAdapter.REVIEW_ROW_ID:
                        String skuId=webviewRow.getItemId();
                        String productId=webviewRow.getProductId();
                        Bundle bundle = new Bundle();
                        bundle.putString(CommonWebviewFragment.TITLE_KEY,getString(R.string.title_my_orders));
                        bundle.putString(CommonWebviewFragment.URL_KEY,getActivity().getString(R.string.server_endpoint)+"/product.jsp?id="+productId+"&sku="+skuId+"&review=write");
                        replaceAccountFragmentWithBundle(new CommonWebviewFragment(), bundle);
                        break;
                    case CustomOrderDetailRecyclerAdapter.TRACK_ROW_ID:
                        String trackingId=orderList.getShippingGroups().get(0).getTrackingNumber();
                        String vendorName=orderList.getShippingGroups().get(0).getCompanyName();
                        Bundle shippingBundle = new Bundle();
                        shippingBundle.putString("vendorName",vendorName);
                        shippingBundle.putString("trackingId",trackingId);
                        replaceAccountFragmentWithBundle(new TrackShipmentFragment(), shippingBundle);
                        break;
                    case CustomOrderDetailRecyclerAdapter.REORDER_ENTIRE_ORDER_ROW_ID:
                        ReOrderRequest reOrderRequest= new ReOrderRequest(orderList.getOrderId(),mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                        try {
                            ((AbstractActivity)getActivity()).startLoadingGif(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mPresenter.reOrder(reOrderRequest);
                        break;
                    case CustomOrderDetailRecyclerAdapter.CANCEL_ORDER_ROW_ID:
                        if(orderList.getIsCancellable().equals("true")) {

                            final OkCancelDialogFragment okCancelDialogFragment = new OkCancelDialogFragment().newInstance(getString(R.string.cancel_order_msg) + orderList.getOrderId(), getString(R.string.cancel_order_title));
                            okCancelDialogFragment.show(((AbstractActivity)getActivity()).getSupportFragmentManager());
                            okCancelDialogFragment.setPositiveListener(new BaseDialogFragment.DialogButtonsListener() {
                                @Override
                                public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                                    ReOrderRequest cancelOrderRequest = new ReOrderRequest(orderList.getOrderId(), mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                                    try {
                                        ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    mPresenter.cancelOrder(cancelOrderRequest);

                                }
                            });
                            okCancelDialogFragment.setNegativeListener(new BaseDialogFragment.DialogButtonsListener() {
                                @Override
                                public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                                    okCancelDialogFragment.dismiss();
                                }
                            });

                        }else{
                            Snackbar.make(mOrderDetailRecyclerView, getString(R.string.cancel_order_error_msg), Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case CustomOrderDetailRecyclerAdapter.REORDER_ITEM_ROW_ID:
                        try {
                            ((AbstractActivity)getActivity()).startLoadingGif(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String reOrderProductId=webviewRow.getProductId();
                        String reOrderSkuId=webviewRow.getItemId();
                        int quantity=webviewRow.getQuantity();
                        AddToCartRequest addToCartRequest=new AddToCartRequest(reOrderSkuId,reOrderProductId,quantity,mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                        mPresenter.addToCart(addToCartRequest);
                        break;
                }

            }
        });
        setRecyclerView();
        setTitle();
        mOrderDetailAdapter.setData(orderList);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    private void setTitle(){
        if(orderList!=null)
            ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.order_txt)+" #"+orderList.getOrderId());
    }

    private void setRecyclerView(){
        mOrderDetailRecyclerView.setAdapter(mOrderDetailAdapter);
        mOrderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderDetailRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mOrderDetailRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess() {
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((HomeActivity)getActivity()).getViewPager().setCurrentItem(1);
    }

    @Override
    public void addToCartSuccess() {
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelSuccess() {
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(mOrderDetailRecyclerView, R.string.order_cancelled_success_msg, Snackbar.LENGTH_LONG).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void setPresenter(OrderDetailContract.Presenter presenter) {

    }
}