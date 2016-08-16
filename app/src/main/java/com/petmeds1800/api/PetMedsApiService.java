package com.petmeds1800.api;

import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.SessionConfNumberResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
}
