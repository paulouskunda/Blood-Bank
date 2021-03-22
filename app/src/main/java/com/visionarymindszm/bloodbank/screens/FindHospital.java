package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.adapters.FindHospitalAdapter;
import com.visionarymindszm.bloodbank.models.DonorsListModel;
import com.visionarymindszm.bloodbank.models.FindHospitalModel;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.bloodbank.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_ADDRESS;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_BLOOD_GROUP;

public class FindHospital extends AppCompatActivity {
    RecyclerView hospitalRecyclerView;
    List<FindHospitalModel> findHospitalModelList;
    FindHospitalAdapter findHospitalAdapter;
    FindHospitalAdapter.RecyclerViewClickListener mListener;
    ConstraintLayout find_hospital_layout;
    SharedPreferencesManager  preferencesManager;
    private String TAG = "FindHospital";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hospital);
        find_hospital_layout = findViewById(R.id.find_hospital_layout);

        preferencesManager = new SharedPreferencesManager(this);



        mListener = new FindHospitalAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                // google maps intent
                // Create a Uri from an intent string. Use the result to create an Intent.
                String uri_pass = "google.streetview:cbll="+findHospitalModelList.get(position).getHospitalLat()+","+findHospitalModelList.get(position).getHospitalLong();
                Uri gmmIntentUri = Uri.parse(uri_pass);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        };

        hospitalRecyclerView = findViewById(R.id.hospitalRecyclerView);
        findHospitalModelList = new ArrayList<>();

        loadRecycler();
    }

    private void loadRecycler() {
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findHospitalModelList.add(new FindHospitalModel("1", "UTH", "Lusaka Shani Shani",
                "-28.1983", "12.345"));
        findHospitalModelList.add(new FindHospitalModel("2", "NDOLA CENTRAL", "Ndola Shani Shani",
                "-28.1983", "12.345"));
        findHospitalModelList.add(new FindHospitalModel("3", "KABWE CENTRAL", "Kabwe Shani Shani",
                "-28.1983", "12.345"));
        findHospitalModelList.add(new FindHospitalModel("4", "KITWE CENTRAL", "Kitwe Shani Shani",
                "-28.1983", "12.345"));
        findHospitalAdapter = new FindHospitalAdapter(findHospitalModelList, mListener);
        findHospitalAdapter.notifyDataSetChanged();
        hospitalRecyclerView.setAdapter(findHospitalAdapter);
    }

    private void loadDonorsFromServer(){
        // volley

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject hospitalNearMe = new JSONObject(response);
                            if (hospitalNearMe.optString("error").equals("false")){
                                JSONArray readArray = hospitalNearMe.getJSONArray("message");


                                for (int i =0;i<readArray.length(); i++){
                                    JSONObject getData = readArray.getJSONObject(i);
                                   findHospitalModelList.add(new FindHospitalModel(getData.getString("id"), getData.getString("hospitalName"),
                                           getData.getString("hospitalAddress"), getData.getString("hospitalLat"), getData.getString("hospitalLong")));


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
                params.put("based_on", preferencesManager.userDetails().get(KEY_ADDRESS));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}