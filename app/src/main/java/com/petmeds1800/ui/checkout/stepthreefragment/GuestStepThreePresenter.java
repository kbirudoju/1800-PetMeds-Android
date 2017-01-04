package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddEditCardResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.model.entities.UpdateCardRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.support.v4.app.Fragment;
import com.petmeds1800.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

    @Inject
    @Named(Constants.TAG_REDIRECT_OFF)
    PetMedsApiService mPetMedsApiServiceRedrirectOff;

    public GuestStepThreePresenter(GuestStepThreeRootContract.View view) {
        mView = view;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void checkoutPayPal(PayPalCheckoutRequest request) {
        mPetMedsApiServiceRedrirectOff.payPalCheckout(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.getLocalizedMessage());
                        mView.onPayPalError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(Response<String> s) {
                        Log.d("response", s + ">>>" );
                        String loactionHeader = s.headers().get("Location" );
                        if (loactionHeader == null || loactionHeader.isEmpty()) {
                            mView.onPayPalError("" );
                        } else {
                            mView.onSuccess(loactionHeader);
                        }

                    }
                });
    }

    @Override
    public void getDefaultBillingAddress(final String sessionNumber) {
        mPetMedsApiService.getSecurityStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<SecurityStatusResponse, Observable<AddAddressResponse>>() {
                    @Override
                    public Observable<AddAddressResponse> call(SecurityStatusResponse securityStatusResponse) {
                        int securityStatus = securityStatusResponse.getSecurityStatus();
                        if (securityStatus == 2) {
                            return mPetMedsApiService
                                    .getDefaultBillingAddress(sessionNumber)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        } else if (securityStatus == 4 || securityStatus == 5) {
                            return Observable.<AddAddressResponse>just(null);
                        } else {
                            return Observable.error(new Throwable("Please try again" ));
                        }
                    }

                })
                .subscribe(new Subscriber<AddAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (errorId != 0) {
                            mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                        }
                    }

                    @Override
                    public void onNext(AddAddressResponse addAddressResponse) {
                        if (addAddressResponse != null) {
                            if (addAddressResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                if (mView.isActive()) {
                                    if (addAddressResponse.getProfileAddress() != null) {
                                        //perform operation in case when default address is there during soft login
                                        mView.onDefaultBillingAddressSuccess(addAddressResponse);
                                    }else{
                                        //show dialog in case when default address is not there during soft login
                                        mView.showFingerprintDialog();
                                    }
                                }
                            } else {
                                if (mView.isActive()) {
                                    mView.showErrorCrouton(addAddressResponse.getStatus().getErrorMessages().get(0),
                                            false);
                                }
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.hideProgressDailog();
                            }
                        }
                    }
                });
    }


    private Observable<AddEditCardResponse> addOrUpdateCard(CardRequest cardRequest,
            UpdateCardRequest updateCardRequest) {
        //identify whether its a card add request or update card request
        return (cardRequest != null) ? mPetMedsApiService.addPaymentCard(cardRequest)
                : mPetMedsApiService.updateCard(updateCardRequest);
    }

    @Override
    public void applyCreditPaymentMethodOnDefaultBillingAddress(CardRequest cardRequest,
            UpdateCardRequest updateCardRequest) {
        addOrUpdateCard(cardRequest, updateCardRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<AddEditCardResponse, Observable<ShoppingCartListResponse>>() {
                    @Override
                    public Observable<ShoppingCartListResponse> call(AddEditCardResponse addEditCardResponse) {

                        if (addEditCardResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            //create a GuestPaymentRequest
                            CreditCardPaymentMethodRequest creditCardPaymentMethodRequest =
                                    new CreditCardPaymentMethodRequest(
                                            addEditCardResponse.getCard().getId()
                                            , addEditCardResponse.getCard().getBillingAddress().getRepositoryId()
                                            , mPreferencesHelper.getSessionConfirmationResponse()
                                            .getSessionConfirmationNumber()
                                    );

                            return mPetMedsApiService.applyCreditCardPaymentMethod(creditCardPaymentMethodRequest)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        } else {
                            mView.showErrorCrouton(addEditCardResponse.getStatus().getErrorMessages().get(0), false);
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

    @Override
    public void applyCreditCardPaymentMethod(AddressRequest addressRequest, final CardRequest cardRequest, final
    UpdateCardRequest updateCardRequest) {

        mPetMedsApiService.addAddress(addressRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, AddAddressResponse>() {
                    @Override
                    public AddAddressResponse call(Throwable throwable) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(throwable);
                        if (errorId != 0) {
//                            mView.hideProgress();
                            mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(throwable.getLocalizedMessage(), false);
                            }
                        }

                        return null;
                    }
                })
                .flatMap(new Func1<AddAddressResponse, Observable<AddEditCardResponse>>() {
                    @Override
                    public Observable<AddEditCardResponse> call(AddAddressResponse addAddressResponse) {

                        if (addAddressResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {

                            //identify whether its a card add request or update card request
                            if (cardRequest != null) { //add card request

                                //set the received addressId to the card request
                                cardRequest.setBillingAddressId(addAddressResponse.getProfileAddress().getAddressId());

                                return mPetMedsApiService.addPaymentCard(cardRequest)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io());

                            } else { //update card request

                                //set the received addressId to the card request
                                updateCardRequest.setBillingAddressId(
                                        addAddressResponse.getProfileAddress().getAddressId());

                                return mPetMedsApiService.updateCard(updateCardRequest)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io());
                            }

                        } else {
                            //                            mView.hideProgress();
                            mView.showErrorCrouton(addAddressResponse.getStatus().getErrorMessages().get(0), false);
                            return null;
                        }
                    }
                })
                .flatMap(new Func1<AddEditCardResponse, Observable<ShoppingCartListResponse>>() {
                    @Override
                    public Observable<ShoppingCartListResponse> call(AddEditCardResponse addEditCardResponse) {

                        if (addEditCardResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            //create a GuestPaymentRequest
                            CreditCardPaymentMethodRequest creditCardPaymentMethodRequest =
                                    new CreditCardPaymentMethodRequest(
                                            addEditCardResponse.getCard().getId()
                                            , addEditCardResponse.getCard().getBillingAddress().getRepositoryId()
                                            , mPreferencesHelper.getSessionConfirmationResponse()
                                            .getSessionConfirmationNumber()
                                    );

                            return mPetMedsApiService.applyCreditCardPaymentMethod(creditCardPaymentMethodRequest)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io());
                        } else {
                            mView.showErrorCrouton(addEditCardResponse.getStatus().getErrorMessages().get(0), false);
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
