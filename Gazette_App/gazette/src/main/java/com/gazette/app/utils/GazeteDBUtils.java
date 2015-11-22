package com.gazette.app.utils;

import android.content.ContentValues;
import android.content.Context;

import com.gazette.app.GazetteApplication;
import com.gazette.app.provider.GazetteContracts;

/**
 * Created by Anil Gudigar on 11/22/2015.
 */
public class GazeteDBUtils {

    public static boolean loadCategory(Context context,String[] Categories) {
        for (String category : Categories) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(GazetteContracts.Category.NAME, category);
            context.getContentResolver().insert(GazetteContracts.Category.CONTENT_URI, contentValues);
        }
        return true;
    }
}
