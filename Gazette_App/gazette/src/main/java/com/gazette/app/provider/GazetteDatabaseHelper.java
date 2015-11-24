
package com.gazette.app.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by mmt5621 on 11/17/2015.
 */
public class GazetteDatabaseHelper extends SQLiteOpenHelper {
    /**
     * Gazette DB version ranges:
     * <p/>
     * <pre>
     *   1-9 Gazette Test Builds
     * </pre>
     */
    public static final int DATABASE_VERSION = 1;

    public static final String CATEGORY_CONCRETE_ID =
            GazetteContracts.Category.TABLE_NAME + "." + GazetteContracts.Category._ID;

    public static final String BRAND_CONCRETE_ID =
            GazetteContracts.Brand.TABLE_NAME + "." + GazetteContracts.Brand._ID;

    public static final String INSURANCE_CONCRETE_ID =
            GazetteContracts.Insurance.TABLE_NAME + "." + GazetteContracts.Insurance._ID;

    public static final String RETAILER_CONCRETE_ID =
            GazetteContracts.Retailer.TABLE_NAME + "." + GazetteContracts.Retailer._ID;

    public static final String WARRANTY_CONCRETE_ID =
            GazetteContracts.Warranty.TABLE_NAME + "." + GazetteContracts.Warranty._ID;

    public static final String INVOICE_CONCRETE_ID =
            GazetteContracts.Invoice.TABLE_NAME + "." + GazetteContracts.Invoice._ID;

    public static final String CATEGORY_CONCRETE_NAME =
            GazetteContracts.Category.TABLE_NAME + "." + GazetteContracts.Category.NAME;

    public static final String BRAND_CONCRETE_NAME =
            GazetteContracts.Brand.TABLE_NAME + "." + GazetteContracts.Brand.NAME;

    public static final String INVOICE_CONCRETE_PHOTO =
            GazetteContracts.Invoice.TABLE_NAME + "." + GazetteContracts.Invoice.PHOTO;

    public static final String INVOICE_CONCRETE_AMOUNT =
            GazetteContracts.Invoice.TABLE_NAME + "." + GazetteContracts.Invoice.AMOUNT;

    public static final String INVOICE_CONCRETE_PLACE =
            GazetteContracts.Invoice.TABLE_NAME + "." + GazetteContracts.Invoice.PLACE_PURCHASE;

    public static final String INSURANCE_CONCRETE_STARTDATE =
            GazetteContracts.Insurance.TABLE_NAME + "." + GazetteContracts.Insurance.START_DATE;

    public static final String INSURANCE_CONCRETE_ENDDATE =
            GazetteContracts.Insurance.TABLE_NAME + "." + GazetteContracts.Insurance.END_DATE;

    private static final String WARRANTY_CONCRETE_STARTDATE =
            GazetteContracts.Warranty.TABLE_NAME + "." + GazetteContracts.Warranty.START_DATE;

    private static final String WARRANTY_CONCRETE_ENDDATE =
            GazetteContracts.Warranty.TABLE_NAME + "." + GazetteContracts.Warranty.END_DATE;

    private static final String PRODUCT_CONCRETE_RATING =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.RATING;

    private static final String PRODUCT_CONCRETE_BRAND_ID =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.BRAND_ID;

    private static final String PRODUCT_CONCRETE_INOVICE_ID =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.INOVICE_ID;

    private static final String PRODUCT_CONCRETE_INSURANCE_ID =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.INSURANCE_ID;

    private static final String INSURANCE_CONCRETE_RETAILER_ID =
            GazetteContracts.Insurance.TABLE_NAME + "." + GazetteContracts.Insurance.RETAILER_ID;

    private static final String PRODUCT_CONCRETE_WARRANTY_ID =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.WARRANTY_ID;

    private static final String PRODUCT_CONCRETE_NAME =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.NAME;

    private static final String PRODUCT_CONCRETE_CODE =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info.PRODUCT_CODE;

    private static final String PRODUCT_CONCRETE_ID =
            GazetteContracts.Product_Info.TABLE_NAME + "." + GazetteContracts.Product_Info._ID;

    private final Context mContext;


