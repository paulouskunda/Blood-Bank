package com.visionarymindszm.bloodbank.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.visionarymindszm.bloodbank.models.LocationsModel;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final String ROOT_URL = "http://10.3.2.180/Blood-Bank-API/api/?apicall=";
    //public static final String  ROOT_URL = "http://10.0.2.2/Blood-Bank-API/?apicall="; // use this for the emulator
    public static final String SIGN_UP = ROOT_URL+"sign_up";
    public static final String LOGIN = ROOT_URL+"login";
    public static final String DONOR_LIST = ROOT_URL+"get_donors";
    public static final String HOSPITAL_SEARCH = ROOT_URL+"";
    public static final String REQUEST_BLOOD = ROOT_URL+"request_blood";
    public static final String ACCEPT_REJECT = ROOT_URL+"accept_reject_request";
    public static final String APPROVED_BLOOD = ROOT_URL+"approved_requests";
    public static final String APPROVED_BLOOD_DONOR = ROOT_URL+"approved_requests_donor";
    public static final String HOSPITAL_NEAR_ME = ROOT_URL+"get_hospital";
    public static final String HOSPITAL_NEAR_ME_COR = ROOT_URL+"get_hospital_cord";
    public static final String WAITING_LIST = ROOT_URL+"pending_requests";
    public static final String WAITING_LIST_DONOR = ROOT_URL+"pending_requests_donor";
    public static final String GIVE_BLOOD = ROOT_URL + "giveBlood";
    public static final String GET_BLOOD = ROOT_URL + "getGivenBlood";
    public static List<LocationsModel> LOCATION_LIST_MODE;

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
