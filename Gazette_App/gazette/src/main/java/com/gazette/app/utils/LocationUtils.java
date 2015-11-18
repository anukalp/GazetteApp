package com.gazette.app.utils;

/**
 * Created by Anil Gudigar on 11/18/2015.
 */
public class LocationUtils {
    private static LocationUtils ourInstance = new LocationUtils();

    public static LocationUtils getInstance() {
        return ourInstance;
    }

    private LocationUtils() {
    }
}
