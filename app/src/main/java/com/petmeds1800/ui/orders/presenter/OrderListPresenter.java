package com.petmeds1800.ui.orders.presenter;

import com.petmeds1800.util.Log;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.OrderFilterList;
import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.ui.orders.OrderListContract;
import com.petmeds1800.util.GeneralPreferencesHelper;

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

    private String mfilterCode;
    public OrderFilterList mfilterList;

    @Inject
    public OrderListPresenter(OrderListContract.View orderView) {
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
                        mfilterList = getFilterCode(orderHistoryFilter);
                        mfilterCode = mfilterList.getCode();
                        return mApiService
                                .getOrderList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(), mfilterCode)
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
                        Log.e("OrderListPresenter", e.getMessage());
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("OrderList", "retrying after session renew");

                            setOrderListData();

                            return;

                        }

                        //proceed further
                        mOrderView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(MyOrder myOrder) {
                        Log.d("orderlist size", myOrder.getCount() + "");
                        if (myOrder.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mOrderView.isActive()) {
                                if (myOrder.getOrderList() != null) {
                                    mOrderView.updateOrderList(myOrder.getOrderList(),mfilterList.getName());
                                }
                            }
                        } else {
                            if (mOrderView.isActive()) {
                                mOrderView.onError(myOrder.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }



    @Override
    public void start() {

    }

    private OrderFilterList getFilterCode(OrderHistoryFilter orderHistoryFilter) {
        mOrderView.updateFilterList(orderHistoryFilter);
        for (OrderFilterList orderFilterList : orderHistoryFilter.getOrderFilterList()) {
            if (orderFilterList.isDefault()) {
                return orderFilterList;

            }
        }
        return null;
    }

    @Override
    public void getFilteredOrderList(final String filterCode, final String filterTitle) {
        mApiService.getOrderList(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(), filterCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                        if (myOrder.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mOrderView.isActive()) {
                                if (myOrder.getOrderList() != null) {
                                    mOrderView.updateOrderList(myOrder.getOrderList(),filterTitle);
                                }
                            }
                        } else {
                            if (mOrderView.isActive()) {
                                mOrderView.onError(myOrder.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }
}
