package com.petmeds1800.ui.orders;

import com.petmeds1800.model.MyOrder;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;
import com.petmeds1800.ui.fragments.dialog.ItemSelectionDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pooja on 8/8/2016.
 */
public interface OrderListContract {

    interface View extends BaseView<Presenter> {
        void updateOrderList( List<MyOrder> orderList);
        void updateFilterList(ArrayList<ItemSelectionDialogFragment.Item> pickerItems );
        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void setOrderListData();
        void setFilterData();
    }
}
