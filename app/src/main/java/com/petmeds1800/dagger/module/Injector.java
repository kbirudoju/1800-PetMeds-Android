package com.petmeds1800.dagger.module;

import com.petmeds1800.loader.LoremLoader;
import com.petmeds1800.loader.SaveLoremLoader;
import com.petmeds1800.ui.fragments.LoremFragment;

/**
 * Specifies the injection places. Utility interface, to separate from the {@link AppComponent}.
 *
 * @author Konrad
 */
public interface Injector {

    void inject(LoremLoader loremLoader);

    void inject(SaveLoremLoader saveLoremLoader);

    void inject(LoremFragment loremFragment);
}
