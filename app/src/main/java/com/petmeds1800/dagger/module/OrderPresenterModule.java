package com.petmeds1800.dagger.module;

import com.petmeds1800.ui.orders.OrderListContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pooja on 8/9/2016.
 */
@Module
public class OrderPresenterModule {

    private final OrderListContract.View mView;

    public OrderPresenterModule(OrderListContract.View view) {
        mView = view;
    }

    @Provides
    OrderListContract.View provideLoginView() {
        return mView;
    }
}