    public interface Views {
        public static final String PRODUCT_DATA = "view_data";
        public static final Uri PRODUCT_DATA_CONTENT_URI = Uri.withAppendedPath(
                GazetteContracts.CONTENT_URI, "product_data");

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "OnCreate database version: " + DATABASE_VERSION);
        /**
         * CREATE TABLES
         */
        db.execSQL("CREATE TABLE " + GazetteContracts.User.TABLE_NAME + " (" + GazetteContracts.User._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.User.ACCOUNT_NAME + " TEXT, "
                + GazetteContracts.User.ACCOUNT_TYPE + " TEXT, " + GazetteContracts.User.DATA_SET + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Product_Info.TABLE_NAME + " (" + GazetteContracts.Product_Info._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Product_Info.PRODUCT_CODE + " TEXT, "
                + GazetteContracts.Product_Info.NAME + " TEXT, "
                + GazetteContracts.Product_Info.BRAND_ID + " INTEGER REFERENCES brand(_id),"
                + GazetteContracts.Product_Info.CATEGORY_ID + " INTEGER REFERENCES category(_id),"
                + GazetteContracts.Product_Info.USER_ID + " INTEGER REFERENCES user(_id),"
                + GazetteContracts.Product_Info.INOVICE_ID + " INTEGER REFERENCES invoice(_id),"
                + GazetteContracts.Product_Info.WARRANTY_ID + " INTEGER REFERENCES warranty(_id),"
                + GazetteContracts.Product_Info.INSURANCE_ID + " INTEGER REFERENCES insurance(_id),"
                + GazetteContracts.Product_Info.BARCODE + " TEXT," + GazetteContracts.Product_Info.DATE_OF_PURCHASE + " TEXT,"
                + GazetteContracts.Product_Info.RATING + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Invoice.TABLE_NAME + " (" + GazetteContracts.Invoice._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Invoice.PHOTO + " BLOB, "
                + GazetteContracts.Invoice.PLACE_PURCHASE + " TEXT, " + GazetteContracts.Invoice.AMOUNT + " TEXT,"
                + GazetteContracts.Invoice.PRODUCT_ID + " INTEGER REFERENCES product(_id),"
                + GazetteContracts.Invoice.RETAILER_ID + " INTEGER REFERENCES retailer(_id)" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Retailer.TABLE_NAME + " (" + GazetteContracts.Retailer._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Retailer.NAME + " TEXT, "
                + GazetteContracts.Retailer.ADDRESS + " TEXT,"
                + GazetteContracts.Retailer.RATING + " INTEGER REFERENCES retailer(_id)" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Brand.TABLE_NAME + " (" + GazetteContracts.Brand._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Brand.NAME + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Category.TABLE_NAME + " (" + GazetteContracts.Category._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Category.NAME + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Warranty.TABLE_NAME + " (" + GazetteContracts.Warranty._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Warranty.BRAND_ID
                + " INTEGER REFERENCES brand(_id), " + GazetteContracts.Warranty.START_DATE + " INTEGER, "
                + GazetteContracts.Warranty.END_DATE + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + GazetteContracts.Insurance.TABLE_NAME + " (" + GazetteContracts.Insurance._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GazetteContracts.Insurance.BRAND_ID
                + " INTEGER REFERENCES brand(_id), " + GazetteContracts.Insurance.RETAILER_ID
                + " INTEGER REFERENCES retailer(_id), " + GazetteContracts.Insurance.START_DATE + " INTEGER, "
                + GazetteContracts.Insurance.END_DATE + " INTEGER" + ");");

        /**
         * CREATE INDEXES
         */
        createSearchIndexTable(db);
        db.execSQL("CREATE INDEX product_lookup_index ON " + GazetteContracts.Product_Info.TABLE_NAME + " ("
                + GazetteContracts.Product_Info.RATING + "," + GazetteContracts.Product_Info.PRODUCT_CODE + ","
                + GazetteContracts.Product_Info.CATEGORY_ID + ");");
        db.execSQL("CREATE INDEX retailer_lookup_index ON " + GazetteContracts.Retailer.TABLE_NAME + " ("
                + GazetteContracts.Retailer.RATING + ");");

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

