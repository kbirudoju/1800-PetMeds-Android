package com.petmeds1800.ui.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.model.MyOrder;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.orders.support.MyOrderAdapter;
import com.petmeds1800.ui.orders.support.MyOrderEndlessOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 8/3/2016.
 */
public class MyOrderFragment extends AbstractFragment implements View.OnClickListener{

    @BindView(R.id.orderlist)
    RecyclerView mOrderRecyclerView;

    private MyOrderAdapter mOrderAdapter;
    private List<MyOrder> mOrderList;
    private MyOrderEndlessOnScrollListener orderEndlessOnScrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_orders,null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderList=new ArrayList<>();
        mOrderList.add(new MyOrder("1234567","04/08/2016","Processing"));
        mOrderList.add(new MyOrder("4567893","04/08/2016","Shipping"));
        mOrderList.add(new MyOrder("8945389", "04/08/2016", "Delivered"));
         mOrderRecyclerView.setLayoutManager(layoutManager);
        mOrderRecyclerView.setHasFixedSize(true);
        mOrderAdapter = new MyOrderAdapter(false,this);
        mOrderAdapter.setData(mOrderList);
        mOrderRecyclerView.setAdapter(mOrderAdapter);
        mOrderAdapter.enableFooter(false);


        orderEndlessOnScrollListener = new MyOrderEndlessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(long current_page) {
                loadData();
            }
        };
    }

    @Override
    public void onClick(View v) {

    }

    private void loadData(){

    }
}
