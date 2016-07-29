package com.dminc.application;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Konrad
 */
public class MockCallInterceptor implements okhttp3.Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String responseString = "Mock Lorem Ipsum";
        return new Response.Builder()
                .code(200)
                .request(chain.request())
                .message(responseString)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString))
                .build();
    }
}
