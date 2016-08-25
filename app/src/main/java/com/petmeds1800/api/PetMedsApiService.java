package com.petmeds1800.api;

import com.petmeds1800.model.entities.AddACardResponse;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.ForgotPasswordRequest;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.MySavedAddress;
import com.petmeds1800.model.entities.MySavedCard;
import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.model.entities.PetList;
import com.petmeds1800.model.entities.Profile;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.UpdateAccountSettingsResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Digvijay on 8/10/2016.
 */
public interface PetMedsApiService {

    @Headers({"Request-Credential: pmdevrestapi"})
    @GET("/rest/model/atg/rest/SessionConfirmationActor/getSessionConfirmationNumber")
    Observable<SessionConfNumberResponse> getSessionConfirmationNumber();

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @POST("/rest/model/atg/userprofiling/ProfileActor/login")
    Observable<String> login(@Body LoginRequest loginRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @POST("/rest/model/atg/userprofiling/ProfileActor/forgotPasswordEmail")
    Observable<String> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("rest/model/atg/userprofiling/ProfileActor/orderHistory")
    Observable<MyOrder> getOrderList(@Query("_dynSessConf") String sessionConfirmation,
            @Query("filterId") String filterId);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("rest/model/atg/userprofiling/ProfileActor/orderHistoryFilterlist")
    Observable<OrderHistoryFilter> getOrderHistoryFilter(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @POST("/rest/model/1800petmeds/payment/PaymentActor/add")
    Observable<AddACardResponse> addPaymentCard(@Body CardRequest cardRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("/rest/model/1800petmeds/payment/PaymentActor/list")
    Observable<MySavedCard> getSavedCards(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("/rest/model/atg/userprofiling/ProfileActor/summary")
    Observable<Profile> getAccountSettings(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @POST("/rest/model/atg/userprofiling/ProfileActor/update")
    Observable<UpdateAccountSettingsResponse> updateAccountSettings(
            @Body UpdateAccountSettingsRequest updateAccountSettingsRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("/rest/model/1800petmeds/contact/ContactActor/list")
    Observable<MySavedAddress> getSavedAddress(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @POST("/rest/model/1800petmeds/contact/ContactActor/add")
    Observable<AddAddressResponse> addAddress(@Body AddressRequest addressRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("/rest/model/1800petmeds/pet/PetActor/list")
    Observable<PetList> getPetList();

}