    public void createProductView(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.PRODUCT_DATA + ";");
        String productSelect = "SELECT " + CATEGORY_CONCRETE_ID + " AS category_id, " + PRODUCT_CONCRETE_ID + " AS product_id, " + CATEGORY_CONCRETE_NAME + " AS category, "
                + INVOICE_CONCRETE_PHOTO + " AS photo, " + INVOICE_CONCRETE_AMOUNT + " AS amount, "
                + INVOICE_CONCRETE_PLACE + " AS purchase_place, " + PRODUCT_CONCRETE_NAME + " AS product_name, " + PRODUCT_CONCRETE_CODE + " AS product_code, "
                + INSURANCE_CONCRETE_STARTDATE + " AS insured_start_date, " + INSURANCE_CONCRETE_ENDDATE + " AS insured_end_date, "
                + WARRANTY_CONCRETE_STARTDATE + " AS start_date, " + WARRANTY_CONCRETE_ENDDATE + " AS end_date, "
                + GazetteContracts.Product_Info.BARCODE + ", " + BRAND_CONCRETE_NAME + " AS brand, "
                + GazetteContracts.Product_Info.DATE_OF_PURCHASE + ", " + PRODUCT_CONCRETE_RATING + " FROM "
                + GazetteContracts.Product_Info.TABLE_NAME + " JOIN " + GazetteContracts.Category.TABLE_NAME + " ON ("
                + GazetteContracts.Product_Info.CATEGORY_ID + "=" + CATEGORY_CONCRETE_ID + ")" + " LEFT OUTER JOIN "
                + GazetteContracts.Brand.TABLE_NAME + " ON (" + PRODUCT_CONCRETE_BRAND_ID + "=" + BRAND_CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + GazetteContracts.Insurance.TABLE_NAME + " ON (" + PRODUCT_CONCRETE_INSURANCE_ID
                + "=" + INSURANCE_CONCRETE_ID + ")" + " LEFT OUTER JOIN " + GazetteContracts.Retailer.TABLE_NAME + " ON ("
                + INSURANCE_CONCRETE_RETAILER_ID + "=" + RETAILER_CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + GazetteContracts.Warranty.TABLE_NAME + " ON (" + PRODUCT_CONCRETE_WARRANTY_ID + "="
                + WARRANTY_CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + GazetteContracts.Invoice.TABLE_NAME + " ON (" + PRODUCT_CONCRETE_INOVICE_ID
                + "=" + INVOICE_CONCRETE_ID + ")";
        db.execSQL("CREATE VIEW " + Views.PRODUCT_DATA + " AS " + productSelect);
    }

    private void createUserTriggers(SQLiteDatabase db) {
        // Automatically delete Data rows when a raw contact is deleted.
        db.execSQL("DROP TRIGGER IF EXISTS " + GazetteContracts.User.TABLE_NAME + "_deleted;");
        db.execSQL("CREATE TRIGGER " + GazetteContracts.User.TABLE_NAME + "_deleted " + "   BEFORE DELETE ON "
                + GazetteContracts.User.TABLE_NAME + " BEGIN " + " DELETE FROM " + GazetteContracts.Product_Info.TABLE_NAME + " WHERE "
                + GazetteContracts.Product_Info.USER_ID + "=OLD." + GazetteContracts.User._ID + ";" + " END");
    }

    private void createProductTriggers(SQLiteDatabase db) {
        // Automatically delete Data rows when a raw contact is deleted.
        db.execSQL("DROP TRIGGER IF EXISTS " + GazetteContracts.Product_Info.TABLE_NAME + "_deleted;");
        db.execSQL("CREATE TRIGGER " + GazetteContracts.Product_Info.TABLE_NAME + "_deleted " + "   BEFORE DELETE ON "
                + GazetteContracts.Product_Info.TABLE_NAME + " BEGIN " + " DELETE FROM " + GazetteContracts.Insurance.TABLE_NAME + " WHERE "
                + GazetteContracts.Insurance._ID + "=OLD." + GazetteContracts.Product_Info.INSURANCE_ID + ";"
                + " DELETE FROM " + GazetteContracts.Warranty.TABLE_NAME + " WHERE " + GazetteContracts.Warranty._ID + "=OLD."
                + GazetteContracts.Product_Info.WARRANTY_ID + ";" + " DELETE FROM " + GazetteContracts.Insurance.TABLE_NAME + " WHERE "
                + GazetteContracts.Invoice._ID + "=OLD." + GazetteContracts.Product_Info.INOVICE_ID + ";" + " END");
    }

    public void createSearchIndexTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.SearchIndex.TABLE_NAME);
        db.execSQL("CREATE VIRTUAL TABLE " + GazetteContracts.SearchIndex.TABLE_NAME + " USING FTS4 ("
                + GazetteContracts.SearchIndex.CONTACT_ID + " INTEGER REFERENCES contacts(_id) NOT NULL,"
                + GazetteContracts.SearchIndex.CONTENT + " TEXT, " + GazetteContracts.SearchIndex.NAME + " TEXT, "
                + GazetteContracts.SearchIndex.TOKENS + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
            Log.i(TAG, "Upgrading to test version " + oldVersion + " to " + newVersion
                    + ", data will be lost!");

            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.User.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Insurance.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Warranty.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Category.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Brand.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Invoice.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Product_Info.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.Retailer.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + GazetteContracts.SearchIndex.TABLE_NAME + ";");
            onCreate(db);
            return;
        }
    }
}
