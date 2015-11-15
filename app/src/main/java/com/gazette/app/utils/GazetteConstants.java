package com.gazette.app.utils;

/**
 * Created by Anil Gudigar on 10/31/2015.
 */
public class GazetteConstants {

    public static final String shopify_api_key = "7c66424a218f35d6fc804f866a9557a4";
    public static final String shopify_shared_secret = "d3babb5fe4124e552b1130f88471d76e";
    public static final String shopify_passowrd = "a665122fe77c6c772c5a916837bbdbc4";
    public static final String shopify_shop_name = "coolgudz-com";
    public static final String dot_myshopify_dot_com = ".myshopify.com";

    public static final String SCAN_MODES = "SCAN_MODES";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String SCAN_RESULT_TYPE = "SCAN_RESULT_TYPE";
    public static final String ERROR_INFO = "ERROR_INFO";

    public static final String PRODUCT_ID = "product_id";

    public static final String BASE_URL = "https://" + shopify_api_key + ":" + shopify_passowrd + "@" + shopify_shop_name + dot_myshopify_dot_com + "/admin/";
    public static final String ALL_PRODUCTS_URL = BASE_URL + "products.json";
    public static final int REQUEST_TIMEOUT_MS = 10000;
    public final static int CACHE_SIZE = 1024;

    public static final String COLLECTION_ID = "collection_id";
    public static class RequestTypes {
        public static final int LIST_GET_ALL_PRODUCT = 0;
    }
}
