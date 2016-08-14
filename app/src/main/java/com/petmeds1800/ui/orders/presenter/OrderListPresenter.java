package com.petmeds1800.ui.orders.presenter;

import android.util.Log;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.ui.fragments.LoginFragment;
import com.petmeds1800.ui.fragments.dialog.ItemSelectionDialogFragment;
import com.petmeds1800.ui.orders.OrderListContract;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 8/8/2016.
 */
public class OrderListPresenter implements OrderListContract.Presenter{

    @Inject
    OrderListContract.View mOrderView;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    OrderListPresenter(OrderListContract.View orderView) {
        mOrderView = orderView;
    }

    public void setOrderListData(){

        mApiService.getOrderHistoryFilter(LoginFragment.sessionConfirmationNUmber)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<OrderHistoryFilter, Observable<MyOrder>>() {
                    @Override
                    public Observable<MyOrder> call(OrderHistoryFilter orderHistoryFilter) {
                        //temporary code just to check API. we will do modification after backend fixes
                        String filterId=orderHistoryFilter.getOrderFilterList().get(0).getCode();
                        return mApiService
                                .getOrderList(LoginFragment.sessionConfirmationNUmber,filterId)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribe(new Subscriber<MyOrder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MyOrder myOrder) {
                        Log.d("orderlist size", myOrder.getCount() + "");
                        if(myOrder.getOrderList()!=null && myOrder.getOrderList().size()>0){
                            mOrderView.updateOrderList(myOrder.getOrderList()) ;
                        }
                    }
                });
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
