package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddEditCardResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.entities.GuestCheckoutPaymentReuest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 5/10/16.
 */
public class GuestStepThreePresenter implements GuestStepThreeRootContract.Presenter {


    private final GuestStepThreeRootContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public GuestStepThreePresenter(GuestStepThreeRootContract.View view) {
        mView = view;
    }

    @Override
    public void start() {

    }


    @Override
    public void applyCreditCardPaymentMethod(AddressRequest addressRequest , final CardRequest cardRequest) {

        mPetMedsApiService.addAddress(addressRequest)
                .onErrorReturn(new Func1<Throwable, AddAddressResponse>() {
                    @Override
                    public AddAddressResponse call(Throwable throwable) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(throwable);
                        if (errorId != 0) {
//                            mView.hideProgress();
                            mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                        }

                        return null;
                    }
                })
                .flatMap(new Func1<AddAddressResponse, Observable<AddEditCardResponse>>() {
                    @Override
                    public Observable<AddEditCardResponse> call(AddAddressResponse addAddressResponse) {

                        if(addAddressResponse.getStatus().equals(API_SUCCESS_CODE)) {
                            //set the received addressId to the card request
                            cardRequest.setBillingAddressId(addAddressResponse.getProfileAddress().getAddressId());
                            return mPetMedsApiService.addPaymentCard(cardRequest)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        }
                        else {
                            //                            mView.hideProgress();
                            mView.showErrorCrouton(addAddressResponse.getStatus().getErrorMessages().get(0), false);
                            return null;
                        }
                    }
                })
                .flatMap(new Func1<AddEditCardResponse, Observable<ShoppingCartListResponse>>() {
                    @Override
                    public Observable<ShoppingCartListResponse> call(AddEditCardResponse addEditCardResponse) {

                        if(addEditCardResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            //create a GuestPaymentRequest
                            CreditCardPaymentMethodRequest creditCardPaymentMethodRequest =
                                    new CreditCardPaymentMethodRequest(
                                            addEditCardResponse.getCard().getId()
                                    , addEditCardResponse.getCard().getBillingAddress().getRepositoryId()
                                    , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()
                                    );

                            return mPetMedsApiService.applyCreditCardPaymentMethod(creditCardPaymentMethodRequest)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        }
                        else {
                            return null;
                        }

                    }
                })
                .subscribe(new Subscriber<ShoppingCartListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (errorId != 0) {
//                            mView.hideProgress();
                            mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                        }
                    }

                    @Override
                    public void onNext(ShoppingCartListResponse shoppingCartListResponse) {
                        if (shoppingCartListResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSuccessCreditCardPayment(shoppingCartListResponse);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(shoppingCartListResponse.getStatus().getErrorMessages().get(0),
                                        false);
                            }
                        }
                    }
                });

    }
}
