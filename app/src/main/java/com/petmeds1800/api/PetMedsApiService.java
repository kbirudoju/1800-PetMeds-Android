package com.petmeds1800.api;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by Digvijay on 8/10/2016.
 */
public interface PetMedsApiService {

    @Headers({"Request-Credential: pmdevrestapi"})
    @GET("/rest/model/atg/rest/SessionConfirmationActor/getSessionConfirmationNumber")
    Observable<String> getSessionConfirmationNumber();

}
