package com.gazette.app.apirequest;

import com.gazette.app.model.APIResponse;
import com.gazette.app.model.opt.OTPRequestModel;
import com.gazette.app.model.opt.OTPVerificationRequestModel;
import com.gazette.app.model.opt.OTPVerificationResponseModel;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Anil Gudigar on 11/15/2015.
 */
public interface OTPRequestInterface {

    @Headers({"Content-Type: application/json;"})
    @POST("/requestOTP")
    void requestOTP(@Body OTPRequestModel request, Callback<APIResponse> callback);

    @Headers({"Content-Type: application/json;"})
    @POST("/verifyOTP")
    void verifyOTP(@Body OTPVerificationRequestModel request, Callback<OTPVerificationResponseModel> callback);

    @Headers({"Content-Type: application/json;"})
    @GET("/requestUserDetail")
    void requestUserDetail(@Query("number") String number, Callback<OTPVerificationResponseModel> callback);
}
