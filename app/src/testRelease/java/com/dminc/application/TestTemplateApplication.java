package com.dminc.application;

import com.dminc.application.module.AppComponent;
import com.dminc.application.module.ApplicationModule;
import com.dminc.application.module.DaggerAppComponent;

/**
 * @author Konrad
 */
public class TestTemplateApplication extends TemplateApplication {
    @Override
    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restModule(new MockRestModule(this))
                .build();
    }
}
