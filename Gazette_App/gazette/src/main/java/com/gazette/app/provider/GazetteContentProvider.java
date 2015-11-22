package com.gazette.app.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.gazette.app.R;
import com.gazette.app.utils.GazeteDBUtils;

public class GazetteContentProvider extends ContentProvider {

    private static final int PRODUCT = 1;
    private static final int CATEGORIES = 2;
    private static final int BRANDS = 3;
    private static final int INSURANCE = 4;
    private static final int RETAILER = 5;
    private static final int INVOICE = 6;
    private static final int WARRANTY = 7;

    private static final int PRODUCT_VIEW = 8;
    private static final int BACKGROUND_TASK_FILL_INSURANCE = 1;
    private static final int BACKGROUND_TASK_FILL_BRANDS = 2;
    private static final int BACKGROUND_TASK_FILL_CATEGORIES = 3;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private GazetteDatabaseHelper mDatabaseHelper;
    private static final UriMatcher URI_MATCHER;

    static {
        // Contacts URI matching table
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        // DO NOT use constants such as Contacts.CONTENT_URI here.  This is the only place
        // where one can see all supported URLs at a glance, and using constants will reduce
        // readability.
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "product_info", PRODUCT);
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "category", CATEGORIES);
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "brand", BRANDS);
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "retailer", RETAILER);
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "insurance", INSURANCE);
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "invoice", INVOICE);
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "warranty", WARRANTY);

        //Views
        URI_MATCHER.addURI(GazetteContracts.AUTHORITY, "product_data", PRODUCT_VIEW);

    }

    public GazetteContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        initialize();
        return true;
    }

    private void initialize() {
        mDatabaseHelper = GazetteDatabaseHelper.getInstance(getContext());
        mBackgroundThread = new HandlerThread("GazetteContentProvider",
                Process.THREAD_PRIORITY_BACKGROUND);
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                performBackgroundTask(msg.what, msg.obj);
            }
        };
        scheduleBackgroundTask(BACKGROUND_TASK_FILL_INSURANCE);
        scheduleBackgroundTask(BACKGROUND_TASK_FILL_CATEGORIES);
        scheduleBackgroundTask(BACKGROUND_TASK_FILL_BRANDS);

    }

    protected void scheduleBackgroundTask(int task) {
        Log.i("Anil", "scheduleBackgroundTask Fill" + task);
        mBackgroundHandler.sendEmptyMessage(task);
    }

    private void performBackgroundTask(int what, Object obj) {
        switch (what) {
            case BACKGROUND_TASK_FILL_CATEGORIES:
                Cursor cursor = GazeteDBUtils.getCategory(getContext());
                if (null != cursor && cursor.getCount() >0){
                    Log.i("Anil", "Categories already loaded");
                }else{
                    Log.i("Anil", "Categories Fill");
                    String[] Categories = getContext().getResources().getStringArray(R.array.categories);
                    GazeteDBUtils.loadCategory(getContext(), Categories);
                }
                break;
        }

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        long id;
        Uri InsertedURI = null;
        switch (URI_MATCHER.match(uri)) {
            case BRANDS:
                id = sqLiteDatabase.insert(GazetteContracts.Brand.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
            case CATEGORIES:
                id = sqLiteDatabase.insert(GazetteContracts.Category.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
            case INSURANCE:
                id = sqLiteDatabase.insert(GazetteContracts.Insurance.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
            case PRODUCT:
                id = sqLiteDatabase.insert(GazetteContracts.Product_Info.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
            case RETAILER:
                id = sqLiteDatabase.insert(GazetteContracts.Retailer.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
            case WARRANTY:
                id = sqLiteDatabase.insert(GazetteContracts.Warranty.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
            case INVOICE:
                id = sqLiteDatabase.insert(GazetteContracts.Invoice.TABLE_NAME, null,
                        values);
                InsertedURI = getUriForId(id, uri);
                getContext().getContentResolver().notifyChange(InsertedURI, null);
                return InsertedURI;
        }
        return InsertedURI;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        Cursor cursor = null;
        switch (URI_MATCHER.match(uri)) {
            case BRANDS:
                sqLiteQueryBuilder.setTables(GazetteContracts.Brand.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Brand.CONTENT_URI);
                break;
            case PRODUCT:
                sqLiteQueryBuilder.setTables(GazetteContracts.Product_Info.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Product_Info.CONTENT_URI);
                break;
            case CATEGORIES:
                sqLiteQueryBuilder.setTables(GazetteContracts.Category.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Category.CONTENT_URI);
                break;
            case INVOICE:
                sqLiteQueryBuilder.setTables(GazetteContracts.Invoice.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Invoice.CONTENT_URI);
                break;
            case INSURANCE:
                sqLiteQueryBuilder.setTables(GazetteContracts.Insurance.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Insurance.CONTENT_URI);
                break;
            case RETAILER:
                sqLiteQueryBuilder.setTables(GazetteContracts.Retailer.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Retailer.CONTENT_URI);
                break;
            case WARRANTY:
                sqLiteQueryBuilder.setTables(GazetteContracts.Warranty.TABLE_NAME);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        GazetteContracts.Warranty.CONTENT_URI);
                break;

        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        throw new SQLException("Problem while inserting into uri: " + uri);
    }
}
