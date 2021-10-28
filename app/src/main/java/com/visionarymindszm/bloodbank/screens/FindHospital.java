package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.adapters.FindHospitalAdapter;
import com.visionarymindszm.bloodbank.models.FindHospitalModel;
import com.visionarymindszm.bloodbank.models.LocationsModel;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.bloodbank.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_CITY;

public class FindHospital extends AppCompatActivity {
    private RecyclerView hospitalRecyclerView;
    private List<FindHospitalModel> findHospitalModelList;
    private FindHospitalAdapter findHospitalAdapter;
    private FindHospitalAdapter.RecyclerViewClickListener mListener;
    ConstraintLayout find_hospital_layout;
    private SharedPreferencesManager  preferencesManager;
    private String TAG = FindHospital.class.getName();
    private TextView foundOrNotResult;
    private EditText searchByTown;
    private boolean status_search = false;
    private String searchingOn = "";
    LocationManager locationManager;
    String latitude, longitude;
    private Double lat;
    private Double lon;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hospital);
        find_hospital_layout = findViewById(R.id.find_hospital_layout);
        Button viewMap = findViewById(R.id.viewMap);
        preferencesManager = new SharedPreferencesManager(this);
        searchByTown = findViewById(R.id.searchByTown);
        foundOrNotResult = findViewById(R.id.foundOrNotResult);

        mListener = (view, position) -> {
            String uri = "geo:"+findHospitalModelList.get(position).getHospitalLat() +","+ findHospitalModelList.get(position).getHospitalLong();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        };
        viewMap.setOnClickListener(click -> {
            openMapIntent();
        });
        hospitalRecyclerView = findViewById(R.id.hospitalRecyclerView);
        findHospitalModelList = new ArrayList<>();
        Utils.LOCATION_LIST_MODE = new ArrayList<>();
        loadHospitalFromServer();
        getLocation();

    }


    public void searchBasedOnTown(View view) {
        status_search = true;
        loadHospitalFromServer();
        findHospitalModelList.clear();
        searchingOn = searchByTown.getText().toString();
    }

    // TODO: 3/29/21 get location permission

    // TODO: 3/29/21 at a progress bar

    public void onLocationSearch(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            turnOnGPS();
        }else{
            getLocation();
        }
    }

    private void loadHospitalFromServer(){
        // volley
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME,
                response -> {
                    try{
                        JSONObject hospitalNearMe = new JSONObject(response);
                        String showingResultsBasedOn = null;
                        if (status_search)
                            showingResultsBasedOn = "Showing resulting searched City: "+ searchingOn;
                        else
                            showingResultsBasedOn = "Showing resulting based on your set City: "+ Objects.requireNonNull(preferencesManager.userDetails().get(KEY_CITY)).toUpperCase();
                        foundOrNotResult.setText(showingResultsBasedOn);
                        Log.d(TAG, "pref: "+preferencesManager.userDetails().get(KEY_CITY));
                        if (hospitalNearMe.optString("error").equals("false")){
                            JSONArray readArray = hospitalNearMe.getJSONArray("message");


                            for (int i =0;i<readArray.length(); i++){
                                JSONObject getData = readArray.getJSONObject(i);
                               findHospitalModelList.add(new FindHospitalModel(
                                       getData.getString("hosp_id"),
                                       getData.getString("hosp_name"),
                                       getData.getString("hosp_address"),
                                       getData.getString("hosp_lat"),
                                       getData.getString("hosp_long"),
                                       getData.getString("hosp_city"),
                                       getData.getString("hosp_location")));
                                Utils.LOCATION_LIST_MODE.add(new LocationsModel(
                                        Double.parseDouble(getData.getString("hosp_lat")),
                                        Double.parseDouble(getData.getString("hosp_long")),
                                        getData.getString("hosp_name")));
                            }
                            findHospitalAdapter = new FindHospitalAdapter(findHospitalModelList, mListener);
                            findHospitalAdapter.notifyDataSetChanged();
                            hospitalRecyclerView.setAdapter(findHospitalAdapter);
                        }else {
                            Utils.showToasterShort(FindHospital.this, "Nothing", 1);
                        }
                    }catch (JSONException error){
                        Log.d(TAG, "Encountered an error "+error);
                    }
                }, error -> Log.d(TAG, "Encountered an error "+error)){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (status_search)
                    params.put("city", searchingOn);
                else
                    params.put("city", preferencesManager.userDetails().get(KEY_CITY));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openMapIntent(){
        if (lat == null){
            Utils.showSnackBar("We have failed to find your current location", find_hospital_layout);
            getLocation();
        }else{
            Intent intent = new Intent(this, HereMapsActivity.class);
            ArrayList<String> latLon = new ArrayList<>();
            latLon.add(String.valueOf(lat));
            latLon.add(String.valueOf(lon));
            intent.putStringArrayListExtra("Arrays", latLon);
            startActivity(intent);
        }

    }

    private void turnOnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes",
                (dialog, which) ->
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            turnOnGPS();
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1001);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            String loc = String.format("Accuracy: %s, lat: %s, lon: %s",
                                    location.getAccuracy(), location.getLatitude(), location.getLongitude());
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Log.d(TAG, loc + " fused last known location ");
                        } else {
                            Log.d(TAG, "Method => nothing last");
                        }
                    }).addOnFailureListener(this, e -> {
                        Log.d(TAG, "Method =>  " + e.getMessage());
                        e.printStackTrace();
            });
        }
    }

    private void getMeTheseHospitals(){
        findHospitalModelList.clear();

        // volley
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME_COR,
                response -> {
                    try{
                        JSONObject hospitalNearMe = new JSONObject(response);
                        String showingResultsBasedOn = "Showing resulting based on your current location, all within 0 to 50KM from you";
                        foundOrNotResult.setText(showingResultsBasedOn);
                        Log.d(TAG, "pref: "+preferencesManager.userDetails().get(KEY_CITY));
                        if (hospitalNearMe.optString("error").equals("false")){
                            JSONArray readArray = hospitalNearMe.getJSONArray("message");


                            for (int i =0;i<readArray.length(); i++){
                                JSONObject getData = readArray.getJSONObject(i);
                                findHospitalModelList.add(new FindHospitalModel(
                                        getData.getString("hosp_id"),
                                        getData.getString("hosp_name"),
                                        getData.getString("hosp_address"),
                                        getData.getString("hosp_lat"),
                                        getData.getString("hosp_long"),
                                        getData.getString("hosp_city"),
                                        getData.getString("hosp_location")));
                            }
                            findHospitalAdapter = new FindHospitalAdapter(findHospitalModelList, mListener);
                            findHospitalAdapter.notifyDataSetChanged();
                            hospitalRecyclerView.setAdapter(findHospitalAdapter);
                        }else {
                            Utils.showToasterShort(FindHospital.this, "Nothing", 1);
                        }


                    }catch (JSONException error){
                        Log.d(TAG, "Encountered an error "+error);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Encountered an error "+error);

            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lat", "-12.8");
                params.put("long", "28.6");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}