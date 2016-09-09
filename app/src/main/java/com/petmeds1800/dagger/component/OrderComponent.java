package com.petmeds1800.dagger.component;

import com.petmeds1800.dagger.scopes.ActivityScope;
import com.petmeds1800.ui.orders.MyOrderFragment;
import com.petmeds1800.dagger.module.OrderPresenterModule;

import dagger.Component;

/**
 * Created by pooja on 8/10/2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class ,modules = OrderPresenterModule.class)
public interface OrderComponent {
    void inject(MyOrderFragment orderFragment);
}
