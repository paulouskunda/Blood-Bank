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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.adapters.FindHospitalAdapter;
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
import java.util.Objects;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_CITY;

public class FindHospital extends AppCompatActivity {
    private RecyclerView hospitalRecyclerView;
    private List<FindHospitalModel> findHospitalModelList;
    private FindHospitalAdapter findHospitalAdapter;
    private FindHospitalAdapter.RecyclerViewClickListener mListener;
    ConstraintLayout find_hospital_layout;
    private SharedPreferencesManager  preferencesManager;
    private String TAG = "FindHospitalError";
    private TextView foundOrNotResult;
    private EditText searchByTown;
    private boolean status_search = false;
    private String searchingOn = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hospital);
        find_hospital_layout = findViewById(R.id.find_hospital_layout);

        preferencesManager = new SharedPreferencesManager(this);
        searchByTown = findViewById(R.id.searchByTown);
        foundOrNotResult = findViewById(R.id.foundOrNotResult);

        mListener = new FindHospitalAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                String uri = "geo:"+findHospitalModelList.get(position).getHospitalLat() +","+ findHospitalModelList.get(position).getHospitalLong();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        };

        hospitalRecyclerView = findViewById(R.id.hospitalRecyclerView);
        findHospitalModelList = new ArrayList<>();

        loadHospitalFromServer();
    }

    private void loadHospitalFromServer(){
        // volley
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

    public void searchBasedOnTown(View view) {
        status_search = true;
        loadHospitalFromServer();
        findHospitalModelList.clear();
        searchingOn = searchByTown.getText().toString();
    }

    // TODO: 3/29/21 get location permission

    // TODO: 3/29/21 at a progress bar

    public void onLocationSearch(View view) {
        findHospitalModelList.clear();

        // volley
            hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));


            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME_COR,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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