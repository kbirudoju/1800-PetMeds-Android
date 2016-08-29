package com.petmeds1800.ui.orders.presenter;

import android.util.Log;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.OrderFilterList;
import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.ui.fragments.dialog.ItemSelectionDialogFragment;
import com.petmeds1800.ui.orders.OrderListContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

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
public class OrderListPresenter implements OrderListContract.Presenter {

    @Inject
    OrderListContract.View mOrderView;

    @Inject
    PetMedsApiService mApiService;

    private String filterCode;

    @Inject
    OrderListPresenter(OrderListContract.View orderView) {
        mOrderView = orderView;
    }

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public void setOrderListData() {

        mApiService.getOrderHistoryFilter(
                mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<OrderHistoryFilter, Observable<MyOrder>>() {
                    @Override
                    public Observable<MyOrder> call(OrderHistoryFilter orderHistoryFilter) {
                        //temporary code just to check API. we will do modification after backend fixes
                        filterCode=getFilterCode(orderHistoryFilter);

                        return mApiService
                                .getOrderList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(), filterCode)
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
                        mOrderView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(MyOrder myOrder) {
                        Log.d("orderlist size", myOrder.getCount() + "");
                        if(myOrder.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if(mOrderView.isActive()){
                                if (myOrder.getOrderList() != null) {
                                    mOrderView.updateOrderList(myOrder.getOrderList());
                                }
                            }
                        }else{
                            if(mOrderView.isActive()){
                                mOrderView.onError(myOrder.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }

    public void setFilterData() {
        //hardcoded temporary data, will be removed after API Integration
        ArrayList<ItemSelectionDialogFragment.Item> pickerItems = new ArrayList<>();
        pickerItems.add(new ItemSelectionDialogFragment.Item("Month", true));
        pickerItems.add(new ItemSelectionDialogFragment.Item("Year", false));
        pickerItems.add(new ItemSelectionDialogFragment.Item("2 Years", false));
        pickerItems.add(new ItemSelectionDialogFragment.Item("All", false));
        if (mOrderView.isActive()) {
            mOrderView.updateFilterList(pickerItems);
        }
    }

    @Override
    public void start() {

    }

    private String getFilterCode(OrderHistoryFilter orderHistoryFilter){
        for(OrderFilterList orderFilterList:orderHistoryFilter.getOrderFilterList() ){
            if(orderFilterList.isDefault()){
                String code=orderFilterList.getCode();
                return code;
            }
        }
        return null;
    }
}
