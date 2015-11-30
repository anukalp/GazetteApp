package com.gazette.app.apirequest;

import com.gazette.app.model.opt.OTPRequestModel;
import com.gazette.app.model.opt.OTPVerificationRequestModel;
import com.gazette.app.model.opt.OTPVerificationResponseModel;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Anil Gudigar on 11/15/2015.
 */
public interface OTPRequestInterface {

    @Headers({"Content-Type: application/json;"})
    @POST("/requestOTP")
    public void requestOTP(@Body OTPRequestModel request, Callback<JSONObject> callback);

    @Headers({"Content-Type: application/json;"})
    @POST("/verifyOTP")
    public void verifyOTP(@Body OTPVerificationRequestModel request, Callback<OTPVerificationResponseModel> callback);
}
