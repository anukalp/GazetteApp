package com.gazette.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class SharedPreferenceManager {
    // Shared Preferences
    private SharedPreferences mSharedPreferences;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "GazettePreferences";

    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_USER_TOKEN = "userToken";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final  String NO_DATA = "no data";
    private static final String KEY_CURRENT_LOCATION = "LOCATION";

    public SharedPreferenceManager(Context context) {
        this._context = context;
        mSharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mSharedPreferences.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return mSharedPreferences.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE, mobileNumber);
        editor.commit();
    }

    public String getMobileNumber() {
        return mSharedPreferences.getString(KEY_MOBILE, null);
    }

    public String getUserToken() {
        return mSharedPreferences.getString(KEY_USER_TOKEN, null);
    }

    public String getName() {
        return mSharedPreferences.getString(KEY_NAME, null);
    }
    public String getEmail() {
        return mSharedPreferences.getString(KEY_EMAIL, null);
    }

    public void setUser(String name, String email, String mobile) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.commit();
    }

    public void setCurrentLocation(double latitude, double longitude) {
        Log.i("Anil","in Pref:"+ latitude+","+longitude);
        String  latlong = new String(latitude+","+longitude);
        Log.i("Anil","Pref latlong :"+ latlong);
        editor.putString(KEY_CURRENT_LOCATION, latlong);
        editor.commit();
    }

    public String getCurrentLocation(){
        return mSharedPreferences.getString(KEY_CURRENT_LOCATION, null);
    }
    public void createLogin(String name, String email, String mobile, String userToken) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_USER_TOKEN, userToken);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public void createLogin(String name, String email, String mobile) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }


    public void setLogin() {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put(GazetteConstants.UserDetails.NAME, mSharedPreferences.getString(KEY_NAME, NO_DATA));
        profile.put(GazetteConstants.UserDetails.EMAIL, mSharedPreferences.getString(KEY_EMAIL, NO_DATA));
        profile.put(GazetteConstants.UserDetails.MOBILE, mSharedPreferences.getString(KEY_MOBILE, NO_DATA));
        return profile;
    }
}
