package com.visionarymindszm.bloodbank.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.screens.LoginActivity;

import java.util.HashMap;

public class SharedPreferencesManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = String.valueOf(R.string.app_name);
    public static final String KEY_USERNAME = "logged_user";
    public static final String KEY_TYPE =  "donor_or_receiver";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_IS_LOGGED = "user_logged_in";
    public static final String KEY_CITY = "logged_in_city";
    public static final String KEY_BLOOD_GROUP = "blood_group";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phone";
    public static final String KEY_PROVINCE = "province";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHYSICAL_ADD = "physical";


    public SharedPreferencesManager(Context _context) {
        preferences = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        this._context = _context;
        editor = preferences.edit();
    }

    public void createUserLoginPref(HashMap<String, String> userMap){
        editor.putBoolean(KEY_IS_LOGGED, true);

        editor.putString(KEY_USERNAME, userMap.get(KEY_USERNAME));
        editor.putString(KEY_EMAIL, userMap.get(KEY_EMAIL));
        editor.putString(KEY_TYPE, userMap.get(KEY_TYPE));
        editor.putString(KEY_USER_ID, userMap.get(KEY_USER_ID));
        editor.putString(KEY_CITY, userMap.get(KEY_CITY));
        editor.putString(KEY_BLOOD_GROUP, userMap.get(KEY_BLOOD_GROUP));
        editor.putString(KEY_ADDRESS, userMap.get(KEY_ADDRESS));
        editor.putString(KEY_PHYSICAL_ADD, userMap.get(KEY_PHYSICAL_ADD));
        editor.putString(KEY_PHONE_NUMBER, userMap.get(KEY_PHONE_NUMBER));
        editor.putString(KEY_PASSWORD, userMap.get(KEY_PASSWORD));
        editor.putString(KEY_PROVINCE, userMap.get(KEY_PROVINCE));

        editor.commit();
    }
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    private boolean isUserLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED, false);
    }


    public HashMap<String, String> userDetails(){
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail.put(KEY_USER_ID, preferences.getString(KEY_USER_ID, null));
        userDetail.put(KEY_USERNAME, preferences.getString(KEY_USERNAME, null));
        userDetail.put(KEY_CITY, preferences.getString(KEY_CITY, null));
        userDetail.put(KEY_TYPE, preferences.getString(KEY_TYPE, null));
        userDetail.put(KEY_BLOOD_GROUP, preferences.getString(KEY_BLOOD_GROUP, null));
        userDetail.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));
        userDetail.put(KEY_PROVINCE, preferences.getString(KEY_PROVINCE, null));
        userDetail.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        userDetail.put(KEY_PHONE_NUMBER, preferences.getString(KEY_PHONE_NUMBER, null));
        userDetail.put(KEY_PHYSICAL_ADD, preferences.getString(KEY_PHYSICAL_ADD, null));
        return userDetail;
    }




}
