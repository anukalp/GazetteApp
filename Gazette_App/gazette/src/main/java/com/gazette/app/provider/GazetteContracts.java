package com.gazette.app.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anil Gudigar on 9/1/2015.
 */
public class GazetteContracts {
    public static final String AUTHORITY = "com.gazette.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public GazetteContracts() {
    }

    /* Inner class that defines the table contents */
    public static abstract class User implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "user");
        public static final String TABLE_NAME = "user";
        public static final String ACCOUNT_NAME = "account_name";
        public static final String ACCOUNT_TYPE = "account_type";
        public static final String DATA_SET = "data_set";
    }

    public static abstract class Product_Info implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "product_info");
        public static final String TABLE_NAME = "product_info";
        public static final String NAME = "name";
        public static final String PRODUCT_CODE = "code";
        public static final String BRAND_ID = "brand_id";
        public static final String CATEGORY_ID = "category_id";
        public static final String USER_ID = "user_id";
        public static final String INOVICE_ID = "invoice_id";
        public static final String BARCODE = "barcode";
        public static final String DATE_OF_PURCHASE = "purchase_date";
        public static final String WARRANTY_ID = "warranty_id";
        public static final String INSURANCE_ID = "insurance_id";
        public static final String RATING = "rating";
    }

    public static abstract class Invoice implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "invoice");
        public static final String TABLE_NAME = "invoice";
        public static final String PHOTO = "photo";
        public static final String PLACE_PURCHASE = "place_purchase";
        public static final String AMOUNT = "amount";
        public static final String PRODUCT_ID = "product_id";
        public static final String RETAILER_ID = "retailer_id";
    }

    public static abstract class Retailer implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "retailer");
        public static final String TABLE_NAME = "retailer";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String RATING = "rating";
    }

    public static abstract class SearchIndex implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "search_index");
        public static final String TABLE_NAME = "search_index";
        public static final String CONTACT_ID = "contact_id";
        public static final String CONTENT = "content";
        public static final String NAME = "name";
        public static final String TOKENS = "tokens";
    }



    public static abstract class Brand implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "brand");
        public static final String TABLE_NAME = "brand";
        public static final String NAME = "name";
    }

    public static abstract class Warranty implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "warranty");
        public static final String TABLE_NAME = "warranty";
        public static final String BRAND_ID = "brand_id";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
    }


    public static abstract class Category implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "category");
        public static final String TABLE_NAME = "category";
        public static final String NAME = "name";
    }

    public static abstract class Insurance implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "insurance");
        public static final String TABLE_NAME = "insurance";
        public static final String RETAILER_ID = "retailer_id";
        public static final String BRAND_ID = "brand_id";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
    }

}
