package com.petmeds1800.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/api/plaintext/{paragraphs}")
    Call<String> getLorem(@Path("paragraphs") int paragraphs);
}
