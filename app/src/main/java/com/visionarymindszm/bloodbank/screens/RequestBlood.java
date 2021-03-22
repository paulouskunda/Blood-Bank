package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.bloodbank.utils.Utils;
import com.visionarymindszm.zededitoptions.ZedUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_ADDRESS;

public class RequestBlood extends AppCompatActivity {
    private TextView nameRequest, bloodGroupRequest, townOriginRequest;
    private EditText select_hospital, dateForRequest;
    SharedPreferencesManager preferencesManager;
    private String[] hospitalsList;
    // calendar variables
    Calendar calendar = Calendar.getInstance();
    int day;
    int month;
    int year;
    private String TAG = "RequestBloodActivity";
    Intent getData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        preferencesManager = new SharedPreferencesManager(this);
        init();
        getDataFromServer();
        onClicks();
        getData = getIntent();
        fillTextView();
    }

    private void init(){
        nameRequest = findViewById(R.id.nameRequest);
        bloodGroupRequest = findViewById(R.id.bloodGroupRequest);
        townOriginRequest = findViewById(R.id.townOriginRequest);
        select_hospital = findViewById(R.id.select_hospital);
        dateForRequest = findViewById(R.id.dateForRequest);


    }

    private void onClicks(){
        dateForRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pickDate();
            }
        });

        select_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZedUtils.showDialog("Select Hospital", RequestBlood.this, hospitalsList, select_hospital);
            }
        });
    }

    private void pickDate() {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearInner, int monthInner, int dayInner) {
                String setMeUp = ""+day+"/"+(month+1)+"/"+year;
                dateForRequest.setText(setMeUp);
            }
        }, year, month, day)
                .show();
    }

    private void fillTextView(){
        nameRequest.setText(getData.getStringExtra(Utils.DONOR_NAME));
        bloodGroupRequest.setText(getData.getStringExtra(Utils.DONOR_BLOOD_GROUP));
        townOriginRequest.setText(getData.getStringExtra(Utils.DONOR_TOWN));
    }
    private void sendDataToServer(){
        // volley

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.REQUEST_BLOOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDataFromServer(){

        // volley
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject hospitalNearMe = new JSONObject(response);
                            if (hospitalNearMe.optString("error").equals("false")){
                                JSONArray readArray = hospitalNearMe.getJSONArray("message");

                                hospitalsList = new String[readArray.length()];

                                for (int i =0;i<readArray.length(); i++){
                                    JSONObject getData = readArray.getJSONObject(i);
                                    hospitalsList[i] = getData.getString("hospital");
                                }
                            }


                        }catch (JSONException error){
                            Log.d(TAG, "Encountered an error "+error);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("city", preferencesManager.userDetails().get(KEY_ADDRESS));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}