package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.mvp.LoginTask.LoginContract;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Digvijay on 8/3/2016.
 */
public class LoginFragment extends AbstractFragment implements LoginContract.View {

    @Inject
    PetMedsApiService mApiService;

    private LoginContract.Presenter mPresenter;
    public static  String sessionConfirmationNUmber="";

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @OnClick(R.id.log_in_button)
    public void login() {

        if (mPresenter.validateCredentials("", "")) {

            mApiService.getSessionConfirmationNumber()
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Func1<SessionConfNumberResponse, Observable<String>>() {
                        @Override
                        public Observable<String> call(SessionConfNumberResponse sessionConfNumberResponse) {
                            Log.v("sessionToken", sessionConfNumberResponse.getSessionConfirmationNumber());
                            sessionConfirmationNUmber=sessionConfNumberResponse.getSessionConfirmationNumber();
                            return mApiService
                                    .login(new LoginRequest("api-demo@gmail.com", "1800petmeds",
                                            sessionConfNumberResponse.getSessionConfirmationNumber()))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            getActivity().startActivity(new HomeIntent(getActivity()));

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {

                            Log.v("login response", s);
                            Toast.makeText(getActivity(), "login response" +
                                    s, Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
    }

}
