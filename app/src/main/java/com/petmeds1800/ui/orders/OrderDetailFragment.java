package com.petmeds1800.ui.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
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
        mOrderDetailAdapter= new OrderDetailAdapter(getActivity(), orderList);
        setRecyclerView();
        setTitle();
        mOrderDetailAdapter.setData(orderList);



    }

    private void setTitle(){
        if(orderList!=null)
        ((AbstractActivity)getActivity()).setTitle(orderList.getOrderId());
    }

    private void setRecyclerView(){
        mOrderDetailRecyclerView.setAdapter(mOrderDetailAdapter);
        mOrderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderDetailRecyclerView.setHasFixedSize(true);
    }
}