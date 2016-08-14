package com.petmeds1800.dagger.module;

import com.petmeds1800.dagger.ActivityScope;
import com.petmeds1800.ui.orders.MyOrderFragment;

import dagger.Component;

/**
 * Created by pooja on 8/10/2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class ,modules = OrderPresenterModule.class)
public interface OrderComponent {
    void inject(MyOrderFragment orderFragment);
}
