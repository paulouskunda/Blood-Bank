package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.MainActivity;
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
import java.util.Map;
import java.util.Objects;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_TYPE;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USER_ID;

public class MainScreenActivity extends AppCompatActivity {
    ConstraintLayout main_screen_layout;
    CardView donorList;
    private   SharedPreferencesManager sharedPreferencesManager;
    private static final String TAG = "MainScreenActivity";
    private TextView notificationBloodRequest, donorList_orDonate;
    private ImageView donateLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        main_screen_layout = findViewById(R.id.main_screen_layout);
        donorList = findViewById(R.id.donorList);
        notificationBloodRequest = findViewById(R.id.notificationBloodRequest);
        donorList_orDonate = findViewById(R.id.donorList_orDonate);
        donateLogo = findViewById(R.id.donateLogo);
        if (Objects.equals(sharedPreferencesManager.userDetails().get(KEY_TYPE), "donor")){
//            donorList.setVisibility(View.GONE);
            String g="Donate Blood";
            donorList_orDonate.setText(g);
        }
        if (sharedPreferencesManager.userDetails().get(KEY_TYPE) == null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        pendingCount();


    }

    public void findHospital(View view) {
        startActivity(new Intent(this, FindHospital.class));
    }


    public void onBloodRequest(View view) {
        startActivity(new Intent(this, PendingRequestActivity.class));
    }

    public void onProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void onDonorList(View view) {
        if (Objects.equals(sharedPreferencesManager.userDetails().get(KEY_TYPE), "donor"))
            startActivity(new Intent(this, DonateBlood.class));
        else
            startActivity(new Intent(this, DonorActivity.class));

    }

    public void onLogout(View view) {
        sharedPreferencesManager.logoutUser();
        finish();
    }

    private void pendingCount(){
            Log.d(TAG, "ID CALLED => "+ sharedPreferencesManager.userDetails().get(KEY_USER_ID));
            // volley

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


//                                        notificationBloodRequest.setText(readArray.length());


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
//                params.put("requesting_id", preferencesManager.userDetails().get(KEY_USER_ID));
                    params.put("requesting_id", sharedPreferencesManager.userDetails().get(KEY_USER_ID));
                    params.put("status", "waiting");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


    }
}