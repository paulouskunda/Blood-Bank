package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.adapters.DonorListAdapter;
import com.visionarymindszm.bloodbank.models.DonorsListModel;
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
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_TYPE;

public class DonorActivity extends AppCompatActivity {
    RecyclerView donorListRecycler;
    List<DonorsListModel> donorsListModelList;
    DonorListAdapter donorListAdapter;
    private DonorListAdapter.RecyclerViewClickListener mListener;
    private String TAG = "DonorActivity";
    SharedPreferencesManager preferencesManager;
    private TextView details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        preferencesManager = new SharedPreferencesManager(this);
        donorListRecycler = findViewById(R.id.donorListRecycler);
        donorsListModelList = new ArrayList<>();
        details = findViewById(R.id.details);



        mListener = new DonorListAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent getOneDonor = new Intent(getApplicationContext(), ViewOneDonor.class);
                getOneDonor.putExtra(Utils.DONOR_NAME, donorsListModelList.get(position).getDonorName());
                getOneDonor.putExtra(Utils.DONOR_BLOOD_GROUP, donorsListModelList.get(position).getDonorBloodGroup());
                getOneDonor.putExtra(Utils.DONOR_PHONE_NUMBER, donorsListModelList.get(position).getDonorPhoneNumber());
                getOneDonor.putExtra(Utils.DONOR_EMAIL, donorsListModelList.get(position).getDonorEmailAddress());
                getOneDonor.putExtra(Utils.DONOR_ID, donorsListModelList.get(position).getDonorID());
                Log.d(TAG, donorsListModelList.get(position).getDonorID());
                getOneDonor.putExtra(Utils.DONOR_ADDRESS, donorsListModelList.get(position).getDonorAddress());
                getOneDonor.putExtra(Utils.DONOR_TOWN, donorsListModelList.get(position).getDonorTown());
                startActivity(getOneDonor);
            }
        };

        loadDonorsFromServer();
    }





    private void loadDonorsFromServer(){
        // volley
        donorListRecycler.setLayoutManager(new LinearLayoutManager(this));
        String setMeUp = "Based on your City "+preferencesManager.userDetails().get(KEY_CITY);
        details.setText(setMeUp);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.DONOR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);
                            if (donorsNearMe.optString("error").equals("false")){
                                JSONArray readArray = donorsNearMe.getJSONArray("message");


                                for (int i =0;i<readArray.length(); i++){
                                    JSONObject getData = readArray.getJSONObject(i);
                                    donorsListModelList.add(new DonorsListModel(
                                            getData.getString("id"),
                                            getData.getString("name"),
                                            getData.getString("physical_address"),
                                            getData.getString("city"),
                                            getData.getString("phone_number"),
                                            getData.getString("email"),
                                            getData.getString("blood_group")));
                                    Log.d(TAG, ""+donorsListModelList);
                                    Log.d(TAG, ""+ getData.getString("id"));
                                }

                                donorListAdapter = new DonorListAdapter(donorsListModelList, mListener);
                                donorListAdapter.notifyDataSetChanged();
                                donorListRecycler.setAdapter(donorListAdapter);

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
                params.put("city",preferencesManager.userDetails().get(KEY_CITY));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}