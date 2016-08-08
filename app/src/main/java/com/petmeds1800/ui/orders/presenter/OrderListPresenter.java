package com.petmeds1800.ui.orders.presenter;

import com.petmeds1800.model.MyOrder;
import com.petmeds1800.ui.fragments.dialog.ItemSelectionDialogFragment;
import com.petmeds1800.ui.orders.OrderListContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by pooja on 8/8/2016.
 */
public class OrderListPresenter implements OrderListContract.Presenter{

 @Inject
 OrderListContract.View mOrderView;

     @Inject
     OrderListPresenter(OrderListContract.View orderView) {
        mOrderView = orderView;
    }

    public void setOrderListData(){
        //hardcoded temporary data, will be removed after API Integration
        List<MyOrder> mOrderList = new ArrayList<>();
        mOrderList.add(new MyOrder("1234567", "04/08/2016", "Delivered"));
        mOrderList.add(new MyOrder("4567893", "04/08/2016", "Cancelled"));
        mOrderList.add(new MyOrder("8945389", "04/08/2016", "Shipping"));
        if(mOrderView.isActive())
             mOrderView.updateOrderList(mOrderList);
    }

    public void setFilterData(){
        //hardcoded temporary data, will be removed after API Integration
        ArrayList<ItemSelectionDialogFragment.Item> pickerItems = new ArrayList<>();
        pickerItems.add(new ItemSelectionDialogFragment.Item("Month",true));
        pickerItems.add(new ItemSelectionDialogFragment.Item("Year", false));
        pickerItems.add(new ItemSelectionDialogFragment.Item("2 Years", false));
        pickerItems.add(new ItemSelectionDialogFragment.Item("All", false));
        if(mOrderView.isActive())
        mOrderView.updateFilterList(pickerItems);
    }



    @Override
    public void start() {

    }
}
