package com.petmeds1800.ui.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.dagger.module.DaggerOrderComponent;
import com.petmeds1800.dagger.module.OrderPresenterModule;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.ItemSelectionDialogFragment;
import com.petmeds1800.ui.fragments.dialog.ItemSelectionDialogFragment.OnItemSelectedListener;
import com.petmeds1800.ui.orders.presenter.OrderListPresenter;
import com.petmeds1800.ui.orders.support.DividerItemDecoration;
import com.petmeds1800.ui.orders.support.MyOrderAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/3/2016.
 */
public class MyOrderFragment extends AbstractFragment implements View.OnClickListener,OnItemSelectedListener,OrderListContract.View{

    @BindView(R.id.order_list_view)
    RecyclerView mOrderRecyclerView;

    @BindView(R.id.filter_button)
    FloatingActionButton mFilterButton;

    private MyOrderAdapter mOrderListAdapter;

    @Inject
    OrderListPresenter mOrderPresenter;

    private List<OrderList> mOrderList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_orders,null);
         ButterKnife.bind(this, view);
        ((AbstractActivity)getActivity()).setTitle(getActivity().getString(R.string.title_my_orders));

        mOrderListAdapter = new MyOrderAdapter(false, getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mOrderRecyclerView.getChildAdapterPosition(v);
                OrderList orderDetail= mOrderList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderlist",orderDetail);
                replaceFragmentWithBundle(new OrderDetailFragment(),bundle);
            }
        });

        DaggerOrderComponent.builder()
                .appComponent(PetMedsApplication.getAppComponent())
                .orderPresenterModule(new OrderPresenterModule(this))
                .build().inject(this);
        setUpOrderList();
        mOrderPresenter.setOrderListData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFilterButton.setOnClickListener(this);

}

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity)getActivity()).setTitle(getActivity().getString(R.string.title_my_orders));

    }

    private void setUpOrderList(){
        mOrderRecyclerView.setAdapter(mOrderListAdapter);
        mOrderRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.filter_button:
              mOrderPresenter.setFilterData();
                break;
        }
    }



    @Override
    public void onItemSelected(ItemSelectionDialogFragment fragment, ItemSelectionDialogFragment.Item item, int index) {

    }

    @Override
    public void setPresenter(OrderListContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void updateOrderList(List<OrderList> orderList) {
        mOrderList=orderList;
        mOrderListAdapter.setData(orderList);

    }

    @Override
    public void updateFilterList(ArrayList<ItemSelectionDialogFragment.Item> pickerItems) {
        ItemSelectionDialogFragment dialog = ItemSelectionDialogFragment.newInstance(
                getActivity().getString(R.string.application_name),
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "ItemPicker");
    }

}
