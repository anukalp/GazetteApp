
package com.gazette.app.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mmt5621 on 11/17/2015.
 */
public class GazetteDatabaseHelper extends SQLiteOpenHelper {
    /**
     * Gazette DB version ranges:
     * 
     * <pre>
     *   1-9 Gazette Test Builds
     * </pre>
     */
    public static final int DATABASE_VERSION = 1;

    public static final String CATEGORY_CONCRETE_ID =
            Tables.CATEGORIES + "." + CategoryColumns._ID;

    public static final String BRAND_CONCRETE_ID =
            Tables.BRAND + "." + BrandColumns._ID;

    public static final String INSURANCE_CONCRETE_ID =
            Tables.INSURANCE + "." + InsuranceColumns._ID;

    public static final String RETAILER_CONCRETE_ID =
            Tables.RETAILER + "." + RetailerColumns._ID;

    public static final String WARRANTY_CONCRETE_ID =
            Tables.WARRANTY + "." + WarrantyColumns._ID;

    public static final String INVOICE_CONCRETE_ID =
            Tables.INVOICE + "." + InvoiceColumns._ID;

    public static final String CATEGORY_CONCRETE_NAME =
            Tables.CATEGORIES + "." + CategoryColumns.NAME;

    public static final String BRAND_CONCRETE_NAME =
            Tables.BRAND + "." + BrandColumns.NAME;

    public static final String INVOICE_CONCRETE_PHOTO =
            Tables.INVOICE + "." + InvoiceColumns.PHOTO;

    public static final String INVOICE_CONCRETE_AMOUNT =
            Tables.INVOICE + "." + InvoiceColumns.AMOUNT;

    public static final String INVOICE_CONCRETE_PLACE =
            Tables.INVOICE + "." + InvoiceColumns.PLACE_PURCHASE;

    public static final String INSURANCE_CONCRETE_STARTDATE =
            Tables.INSURANCE + "." + InsuranceColumns.START_DATE;

    public static final String INSURANCE_CONCRETE_ENDDATE =
            Tables.INSURANCE + "." + InsuranceColumns.END_DATE;

    private static final String WARRANTY_CONCRETE_STARTDATE =
            Tables.WARRANTY + "." + WarrantyColumns.START_DATE;

    private static final String WARRANTY_CONCRETE_ENDDATE =
            Tables.WARRANTY + "." + WarrantyColumns.END_DATE;

    private static final String PRODUCT_CONCRETE_RATING =
            Tables.PRODUCT_INFO + "." + ProductColumns.RATING;

    private static final String PRODUCT_CONCRETE_BRAND_ID =
            Tables.PRODUCT_INFO + "." + ProductColumns.BRAND_ID;

    private static final String PRODUCT_CONCRETE_INOVICE_ID =
            Tables.PRODUCT_INFO + "." + ProductColumns.INOVICE_ID;

    private static final String PRODUCT_CONCRETE_INSURANCE_ID =
            Tables.PRODUCT_INFO + "." + ProductColumns.INSURANCE_ID;

    private static final String INSURANCE_CONCRETE_RETAILER_ID =
            Tables.INSURANCE + "." + InsuranceColumns.RETAILER_ID;

    private static final String PRODUCT_CONCRETE_WARRANTY_ID =
            Tables.PRODUCT_INFO + "." + ProductColumns.WARRANTY_ID;

    private final Context mContext;

    public interface Tables {
        public static final String PRODUCT_INFO = "product_info";

        public static final String WARRANTY = "warranty";

        public static final String INSURANCE = "insurance";

        public static final String BRAND = "brand";

        public static final String CATEGORIES = "category";

        public static final String RETAILER = "retailer";

        public static final String SEARCH_INDEX = "search_index";

        public static final String USER = "user";

        public static final String INVOICE = "invoice";
    }

    public interface Views {
        public static final String PRODUCT_DATA = "view_data";

        public static final String PRODUCT_ISSUER = "view_issuer";

        public static final String PRODUCT_RETAILER = "view_retailer";
    }

    private static final String TAG = "GazetteDatabaseHelper";

    private static final String DATABASE_NAME = "gazette.db";

    private static GazetteDatabaseHelper sSingleton = null;

