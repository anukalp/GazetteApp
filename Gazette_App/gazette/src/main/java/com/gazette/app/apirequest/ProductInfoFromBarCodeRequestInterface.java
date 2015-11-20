package com.gazette.app.apirequest;

import com.gazette.app.model.productInfo.ProductInfoResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Anil Gudigar on 11/20/2015.
 */
public interface ProductInfoFromBarCodeRequestInterface {

    @GET("/v2/products/{product}")
    public void requestProductInfo(@Path("product") String Product,@Query("apikey") String APIKey, Callback<ProductInfoResponse> callback);
}
