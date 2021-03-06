package com.gazette.app.utils;

import android.content.Context;
import android.util.Log;

import com.gazette.app.apirequest.OTPRequestInterface;
import com.gazette.app.apirequest.ProductInfoFromBarCodeRequestInterface;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Anil Gudigar on 11/15/2015.
 */
public class RetrofitManagerClass {

    private RequestInterceptor mRequestInterceptor;
    private RestAdapter mRestAdapter;
    private RestAdapter mUPCInfoRestAdapter;
    private ProductInfoFromBarCodeRequestInterface mProductInfoFromBarCodeRequestInterface;
    private OTPRequestInterface mOtpRequestInterface;
    private SharedPreferenceManager pref;

    public RetrofitManagerClass(Context mContext) {
        pref = new SharedPreferenceManager(mContext);
        Log.d("Anil", "Access Token = " + pref.getUserToken() + " Mobile = " + pref.getMobileNumber());
        mRequestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("accept", "application/json");
            }
        };
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(GazetteConstants.SERVICE_API_BASE_URL)
                .setRequestInterceptor(mRequestInterceptor)
                .build();

        mOtpRequestInterface =
                mRestAdapter.create(OTPRequestInterface.class);

        mUPCInfoRestAdapter = new RestAdapter.Builder()
                .setEndpoint(GazetteConstants.OUTPAN_SERVICE_API_BASE_URL)
                .setRequestInterceptor(mRequestInterceptor)
                .build();

        mProductInfoFromBarCodeRequestInterface = mUPCInfoRestAdapter.create(ProductInfoFromBarCodeRequestInterface.class);
    }

    public OTPRequestInterface getmOtpRequestInterface() {
        return mOtpRequestInterface;
    }

    public ProductInfoFromBarCodeRequestInterface getmProductInfoFromBarCodeRequestInterface() {
        return mProductInfoFromBarCodeRequestInterface;
    }


}