    public static synchronized GazetteDatabaseHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new GazetteDatabaseHelper(context, DATABASE_NAME);
        }
        return sSingleton;
    }

    public GazetteDatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        mContext = context;
    }

    public SQLiteDatabase getDatabase(boolean writable) {
        return writable ? getWritableDatabase() : getReadableDatabase();
    }

    public interface UserColumns {
        public static final String _ID = "_id";

        public static final String ACCOUNT_NAME = "account_name";

        public static final String ACCOUNT_TYPE = "account_type";

        public static final String DATA_SET = "data_set";
    }

    public interface ProductColumns {
        public static final String _ID = "_id";

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

    public interface InvoiceColumns {
        public static final String _ID = "_id";

        public static final String PHOTO = "photo";

        public static final String PLACE_PURCHASE = "place_purchase";

        public static final String AMOUNT = "amount";

        public static final String PRODUCT_ID = "product_id";

        public static final String RETAILER_ID = "retailer_id";
    }

    public interface RetailerColumns {
        public static final String _ID = "_id";

        public static final String NAME = "name";

        public static final String ADDRESS = "address";

        public static final String RATING = "rating";
    }

    public interface BrandColumns {
        public static final String _ID = "_id";

        public static final String NAME = "name";
    }

    public interface CategoryColumns {
        public static final String _ID = "_id";

        public static final String NAME = "name";
    }

    public interface WarrantyColumns {
        public static final String _ID = "_id";

        public static final String BRAND_ID = "brand_id";

        public static final String START_DATE = "start_date";

        public static final String END_DATE = "end_date";
    }

    public interface InsuranceColumns {
        public static final String _ID = "_id";

        public static final String RETAILER_ID = "retailer_id";

        public static final String BRAND_ID = "brand_id";

        public static final String START_DATE = "start_date";

        public static final String END_DATE = "end_date";
    }

    public interface SearchIndexColumns {
        public static final String CONTACT_ID = "contact_id";

        public static final String CONTENT = "content";

        public static final String NAME = "name";

        public static final String TOKENS = "tokens";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "OnCreate database version: " + DATABASE_VERSION);
        /**
         * CREATE TABLES
         */
        db.execSQL("CREATE TABLE " + Tables.USER + " (" + UserColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UserColumns.ACCOUNT_NAME + " TEXT, "
                + UserColumns.ACCOUNT_TYPE + " TEXT, " + UserColumns.DATA_SET + " TEXT" + ");");
        db.execSQL("CREATE TABLE " + Tables.PRODUCT_INFO + " (" + ProductColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ProductColumns.PRODUCT_CODE + " TEXT, "
                + ProductColumns.BRAND_ID + " INTEGER REFERENCES brand(_id),"
                + ProductColumns.CATEGORY_ID + " INTEGER REFERENCES category(_id),"
                + ProductColumns.USER_ID + " INTEGER REFERENCES user(_id),"
                + ProductColumns.INOVICE_ID + " INTEGER REFERENCES invoice(_id),"
                + ProductColumns.WARRANTY_ID + " INTEGER REFERENCES warranty(_id),"
                + ProductColumns.INSURANCE_ID + " INTEGER REFERENCES insurance(_id),"
                + ProductColumns.BARCODE + " TEXT," + ProductColumns.DATE_OF_PURCHASE + " TEXT,"
                + ProductColumns.RATING + " INTEGER" + ");");
        db.execSQL("CREATE TABLE " + Tables.INVOICE + " (" + InvoiceColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + InvoiceColumns.PHOTO + " BLOB, "
                + InvoiceColumns.PLACE_PURCHASE + " TEXT, " + InvoiceColumns.AMOUNT + " TEXT,"
                + InvoiceColumns.PRODUCT_ID + " INTEGER REFERENCES product(_id),"
                + InvoiceColumns.RETAILER_ID + " INTEGER REFERENCES retailer(_id)" + ");");
        db.execSQL("CREATE TABLE " + Tables.RETAILER + " (" + RetailerColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RetailerColumns.NAME + " TEXT, "
                + InvoiceColumns.AMOUNT + " TEXT," + RetailerColumns.ADDRESS + " TEXT,"
                + RetailerColumns.RATING + " INTEGER REFERENCES retailer(_id)" + ");");
        db.execSQL("CREATE TABLE " + Tables.BRAND + " (" + BrandColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BrandColumns.NAME + " TEXT" + ");");
        db.execSQL("CREATE TABLE " + Tables.CATEGORIES + " (" + CategoryColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CategoryColumns.NAME + " TEXT" + ");");
        db.execSQL("CREATE TABLE " + Tables.WARRANTY + " (" + WarrantyColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WarrantyColumns.BRAND_ID
                + " INTEGER REFERENCES brand(_id), " + WarrantyColumns.START_DATE + " INTEGER, "
                + WarrantyColumns.END_DATE + " INTEGER" + ");");
        db.execSQL("CREATE TABLE " + Tables.INSURANCE + " (" + InsuranceColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + InsuranceColumns.BRAND_ID
                + " INTEGER REFERENCES brand(_id), " + InsuranceColumns.RETAILER_ID
                + " INTEGER REFERENCES retailer(_id), " + InsuranceColumns.START_DATE + " INTEGER, "
                + InsuranceColumns.END_DATE + " INTEGER" + ");");

        /**
         * CREATE INDEXES
         */
        createSearchIndexTable(db);
        db.execSQL("CREATE INDEX product_lookup_index ON " + Tables.PRODUCT_INFO + " ("
                + ProductColumns.RATING + "," + ProductColumns.PRODUCT_CODE + ","
                + ProductColumns.CATEGORY_ID + ");");
        db.execSQL("CREATE INDEX retailer_lookup_index ON " + Tables.RETAILER + " ("
                + RetailerColumns.RATING + ");");

        /**
         * CREATE TRIGGERS
         */
        createUserTriggers(db);
        createProductTriggers(db);

        /**
         * CREATE VIEWS
         */
        createProductView(db);
        // createRetailerView(db);

    }

    private void createProductView(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.PRODUCT_DATA + ";");
        String productSelect = "SELECT " + CATEGORY_CONCRETE_NAME + " AS category, "
                + INVOICE_CONCRETE_PHOTO + " AS photo, " + INVOICE_CONCRETE_AMOUNT + " AS amount, "
                + INVOICE_CONCRETE_PLACE + " AS purchase_place, " + ProductColumns.PRODUCT_CODE + ", "
                + INSURANCE_CONCRETE_STARTDATE + " AS insured_start_date, " + INSURANCE_CONCRETE_ENDDATE + " AS insured_end_date, "
                + WARRANTY_CONCRETE_STARTDATE + " AS start_date, " + WARRANTY_CONCRETE_ENDDATE + " AS end_date, "
                + ProductColumns.BARCODE + ", " + BRAND_CONCRETE_NAME + " AS brand, "
                + ProductColumns.DATE_OF_PURCHASE + ", " + PRODUCT_CONCRETE_RATING + " FROM "
                + Tables.PRODUCT_INFO + " JOIN " + Tables.CATEGORIES + " ON ("
                + ProductColumns.CATEGORY_ID + "=" + CATEGORY_CONCRETE_ID + ")" + " LEFT OUTER JOIN "
                + Tables.BRAND + " ON (" + PRODUCT_CONCRETE_BRAND_ID + "=" + BRAND_CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + Tables.INSURANCE + " ON (" + PRODUCT_CONCRETE_INSURANCE_ID
                + "=" + INSURANCE_CONCRETE_ID + ")" + " LEFT OUTER JOIN " + Tables.RETAILER + " ON ("
                + INSURANCE_CONCRETE_RETAILER_ID + "=" + RETAILER_CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + Tables.WARRANTY + " ON (" + PRODUCT_CONCRETE_WARRANTY_ID + "="
                + WARRANTY_CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + Tables.INVOICE + " ON (" + PRODUCT_CONCRETE_INOVICE_ID
                + "=" + INVOICE_CONCRETE_ID + ")";
        db.execSQL("CREATE VIEW " + Views.PRODUCT_DATA + " AS " + productSelect);
    }

    private void createUserTriggers(SQLiteDatabase db) {
        // Automatically delete Data rows when a raw contact is deleted.
        db.execSQL("DROP TRIGGER IF EXISTS " + Tables.USER + "_deleted;");
        db.execSQL("CREATE TRIGGER " + Tables.USER + "_deleted " + "   BEFORE DELETE ON "
                + Tables.USER + " BEGIN " + " DELETE FROM " + Tables.PRODUCT_INFO + " WHERE "
                + ProductColumns.USER_ID + "=OLD." + UserColumns._ID + ";" + " END");
    }

    private void createProductTriggers(SQLiteDatabase db) {
        // Automatically delete Data rows when a raw contact is deleted.
        db.execSQL("DROP TRIGGER IF EXISTS " + Tables.PRODUCT_INFO + "_deleted;");
        db.execSQL("CREATE TRIGGER " + Tables.PRODUCT_INFO + "_deleted " + "   BEFORE DELETE ON "
                + Tables.PRODUCT_INFO + " BEGIN " + " DELETE FROM " + Tables.INSURANCE + " WHERE "
                + InsuranceColumns._ID + "=OLD." + ProductColumns.INSURANCE_ID + ";"
                + " DELETE FROM " + Tables.WARRANTY + " WHERE " + WarrantyColumns._ID + "=OLD."
                + ProductColumns.WARRANTY_ID + ";" + " DELETE FROM " + Tables.INVOICE + " WHERE "
                + InvoiceColumns._ID + "=OLD." + ProductColumns.INOVICE_ID + ";" + " END");
    }

    public void createSearchIndexTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SEARCH_INDEX);
        db.execSQL("CREATE VIRTUAL TABLE " + Tables.SEARCH_INDEX + " USING FTS4 ("
                + SearchIndexColumns.CONTACT_ID + " INTEGER REFERENCES contacts(_id) NOT NULL,"
                + SearchIndexColumns.CONTENT + " TEXT, " + SearchIndexColumns.NAME + " TEXT, "
                + SearchIndexColumns.TOKENS + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
            Log.i(TAG, "Upgrading to test version " + oldVersion + " to " + newVersion
                    + ", data will be lost!");

            db.execSQL("DROP TABLE IF EXISTS " + Tables.USER + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.INSURANCE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARRANTY + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.CATEGORIES + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.BRAND + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.INVOICE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.PRODUCT_INFO + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.RETAILER + ";");
            db.execSQL("DROP TABLE IF EXISTS " + Tables.SEARCH_INDEX + ";");
            onCreate(db);
            return;
        }
    }
}
