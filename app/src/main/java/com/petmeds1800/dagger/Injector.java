package com.petmeds1800.dagger;

import com.petmeds1800.dagger.component.AppComponent;
import com.petmeds1800.mvp.SignupTask.SignUpPresenter;
import com.petmeds1800.service.RefillReminderService;
import com.petmeds1800.ui.BarcodeScannerActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.IntroActivity;
import com.petmeds1800.ui.SplashActivity;
import com.petmeds1800.ui.account.AccountSettingsFragment;
import com.petmeds1800.ui.account.AccountSettingsPresenter;
import com.petmeds1800.ui.account.SignOutPresenter;
import com.petmeds1800.ui.address.AddAddressPresenter;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.address.SavedAddressListPresenter;
import com.petmeds1800.ui.dashboard.CategoryListFragment;
import com.petmeds1800.ui.dashboard.ProductCategoryPresenter;
import com.petmeds1800.ui.dashboard.presenter.WidgetPresenter;
import com.petmeds1800.ui.fragments.AccountFragment;
import com.petmeds1800.ui.fragments.AccountRootFragment;
import com.petmeds1800.ui.fragments.ForgotPasswordFragment;
import com.petmeds1800.ui.fragments.LoginFragment;
import com.petmeds1800.ui.fragments.SignOutFragment;
import com.petmeds1800.ui.fragments.SignUpFragment;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.ui.payment.AddACardPresenter;
import com.petmeds1800.ui.payment.AddEditCardFragment;
import com.petmeds1800.ui.payment.SavedCardsListPresenter;
import com.petmeds1800.ui.pets.AddPetFragment;
import com.petmeds1800.ui.pets.presenter.AddPetPresenter;
import com.petmeds1800.ui.pets.presenter.PetListPresenter;
import com.petmeds1800.util.AnalyticsUtil;

/**
 * Specifies the injection places. Utility interface, to separate from the {@link AppComponent}.
 *
 * @author Konrad
 */
public interface Injector {

    void inject(SplashActivity splashActivity);

    void inject(HomeActivity homeActivity);

    void inject(LoginFragment loginFragment);

    void inject(ForgotPasswordFragment forgotPasswordFragment);

    void inject(FingerprintAuthenticationDialog fingerprintAuthenticationDialog);

    void inject(IntroActivity introActivity);

    void inject(SignUpFragment signUpFragment);

    void inject(SignUpPresenter signUpPresenter);

    void inject(BarcodeScannerActivity barcodeScannerActivity);

    void inject(AddACardPresenter addACardPresenter);

    void inject(SavedCardsListPresenter savedCardsListPresenter);

    void inject(AccountSettingsPresenter accountSettingsPresenter);

    void inject(SavedAddressListPresenter savedAddressListPresenter);

    void inject(AddAddressPresenter addAddressPresenter);

    void inject(AddEditCardFragment addEditCardFragment);

    void inject(AddEditAddressFragment addEditAddressFragment);

    void inject(AccountSettingsFragment accountSettingsFragment);

    void inject(PetListPresenter petsListPresenter);

    void inject(AddPetPresenter addPetPresenter);

    void inject(AddPetFragment addPetFragment);

    void inject(AccountFragment accountFragment);

    void inject(SignOutFragment signoutfragment);

    void inject(AccountRootFragment accountRootFragment);

    void inject(RefillReminderService refillReminderService);

    void inject(SignOutPresenter signOutPresenter);

    void inject(AnalyticsUtil analyticsUtil);

    void inject(WidgetPresenter widgetPresenter);

    void inject(CategoryListFragment categoryListFragment);

    void inject(ProductCategoryPresenter productCategoryPresenter);
}
