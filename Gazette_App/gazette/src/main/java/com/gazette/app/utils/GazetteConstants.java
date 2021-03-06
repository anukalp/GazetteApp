package com.gazette.app.utils;

/**
 * Created by Anil Gudigar on 10/31/2015.
 */
public class GazetteConstants {
    public static final String SCAN_MODES = "SCAN_MODES";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String SCAN_RESULT_TYPE = "SCAN_RESULT_TYPE";
    public static final String ERROR_INFO = "ERROR_INFO";
    public static final String SMS_ORIGIN = "GAZETE";
    public static final String OTP_DELIMITER = "\\)";

    public static final String PRODUCT_ID = "product_id";
    public static final String COLLECTION_ID = "collectionid";

    public static final String SERVICE_API_BASE_URL = "http://ec2-52-11-139-107.us-west-2.compute.amazonaws.com/GazetteWebApp/rest";

    public static final String OUTPAN_SERVICE_API_BASE_URL = "https://api.outpan.com/";
    public static final String OUTPAN_APIKEY = "840331c0a3841485828e10b7e521dbf4";

    public static class UserDetails {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
        public static final String OTP = "otp";
    }

    public static class Jabber {
        public static final String HOST ="ec2-52-11-139-107.us-west-2.compute.amazonaws.com";
        public static final String DOMAIN ="ec2-52-11-139-107.us-west-2.compute.amazonaws.com";
        public static final int PORT =5222;
    }
}
