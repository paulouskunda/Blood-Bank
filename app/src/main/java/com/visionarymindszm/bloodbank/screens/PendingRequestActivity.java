package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
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
import com.visionarymindszm.bloodbank.adapters.PendingAdapter;
import com.visionarymindszm.bloodbank.models.DonorsListModel;
import com.visionarymindszm.bloodbank.models.PendingListModel;
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

public class PendingRequestActivity extends AppCompatActivity {
    // pending
    RecyclerView pendingRecycler;
    List<PendingListModel> pendingListModels;
    PendingAdapter pendingAdapter;
    private PendingAdapter.RecyclerViewClickListener mListener;
    private String TAG = "PendingRequestActivity";
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);

        pendingRecycler = findViewById(R.id.pendingRecycler);
        number = findViewById(R.id.number);
        mListener = new PendingAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
             Utils.showToasterShort(getApplicationContext(),pendingListModels.get(position).getID(), 1);
            }
        };

        loadRecycler();
    }

    private void loadRecycler() {
        pendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        pendingListModels = new ArrayList<>();
        pendingListModels.add(new PendingListModel("1", "Wainting", "Paulous Kunda", "Ndola Central","12/03/2021","I need the freaking blood man","donor"));
        pendingListModels.add(new PendingListModel("2", "Wainting", "Kasolo Mambwe", "Ndola Central","12/03/2021","I need the freaking blood man","donor"));

        number.setTextColor(Color.GREEN);
        String setMe = "You have "+pendingListModels.size()+" pending approvals";
        number.setText(setMe);
        pendingAdapter = new PendingAdapter(pendingListModels, mListener);
        pendingAdapter.notifyDataSetChanged();
        pendingRecycler.setAdapter(pendingAdapter);
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
//                                    donorsListModelList.add(new DonorsListModel(getData.getString("id"), getData.getString("name"),
//                                            getData.getString("address"), getData.getString("town"), getData.getString("phone"),
//                                            getData.getString("email"), getData.getString("blood_group")));
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
//                params.put("city", preferencesManager.userDetails().get(KEY_ADDRESS));
//                params.put("blood_group", preferencesManager.userDetails().get(KEY_BLOOD_GROUP));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}