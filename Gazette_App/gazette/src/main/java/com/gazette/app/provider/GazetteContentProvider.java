package com.gazette.app.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

public class GazetteContentProvider extends ContentProvider {

    private static final int PRODUCT = 1;
    private static final int CATEGORIES = 2;
    private static final int BRANDS = 3;
    private static final int INSURANCE = 4;
    private static final int PRODUCT_VIEW = 5;
    private static final int BACKGROUND_TASK_FILL_INSURANCE = 1;
    private static final int BACKGROUND_TASK_FILL_BRANDS = 2;
    private static final int BACKGROUND_TASK_FILL_CATEGORIES = 3;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private GazetteDatabaseHelper mDatabaseHelper;

    private class GazetteContract {
        public static final String AUTHORITY = "com.gazette.provider";
    }

    static {
        // Contacts URI matching table
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // DO NOT use constants such as Contacts.CONTENT_URI here.  This is the only place
        // where one can see all supported URLs at a glance, and using constants will reduce
        // readability.
        matcher.addURI(GazetteContract.AUTHORITY, "product", PRODUCT);
        matcher.addURI(GazetteContract.AUTHORITY, "categories", CATEGORIES);
        matcher.addURI(GazetteContract.AUTHORITY, "brands", BRANDS);
        matcher.addURI(GazetteContract.AUTHORITY, "insurance", INSURANCE);
        matcher.addURI(GazetteContract.AUTHORITY, "product_data", PRODUCT_VIEW);
    }

    public GazetteContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
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
        mBackgroundHandler.sendEmptyMessage(task);
    }

    private void performBackgroundTask(int what, Object obj) {
        SQLiteDatabase db = mDatabaseHelper.getDatabase(true);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
