package com.visionarymindszm.bloodbank.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.adapters.PendingAdapter;
import com.visionarymindszm.bloodbank.models.PendingListModel;
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

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_TYPE;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USER_ID;

public class PendingRequestActivity extends AppCompatActivity {
    // pending
    RecyclerView pendingRecycler;
    RecyclerView approvedRecycler;
    List<PendingListModel> pendingListModels;
    PendingAdapter pendingAdapter;
    List<PendingListModel> approvedListModels;
    PendingAdapter approvedAdapter;
    private PendingAdapter.RecyclerViewClickListener mListener;
    private String TAG = "PendingRequestActivity";
    TextView number;
    private SharedPreferencesManager preferencesManager;
    private ConstraintLayout pending_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);
        pending_layout = findViewById(R.id.pending_layout);
        pendingRecycler = findViewById(R.id.pendingRecycler);
        approvedRecycler = findViewById(R.id.approvedRecycler);
        preferencesManager = new SharedPreferencesManager(this);
        number = findViewById(R.id.number);
        mListener = new PendingAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                final int inner_position = position;
             Utils.showToasterShort(getApplicationContext(),pendingListModels.get(inner_position).getID(), 1);
                final AlertDialog.Builder dialog = new  AlertDialog.Builder(
                        PendingRequestActivity.this);
                View innerView = LayoutInflater.from(PendingRequestActivity.this).inflate(R.layout.dialog_options, null);
                dialog.setView(innerView)
                        .setCancelable(true)
                        .create();
                TextView name_of_requester = innerView.findViewById(R.id.name_passed);
                Button reject_button = innerView.findViewById(R.id.reject_button);
                Button accept_button = innerView.findViewById(R.id.accept_button);
                name_of_requester.setText(pendingListModels.get(position).getName());

                reject_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        response_to_server(pendingListModels.get(inner_position).getID(), "rejected");

                    }
                });

                accept_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        response_to_server(pendingListModels.get(inner_position).getID(), "accepted");
                    }
                });
                dialog.show();
            }
        };

        if (Objects.requireNonNull(preferencesManager.userDetails()
                .get(KEY_TYPE)).equalsIgnoreCase("donor")){
            loadDonorsFromServer();
            approvedRequestDonor();

        }else {
            loadWaitingReceivers();
            approvedRequestReceiver();
        }
    }

    private void response_to_server(final String id, final String status) {
        Log.d(TAG, "ID CALLED => "+ preferencesManager.userDetails().get(KEY_USER_ID));
        // volley
        pendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        pendingListModels = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.ACCEPT_REJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);

                            if (donorsNearMe.length() > 0){
                                if (donorsNearMe.optString("error").equals("false")){
                                    Utils.showSnackBar("Request was "+status,pending_layout, 0);

                                    if (Objects.requireNonNull(preferencesManager.userDetails()
                                            .get(KEY_TYPE)).equalsIgnoreCase("donor")){
                                        loadDonorsFromServer();
                                        approvedRequestDonor();

                                    }else {
                                        loadWaitingReceivers();
                                        approvedRequestReceiver();

                                    }

                                }else {
                                    Utils.showSnackBar("Request encountered an error",pending_layout, 0);
                                    if (Objects.requireNonNull(preferencesManager.userDetails()
                                            .get(KEY_TYPE)).equalsIgnoreCase("donor")){
                                        loadDonorsFromServer();
                                        approvedRequestDonor();
                                    }else {
                                        loadWaitingReceivers();
                                        approvedRequestReceiver();

                                    }
                                    Log.d(TAG, donorsNearMe.optString("error") + "   "+ donorsNearMe.optString("message"));
                                }
                            }
                        }catch (JSONException error){
                            Log.d(TAG, "Encountered an error "+error);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERR: "+error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("requesting_id", preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("req_id",  id);
                params.put("status", status);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadDonorsFromServer(){
        Log.d(TAG, "ID CALLED => "+ preferencesManager.userDetails().get(KEY_USER_ID));
        // volley
        pendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        pendingListModels = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.WAITING_LIST_DONOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);

                            if (donorsNearMe.length() > 0){
                                if (donorsNearMe.optString("error").equals("false")){
                                    JSONArray readArray = donorsNearMe.getJSONArray("message");
                                    Log.d(TAG,"You have "+readArray.length()+" pending approvals" );

                                    String setMe = "You have "+readArray.length()+" pending approvals";
                                    number.setText(setMe);

                                    for (int i =0;i<readArray.length(); i++){
                                        JSONObject getData = readArray.getJSONObject(i);

                                        pendingListModels.add(new PendingListModel(
                                                getData.getString("req_id"),
                                                "Waiting",
                                                getData.getString("name"),
                                                getData.getString("requested_hosp"),
                                                getData.getString("request_date"),
                                                getData.getString("reasonForBlood"),
                                                preferencesManager.userDetails().get(KEY_TYPE)));
                                    }
                                    pendingAdapter = new PendingAdapter(pendingListModels, mListener);
                                    pendingAdapter.notifyDataSetChanged();
                                    pendingRecycler.setAdapter(pendingAdapter);
                                }else {
                                    String setMe = "You have 0 pending approvals";
                                    number.setText(setMe);
                                    Log.d(TAG, donorsNearMe.optString("error") + "   "+ donorsNearMe.optString("message"));
                                }
                            }
                        }catch (JSONException error){
                            Log.d(TAG, "Encountered an error "+error);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERR: "+error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("requesting_id", preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("requesting_id",  preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("status", "waiting");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void loadWaitingReceivers(){
        Log.d(TAG, "ID Waiting Receiver CALLED => "+ preferencesManager.userDetails().get(KEY_USER_ID));
        // volley
        pendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        pendingListModels = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.WAITING_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);

                            if (donorsNearMe.length() > 0){
                                if (donorsNearMe.optString("error").equals("false")){
                                    JSONArray readArray = donorsNearMe.getJSONArray("message");
                                    Log.d(TAG,"You have "+readArray.length()+" pending approvals" );

                                    String setMe = "You have "+readArray.length()+" pending approvals";
                                    number.setText(setMe);

                                    for (int i =0;i<readArray.length(); i++){
                                        JSONObject getData = readArray.getJSONObject(i);

                                        pendingListModels.add(new PendingListModel(
                                                getData.getString("req_id"),
                                                "Waiting",
                                                getData.getString("name"),
                                                getData.getString("requested_hosp"),
                                                getData.getString("request_date"),
                                                getData.getString("reasonForBlood"),
                                                preferencesManager.userDetails().get(KEY_TYPE)));
                                    }
                                    pendingAdapter = new PendingAdapter(pendingListModels, mListener);
                                    pendingAdapter.notifyDataSetChanged();
                                    pendingRecycler.setAdapter(pendingAdapter);
                                }else {
                                    String setMe = "You have 0 pending approvals";
                                    number.setText(setMe);
                                    Log.d(TAG, "Receiver "+donorsNearMe.optString("error") + "   "+ donorsNearMe.optString("message"));
                                }
                            }
                        }catch (JSONException error){
                            Log.d(TAG, "Encountered an error "+error);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERR: "+error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("requesting_id", preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("requesting_id",  preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("status", "waiting");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void approvedRequestReceiver(){
        Log.d(TAG, "ID APPROVED CALLED => "+ preferencesManager.userDetails().get(KEY_USER_ID));
        // volley
        approvedRecycler.setLayoutManager(new LinearLayoutManager(this));
        approvedListModels = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.APPROVED_BLOOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);

                            if (donorsNearMe.length() > 0){
                                if (donorsNearMe.optString("error").equals("false")){
                                    JSONArray readArray = donorsNearMe.getJSONArray("message");
                                    Log.d(TAG,"You have "+readArray.length()+" approved requests" );

                                    String setMe = "You have "+readArray.length()+" approved requests";
//                                    number.setText(setMe);

                                    for (int i =0;i<readArray.length(); i++){
                                        JSONObject getData = readArray.getJSONObject(i);
                                        Log.d(TAG, "Message "+readArray);
                                        approvedListModels.add(new PendingListModel(
                                                getData.getString("req_id"),
                                                getData.getString("request_status"),
                                                getData.getString("name"),
                                                getData.getString("requested_hosp"),
                                                getData.getString("request_date"),
                                                getData.getString("reasonForBlood"),
                                                getData.getString("type_of_user")));

                                    }

                                    approvedAdapter = new PendingAdapter(approvedListModels, mListener);
                                    approvedAdapter.notifyDataSetChanged();
                                    approvedRecycler.setAdapter(approvedAdapter);
                                }else {
//                                    String setMe = "You have 0 Accepted/Rejected approvals";
//                                    number.setText(setMe);
                                    Log.d(TAG, "Error no approval "+donorsNearMe.optString("error") + "   "+ donorsNearMe.optString("message"));
                                }
                            }


                        }catch (JSONException error){
                            Log.d(TAG, "Encountered an error @approval-Receiver "+error);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERR: "+error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("requesting_id", preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("status", "accepted");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void approvedRequestDonor(){
        Log.d(TAG, "ID CALLED => "+ preferencesManager.userDetails().get(KEY_USER_ID));
        // volley
        approvedRecycler.setLayoutManager(new LinearLayoutManager(this));
        approvedListModels = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.APPROVED_BLOOD_DONOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject donorsNearMe = new JSONObject(response);

                            if (donorsNearMe.length() > 0){
                                if (donorsNearMe.optString("error").equals("false")){
                                    JSONArray readArray = donorsNearMe.getJSONArray("message");


                                    for (int i =0;i<readArray.length(); i++){
                                        JSONObject getData = readArray.getJSONObject(i);

                                        approvedListModels.add(new PendingListModel(
                                                getData.getString("req_id"),
                                                getData.getString("request_status"),
                                                getData.getString("name"),
                                                getData.getString("requested_hosp"),
                                                getData.getString("request_date"),
                                                getData.getString("reasonForBlood"),
                                                getData.getString("type_of_user")));

                                    }

                                    approvedAdapter = new PendingAdapter(approvedListModels, mListener);
                                    approvedAdapter.notifyDataSetChanged();
                                    approvedRecycler.setAdapter(approvedAdapter);
                                }else {

                                    Log.d(TAG, donorsNearMe.optString("error") + "   "+ donorsNearMe.optString("message"));
                                }
                            }


                        }catch (JSONException error){
                            Log.d(TAG, "Encountered an error "+error);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERR: "+error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("requester_id", preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("status", "accepted");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}