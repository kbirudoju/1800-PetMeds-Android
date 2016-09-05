package com.petmeds1800.ui.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.model.entities.WebViewHeader;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.orders.support.CustomOrderDetailRecyclerAdapter;
import com.petmeds1800.ui.orders.support.OrderDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/19/2016.
 */
public class OrderDetailFragment extends AbstractFragment {
    @BindView(R.id.order_deatil_recycler_view)
    RecyclerView mOrderDetailRecyclerView;

    private OrderDetailAdapter mOrderDetailAdapter;

    OrderList orderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, null);
        ButterKnife.bind(this, view);
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
                        String skuId=orderList.getCommerceItems().get(0).getSkuId();
                        String productId=orderList.getCommerceItems().get(0).getProductId();
                        Bundle bundle = new Bundle();
                        bundle.putString("title",getString(R.string.title_my_orders));
                        bundle.putString("url",getActivity().getString(R.string.server_endpoint)+"/product.jsp?id="+productId+"&sku="+skuId+"&review=write");
                        replaceFragmentWithBundle(new CommonWebviewFragment(),bundle);
                        break;
                    case CustomOrderDetailRecyclerAdapter.TRACK_ROW_ID:
                        String trackingId=orderList.getShippingGroups().get(0).getTrackingNumber();
                        String vendorName=orderList.getShippingGroups().get(0).getCompanyName();
                        Bundle shippingBundle = new Bundle();
                        shippingBundle.putString("title",getString(R.string.title_track_shipment));
                        shippingBundle.putString("url",getActivity().getString(R.string.server_endpoint)+"rsTrack.jsp?TrackID="+trackingId+"&TrackType="+vendorName);
                        replaceFragmentWithBundle(new CommonWebviewFragment(), shippingBundle);
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
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.order_txt)+"#"+orderList.getOrderId());
    }

    private void setRecyclerView(){
        mOrderDetailRecyclerView.setAdapter(mOrderDetailAdapter);
        mOrderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderDetailRecyclerView.setHasFixedSize(true);
    }
}