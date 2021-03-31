package com.visionarymindszm.bloodbank.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.*;
import com.visionarymindszm.bloodbank.utils.Utils;
import com.visionarymindszm.zededitoptions.ZedUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private EditText name, emailAddress, phoneNumber, physicalAddress, password, whyGiveBlood;
    private EditText bloodGroupField, citySignUp, provinceSignUp;
    private ConstraintLayout sign_up_layout;
    String[] bloodGroups = {"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "A-"};
    private Intent getMeThoseExtras;
    private SharedPreferencesManager preferencesManager;
    private final String TAG = "SignUpActivity";
//    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getMeThoseExtras = getIntent();

        preferencesManager = new SharedPreferencesManager(this);


        init();
        loadButtons();

        if (Objects.equals(getMeThoseExtras.getStringExtra(Utils.KEY_TYPE_PASSED), Utils.KEY_DONOR)) {
            whyGiveBlood.setVisibility(View.VISIBLE);
        }
        else if (Objects.equals(getMeThoseExtras.getStringExtra(Utils.KEY_TYPE_PASSED), Utils.KEY_RECEIVER)){
            whyGiveBlood.setVisibility(View.GONE);
        }
    }



    private void init() {
        name = findViewById(R.id.donorName);
        emailAddress = findViewById(R.id.emailAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        physicalAddress = findViewById(R.id.physicalAddress);
        password = findViewById(R.id.password);
        whyGiveBlood = findViewById(R.id.whyGiveBlood);
        bloodGroupField = findViewById(R.id.bloodGroupField);
        citySignUp = findViewById(R.id.citySignUp);
        provinceSignUp = findViewById(R.id.provinceSignUp);
        sign_up_layout = findViewById(R.id.sign_up_layout);

    }
    private void loadButtons() {
        bloodGroupField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZedUtils.showDialog("Select Blood Group", SignUp.this, bloodGroups, bloodGroupField);
            }
        });



        provinceSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ZedUtils.showDialog("Select Province", SignUp.this, ZedUtils.PROVINCES, provinceSignUp);
            }
        });

        citySignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provinceSignUp.getText().toString().isEmpty()){
                    citySignUp.setError("Please select a province first");
                }else{
                   ZedUtils.showDialog("Select City From "+provinceSignUp.getText().toString(), SignUp.this,
                           ZedUtils.getDistrictArray(provinceSignUp.getText().toString()), citySignUp);
                }

            }
        });

    }

    public void onSignUp(View view) {

            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.SIGN_UP,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject signUpObject = new JSONObject(response);

                                if (signUpObject.optString("error").equals("false")){

                                    Intent signUp = new Intent(SignUp.this, LoginActivity.class);
                                    signUp.putExtra("COOL", "signedUp");
                                    signUp.putExtra("Password", password.getText().toString());
                                    signUp.putExtra("Email", emailAddress.getText().toString());
                                    startActivity(signUp);
                                    finish();
                                }

                            }catch (JSONException error){
                                Log.d(TAG, "Error: "+error);
                                Utils.showSnackBar("We encounter an error, try again later",sign_up_layout, -1 );
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: "+error);
                    Utils.showSnackBar("We encounter an error, try again later",sign_up_layout, -1 );

                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> param = new HashMap<>();
                    param.put("name", name.getText().toString());
                    param.put("email", emailAddress.getText().toString());
                    param.put("phone_number", phoneNumber.getText().toString());
                    param.put("physical_address", physicalAddress.getText().toString());
                    param.put("password", password.getText().toString());
                    param.put("blood_group", bloodGroupField.getText().toString());
                    param.put("province", provinceSignUp.getText().toString());
                    param.put("city", citySignUp.getText().toString());
                    if (Objects.equals(getMeThoseExtras.getStringExtra(Utils.KEY_TYPE_PASSED), Utils.KEY_RECEIVER))
                        param.put("why_give_blood", "null");
                    else
                        param.put("why_give_blood", whyGiveBlood.getText().toString());
                    param.put("user_type", getMeThoseExtras.getStringExtra(Utils.KEY_TYPE_PASSED));
                    return param;
                }
            };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}