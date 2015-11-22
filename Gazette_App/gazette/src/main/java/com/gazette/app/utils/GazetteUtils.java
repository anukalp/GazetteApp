package com.gazette.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Anil Gudigar on 11/22/2015.
 */
public class GazetteUtils {

    public static String gettodatDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c.getTime());
    }
}
