package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_ADDRESS;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_BLOOD_GROUP;

public class DonorActivity extends AppCompatActivity {
    RecyclerView donorListRecycler;
    List<DonorsListModel> donorsListModelList;
    DonorListAdapter donorListAdapter;
    private DonorListAdapter.RecyclerViewClickListener mListener;
    private String TAG = "DonorActivity";
    SharedPreferencesManager preferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        preferencesManager = new SharedPreferencesManager(this);
        donorListRecycler = findViewById(R.id.donorListRecycler);
        mListener = new DonorListAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent getOneDonor = new Intent(getApplicationContext(), ViewOneDonor.class);
                getOneDonor.putExtra(Utils.DONOR_NAME, donorsListModelList.get(position).getDonorName());
                getOneDonor.putExtra(Utils.DONOR_BLOOD_GROUP, donorsListModelList.get(position).getDonorBloodGroup());
                getOneDonor.putExtra(Utils.DONOR_PHONE_NUMBER, donorsListModelList.get(position).getDonorPhoneNumber());
                getOneDonor.putExtra(Utils.DONOR_EMAIL, donorsListModelList.get(position).getDonorEmailAddress());
                getOneDonor.putExtra(Utils.DONOR_ID, donorsListModelList.get(position).getDonorID());
                getOneDonor.putExtra(Utils.DONOR_ADDRESS, donorsListModelList.get(position).getDonorAddress());
                getOneDonor.putExtra(Utils.DONOR_TOWN, donorsListModelList.get(position).getDonorTown());
                startActivity(getOneDonor);
            }
        };

        loadRecycler();
    }

    private void loadRecycler() {
        donorListRecycler.setLayoutManager(new LinearLayoutManager(this));
        donorsListModelList = new ArrayList<>();
        donorsListModelList.add(new DonorsListModel("1","Paul Kunda", "New Mushili", "Ndola",
                "0972157418", "pkunda24@gmail.com", "O"));
        donorsListModelList.add(new DonorsListModel("2","Kasolo Mambwe", "Matero", "Lusaka",
                "0972157418", "kasolo@gmail.com", "A"));

        donorListAdapter = new DonorListAdapter(donorsListModelList, mListener);
        donorListAdapter.notifyDataSetChanged();
        donorListRecycler.setAdapter(donorListAdapter);

    }

    private void loadDonorsFromServer(){
        // volley

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);
                            if (donorsNearMe.optString("error").equals("false")){
                                JSONArray readArray = donorsNearMe.getJSONArray("message");


                                for (int i =0;i<readArray.length(); i++){
                                    JSONObject getData = readArray.getJSONObject(i);
                                    donorsListModelList.add(new DonorsListModel(getData.getString("id"), getData.getString("name"),
                                            getData.getString("address"), getData.getString("town"), getData.getString("phone"),
                                            getData.getString("email"), getData.getString("blood_group")));
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
                params.put("blood_group", preferencesManager.userDetails().get(KEY_BLOOD_GROUP));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}