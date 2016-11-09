package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.util.AsyncRenewSessionConfirmationNumber;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 8/4/2016.
 */
public class HomeRootFragment extends AbstractFragment {

    IntentFilter intentFilter = new IntentFilter(Constants.KEY_HOME_ROOT_SESSION_CONFIRMATION);

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        addHomeFragment();
        registerIntent(intentFilter,getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deregisterIntent(getActivity());
    }

    public void addHomeFragment() {
        replaceHomeFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showErrorCrouton(CharSequence message, boolean span) {
        if (span) {
            Utils.displayCrouton(getActivity(), (Spanned) message);
        } else {
            Utils.displayCrouton(getActivity(), (String) message);
        }
    }

    /**
     * Independent call to renew session Confirmation number
     * In case of 409 error in add to cart of {@link CommonWebviewFragment}
     */
    private void asyncRenewSessionConfirmation(){
        Log.w("HomeRootFragment", "asyncRenewSessionConfirmation Enter");
        new AsyncRenewSessionConfirmationNumber(this,mApiService,mPreferencesHelper).initializeSessionConfirmationNumber();
        Log.w("HomeRootFragment", "asyncRenewSessionConfirmation Exit");
    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        super.onReceivedBroadcast(context, intent);
        if (intent.getAction().equalsIgnoreCase(Constants.KEY_HOME_ROOT_SESSION_CONFIRMATION)){
            try {
                asyncRenewSessionConfirmation();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
