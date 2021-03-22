package com.visionarymindszm.bloodbank.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.visionarymindszm.bloodbank.screens.SignUp;

public class Utils {

    public static final String ROOT_URL = "";
    public static final String SIGN_UP = ROOT_URL+"";
    public static final String LOGIN = ROOT_URL+"";
    public static final String DONOR_LIST = ROOT_URL+"";
    public static final String HOSPITAL_SEARCH = ROOT_URL+"";
    public static final String REQUEST_BLOOD = ROOT_URL+"";
    public static final String APPROVE_BLOOD = ROOT_URL+"";
    public static final String HOSPITAL_NEAR_ME = ROOT_URL+"";
//    public static final String


    // EXTRA
    public static final String DONOR_NAME = "donor_name";
    public static final String DONOR_ADDRESS = "donor_location";
    public static final String DONOR_TOWN = "donor_town";
    public static final String DONOR_PHONE_NUMBER = "donor_phone";
    public static final String DONOR_EMAIL = "donor_email";
    public static final String DONOR_BLOOD_GROUP = "donor_blood_group";
    public static final String DONOR_ID = "donor_id";



    public static final String KEY_DONOR = "donor";
    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_TYPE_PASSED = "TYPE_REQUEST";





    /**
     *  show a short Toast
     * @param currentActivity : Pass the Activity/Fragment on the fore screen
     * @param message : Message to be displayed
     */
    public static void showToasterShort(Context currentActivity, String message, int length){
        if (length == 0){
            Toast.makeText(currentActivity, message, Toast.LENGTH_SHORT).show();
        }else if (length == 1){
            Toast.makeText(currentActivity, message, Toast.LENGTH_LONG).show();
        }
    }


    /**
     *
     * @param message  : Message to be displayed
     * @param view_name : layout name
     *
     */
    public static void showSnackBar(String message, View view_name){
        Snackbar.make(view_name, message, Snackbar.LENGTH_SHORT).show();
    }
    /**
     * @param message  : Message to be displayed
     * @param view_name : layout name
     * @param length: How long is the snack bar
     */
    public static void showSnackBar(String message, View view_name, int length){
        if (length == -1){
            Snackbar.make(view_name, message, Snackbar.LENGTH_SHORT).show();
        }else if (length == -2){
            Snackbar.make(view_name, message, Snackbar.LENGTH_INDEFINITE).show();
        }else if (length == 0){
            Snackbar.make(view_name, message, Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(view_name, message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
