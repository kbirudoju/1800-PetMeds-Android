package com.petmeds1800.api;

import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.model.entities.SessionConfNumberResponse;

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
    @GET("rest/model/atg/userprofiling/ProfileActor/orderHistory")
    Observable<MyOrder> getOrderList(@Query("_dynSessConf") String sessionConfirmation , @Query("filterId") String filterId);

    @Headers({"Content-Type: application/json", "Request-Credential: pmdevrestapi"})
    @GET("rest/model/atg/userprofiling/ProfileActor/orderHistoryFilterlist")
    Observable<OrderHistoryFilter> getOrderHistoryFilter(@Query("_dynSessConf") String sessionConfirmation);

}
