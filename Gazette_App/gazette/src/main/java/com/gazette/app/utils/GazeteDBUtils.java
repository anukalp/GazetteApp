package com.gazette.app.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.gazette.app.model.Product;
import com.gazette.app.provider.GazetteContracts;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by Anil Gudigar on 11/22/2015.
 */
public class GazeteDBUtils {

    public static boolean loadCategory(Context context, String[] Categories) {
        for (String category : Categories) {
            ContentValues contentValues = new ContentValues();
            Log.i("Anil", "Category:" + category);
            contentValues.put(GazetteContracts.Category.NAME, category);
            Uri uri = context.getContentResolver().insert(GazetteContracts.Category.CONTENT_URI, contentValues);
            if (null != uri)
                Log.i("Anil", "URI :" + uri.toString());
        }
        return true;
    }

    public static Cursor getCategory(Context context) {
        return context.getContentResolver().query(GazetteContracts.Category.CONTENT_URI, null, null, null, null);
    }

    public static Cursor getCategoryID(Context context, String Category) {
        String[] projection = new String[]{GazetteContracts.Category._ID,
                GazetteContracts.Category.NAME};
        String selection = GazetteContracts.Category.NAME + " = ?";
        String[] selectionArgs = {Category};
        return context.getContentResolver().query(GazetteContracts.Category.CONTENT_URI, projection, selection, selectionArgs, null);
    }


    public static Cursor getProductByCategory(Context context, Long category_ID) {
        String[] projection = new String[]{GazetteContracts.Product_Info._ID,
                GazetteContracts.Product_Info.NAME};
        String selection = GazetteContracts.Product_Info.CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(category_ID)};
        return context.getContentResolver().query(GazetteContracts.Product_Info.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    public static Uri addRetailer(Context context, String name, String address, String rating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GazetteContracts.Retailer.NAME, name);
        contentValues.put(GazetteContracts.Retailer.ADDRESS, name);
        contentValues.put(GazetteContracts.Retailer.RATING, name);
        Uri uri = context.getContentResolver().insert(GazetteContracts.Retailer.CONTENT_URI, contentValues);
        if (null != uri)
            Log.i("Anil", "Retailer URI :" + uri.toString());
        return uri;
    }


    public static Uri addInvoice(Context context, byte[] photo, String place_of_purchase, String ammount, long product_id, long reatiler_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GazetteContracts.Invoice.PHOTO, photo);
        contentValues.put(GazetteContracts.Invoice.PLACE_PURCHASE, place_of_purchase);
        contentValues.put(GazetteContracts.Invoice.AMOUNT, ammount);
        contentValues.put(GazetteContracts.Invoice.PRODUCT_ID, product_id);
        contentValues.put(GazetteContracts.Invoice.RETAILER_ID, reatiler_id);

        Uri uri = context.getContentResolver().insert(GazetteContracts.Invoice.CONTENT_URI, contentValues);
        if (null != uri)
            Log.i("Anil", "Invoice URI :" + uri.toString());
        return uri;
    }

    public static Uri addWarranty(Context context, long brand_id, String StartDate, String EndDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GazetteContracts.Warranty.BRAND_ID, brand_id);
        contentValues.put(GazetteContracts.Warranty.START_DATE, StartDate);
        contentValues.put(GazetteContracts.Warranty.END_DATE, EndDate);
        Uri uri = context.getContentResolver().insert(GazetteContracts.Warranty.CONTENT_URI, contentValues);
        if (null != uri)
            Log.i("Anil", "Retailer URI :" + uri.toString());
        return uri;
    }


    public static Uri addProduct(Context context, String name, String serialnumber, long brand_id, long category_id, long user_id, long invoice_id, long warranty_id, String barcode, String PurchaseDate, int rating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GazetteContracts.Product_Info.NAME, name);
        contentValues.put(GazetteContracts.Product_Info.PRODUCT_CODE, serialnumber);
        contentValues.put(GazetteContracts.Product_Info.BRAND_ID, brand_id);
        contentValues.put(GazetteContracts.Product_Info.CATEGORY_ID, category_id);
        contentValues.put(GazetteContracts.Product_Info.USER_ID, user_id);
        contentValues.put(GazetteContracts.Product_Info.INOVICE_ID, invoice_id);
        contentValues.put(GazetteContracts.Product_Info.WARRANTY_ID, warranty_id);
        contentValues.put(GazetteContracts.Product_Info.BARCODE, barcode);
        contentValues.put(GazetteContracts.Product_Info.DATE_OF_PURCHASE, PurchaseDate);
        contentValues.put(GazetteContracts.Product_Info.RATING, rating);

        Uri uri = context.getContentResolver().insert(GazetteContracts.Product_Info.CONTENT_URI, contentValues);
        if (null != uri)
            Log.i("Anil", "Retailer URI :" + uri.toString());
        return uri;
    }

    public static Uri persistProduct(Context context, Product product) {
        Uri productUri = null;
        //Retailer
        Uri retailerUri = addRetailer(context, product.getProductVendor(), "", "");

        //Invoice
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        product.getProductInvoice().getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] InvoiceCopy = stream.toByteArray();
        Uri invoiceUri = addInvoice(context, InvoiceCopy, product.getProductVendor(), product.getProductPrice(), -1, ContentUris.parseId(retailerUri));

        //Warranty
        Uri warrantyUri = addWarranty(context, -1, "", "");

        //Product
        Log.i("Anil", "product.getProductCategory()  :" + product.getProductCategory());
        Cursor cursor = getCategoryID(context, product.getProductCategory());
        long categoryID = -1;
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.i("Anil", "getCount :" + cursor.getCount());
            categoryID = cursor.getLong(cursor.getColumnIndex(GazetteContracts.Category._ID));
            Log.i("Anil", "ID :" + categoryID);
        }
        productUri = addProduct(context, product.getProductName(), product.getProductSerialNumber(), -1, categoryID, -1, ContentUris.parseId(invoiceUri), ContentUris.parseId(warrantyUri), product.getProductBarCode(), product.getProductPurchaseDate(), 0);
        return productUri;
    }
}
