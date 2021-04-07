package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.adapters.DonorBloodAdapter;
import com.visionarymindszm.bloodbank.models.DonateBloodModel;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.bloodbank.utils.Utils;
import com.visionarymindszm.zededitoptions.ZedUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_BLOOD_GROUP;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_CITY;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USERNAME;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USER_ID;

// TODO: 4/4/21 Check dates, no past dates
public class DonateBlood extends AppCompatActivity {
    private EditText dateOfDonation, hospitalNearBy,reasonForDonation;
    private String[] hospitalNearYou;
    private String[] reasonOfDonation = {"Anniversary", "Felt the Need", "Others"};

    private String TAG = "DonateBloodActivity";
    private SharedPreferencesManager preferencesManager;
    private ProgressBar progressBarDonate;
    private List<DonateBloodModel> donateBloodModelList;
    private DonorBloodAdapter donorBloodAdapter;
    private RecyclerView counterRecycler;
    private DonorBloodAdapter.RecyclerViewClickListener mListener;
    private ConstraintLayout donate_layout;
    private Button donateButton;
    private BottomSheetDialog bottomSheetDialog;
    private TextView counterList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);
        preferencesManager =  new SharedPreferencesManager(this);

        counterRecycler = findViewById(R.id.counterRecycler);
        donate_layout = findViewById(R.id.donate_layout);
        counterList = findViewById(R.id.counterList);

        getHospital();
        onLoad();
        appointmentsView();
    }

    private void onLoad(){

    }

    private void pickDate() {
        // calendar variables
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);


        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearInner, int monthInner, int dayInner) {
                String setMeUp = ""+dayInner+"/"+(monthInner+1)+"/"+yearInner;
                Toast.makeText(DonateBlood.this, setMeUp, Toast.LENGTH_SHORT).show();
                dateOfDonation.setText(setMeUp);
            }
        }, year, month, day)
                .show();
    }

    private void getHospital(){

        // volley
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.HOSPITAL_NEAR_ME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject hospitalNearMe = new JSONObject(response);
                            if (hospitalNearMe.optString("error").equals("false")){
                                JSONArray readArray = hospitalNearMe.getJSONArray("message");

                                hospitalNearYou = new String[readArray.length()];

                                for (int i =0;i<readArray.length(); i++){
                                    JSONObject getData = readArray.getJSONObject(i);
                                    hospitalNearYou[i] = getData.getString("hosp_name");
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
                params.put("city", preferencesManager.userDetails().get(KEY_CITY));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getDates(){

    }

    private void uploadToServer(){
        // volley
        progressBarDonate.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.GIVE_BLOOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            progressBarDonate.setVisibility(View.INVISIBLE);
                            bottomSheetDialog.cancel();
                            JSONObject donateBlood = new JSONObject(response);
                            if (donateBlood.optString("error").equals("false")){
                                donateButton.setEnabled(true);
                                hospitalNearBy.setText("");
                                dateOfDonation.setText("");
                                reasonForDonation.setText("");
                                Utils.showSnackBar(donateBlood.optString("message"), donate_layout, 1);
                                appointmentsView();

                            }else{
                                Utils.showSnackBar(donateBlood.optString("message"), donate_layout, 1);
                                Log.d(TAG, donateBlood.optString("message"));
                            }


                        }catch (JSONException error){
                            progressBarDonate.setVisibility(View.INVISIBLE);
                            bottomSheetDialog.cancel();
                            Utils.showSnackBar("Error, try again later", donate_layout, 1);

                            Log.d(TAG, "Encountered an error "+error);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarDonate.setVisibility(View.INVISIBLE);
                Utils.showSnackBar("Error, try again later", donate_layout, 1);
                bottomSheetDialog.cancel();
                Log.d(TAG, "Encountered Volley error "+error);

            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("don_id_ref", preferencesManager.userDetails().get(KEY_USER_ID));
                params.put("donor_name", preferencesManager.userDetails().get(KEY_USERNAME));
                params.put("donor_blood_group", preferencesManager.userDetails().get(KEY_BLOOD_GROUP));
                params.put("donor_hospital", hospitalNearBy.getText().toString());
                params.put("donor_date", dateOfDonation.getText().toString());
                params.put("donor_reason", reasonForDonation.getText().toString());
                params.put("donor_city", preferencesManager.userDetails().get(KEY_CITY));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void appointmentsView(){

        Log.d(TAG, " appointmentsView ID CALLED => "+ preferencesManager.userDetails().get(KEY_USER_ID));
        // volley
        counterRecycler.setLayoutManager(new LinearLayoutManager(this));
        donateBloodModelList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.GET_BLOOD,
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

                                        donateBloodModelList.add(new DonateBloodModel(
                                                getData.getString("don_id"),
                                                getData.getString("donor_name"),
                                                getData.getString("donor_blood_group"),
                                                getData.getString("donor_hospital"),
                                                getData.getString("donation_reason"),
                                                getData.getString("donor_date"),
                                                getData.getString("donor_city"),
                                                getData.getString("don_id_ref")));
                                    }

                                    donorBloodAdapter = new DonorBloodAdapter(donateBloodModelList);
                                    donorBloodAdapter.notifyDataSetChanged();
                                    counterRecycler.setAdapter(donorBloodAdapter);
                                }else {

                                    Utils.showSnackBar( donorsNearMe.optString("message"), donate_layout, 1);
                                    Log.d(TAG, donorsNearMe.optString("error") + "   "+ donorsNearMe.optString("message"));
                                }
                            }


                        }catch (JSONException error){
                            Utils.showSnackBar("We faced an error, try again later", donate_layout, 1);

                            Log.d(TAG, "Encountered an error "+error);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showSnackBar("We faced an error, try again later", donate_layout, 1);

                Log.d(TAG, "ERR: "+error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("don_id_ref", preferencesManager.userDetails().get(KEY_USER_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onDonate(View view) {
        if (TextUtils.isEmpty(hospitalNearBy.getText().toString())){
            Utils.showSnackBar("Select a hospital", donate_layout, 1);
        }else if (TextUtils.isEmpty(dateOfDonation.getText().toString())){
            Utils.showSnackBar("Select a date", donate_layout, 1);
        }else if (TextUtils.isEmpty(reasonForDonation.getText().toString())){
            Utils.showSnackBar("Select a reason", donate_layout, 1);
        }else {
            uploadToServer();
            donateButton.setEnabled(false);
        }
    }

    public void onOpenBottomSheet(View view) {
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_view_card_blood, null);
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
        dateOfDonation = bottomSheet.findViewById(R.id.dateOfDonation);
        hospitalNearBy = bottomSheet.findViewById(R.id.hospitalNearBy);
        reasonForDonation = bottomSheet.findViewById(R.id.reasonForDonation);
        progressBarDonate = bottomSheet.findViewById(R.id.progressBarDonate);
        progressBarDonate.setVisibility(View.INVISIBLE);
        donateButton = bottomSheet.findViewById(R.id.donateButton);
        reasonForDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZedUtils.showDialog("Select A Reason", DonateBlood.this, reasonOfDonation, reasonForDonation);
            }
        });

        dateOfDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });

        hospitalNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZedUtils.showDialog("Select a Hospital", DonateBlood.this, hospitalNearYou, hospitalNearBy);
            }
        });
    }
}