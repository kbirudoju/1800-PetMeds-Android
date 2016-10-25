package com.petmeds1800.ui.orders;

import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by pooja on 8/8/2016.
 */
public interface OrderListContract {

    interface View extends BaseView<Presenter> {
        void updateOrderList( List<OrderList> orderList,String filterApplied);
        void updateFilterList(OrderHistoryFilter orderHistoryFilter);
        boolean isActive();
        void onError(String errorMessage);

    }

    interface Presenter extends BasePresenter {

        void setOrderListData();
         void getFilteredOrderList(String filterCode,String filterTitle);
    }
}
