package com.petmeds1800.dagger.module;

import com.petmeds1800.ui.account.AccountSettingsPresenter;
import com.petmeds1800.ui.fragments.LoginFragment;

import com.petmeds1800.ui.fragments.LoginFragment;
import com.petmeds1800.ui.payment.AddACardPresenter;
import com.petmeds1800.ui.payment.SavedCardsListPresenter;

/**
 * Specifies the injection places. Utility interface, to separate from the {@link AppComponent}.
 *
 * @author Konrad
 */
public interface Injector {

    void inject(LoginFragment loginFragment);

    void inject(AddACardPresenter addACardPresenter);

    void inject(SavedCardsListPresenter savedCardsListPresenter);

    void inject(AccountSettingsPresenter accountSettingsPresenter);

}
