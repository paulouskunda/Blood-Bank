package com.visionarymindszm.bloodbank.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.bloodbank.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_ADDRESS;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_BLOOD_GROUP;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_CITY;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_EMAIL;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_PASSWORD;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_PHONE_NUMBER;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_PHYSICAL_ADD;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_PROVINCE;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_TYPE;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USERNAME;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USER_ID;


public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    ConstraintLayout login_layout;
    SharedPreferencesManager preferencesManager;
    private String TAG = "LoginActivity";
    private ProgressBar progressBarLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent getMe = getIntent();

        init();
        if (Objects.equals(getMe.getStringExtra("COOL"), "signedUp")){
            Utils.showSnackBar("Sign up complete", login_layout, 0);
        }
        preferencesManager = new SharedPreferencesManager(this);

        username.setText(getMe.getStringExtra("Email"));
        password.setText(getMe.getStringExtra("Password"));

    }

    private void init() {
        username = findViewById(R.id.username_email);
        password = findViewById(R.id.passwordLogin);
        login_layout = findViewById(R.id.login_layout);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.INVISIBLE);
    }

    public void loginMeIn(View view) {

        loginFunction();
//        startActivity(new Intent(this, MainScreenActivity.class));
    }

    private void loginFunction(){
        if (TextUtils.isEmpty(username.getText().toString())){
            username.setError("Please provider your username or email");
        }else if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please provider your password");
        }else{
            final String usernamePara = username.getText().toString();
            final String passwordPara = password.getText().toString();
            progressBarLogin.setVisibility(View.VISIBLE);
            // volley

            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                Utils.showToasterShort(LoginActivity.this,"we", 0 );
                                JSONObject loginObject = new JSONObject(response);

                                if (loginObject.optString("error").equals("false")){
                                    Utils.showToasterShort(LoginActivity.this,"lol", 0 );

                                    JSONObject messagePicked =  new JSONObject(String.valueOf(loginObject.getJSONObject("message")));
                                    HashMap<String, String> userDetails = new HashMap<>();

                                    userDetails.put(KEY_PHYSICAL_ADD, messagePicked.getString("physical_address"));
                                    userDetails.put(KEY_BLOOD_GROUP,messagePicked.getString("blood_group"));
                                    userDetails.put(KEY_EMAIL, messagePicked.getString("email"));
                                    userDetails.put(KEY_USERNAME, messagePicked.getString("name"));
                                    userDetails.put(KEY_CITY, messagePicked.getString("city"));
                                    userDetails.put(KEY_PHONE_NUMBER, messagePicked.getString("phone_number"));
                                    userDetails.put(KEY_TYPE, messagePicked.getString("type_of_user"));
                                    userDetails.put(KEY_USER_ID, messagePicked.getString("id"));
                                    userDetails.put(KEY_PROVINCE, messagePicked.getString("province"));
                                    userDetails.put(KEY_PASSWORD, messagePicked.getString("password"));
                                    Log.d(TAG, ""+userDetails.get(KEY_CITY));

                                    Log.d(TAG, "Hash "+ Collections.singletonList(userDetails));
                                    preferencesManager.createUserLoginPref(userDetails);
                                    startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                                    finish();
                                }else {
                                    Utils.showSnackBar(loginObject.getString("message"),login_layout, -1 );
                                }
                            }catch (JSONException error){
                                Log.d(TAG, "Error: "+error);
                                Utils.showSnackBar("We encounter an error, try again later",login_layout, -1 );
                                progressBarLogin.setVisibility(View.INVISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: "+error);
                    Utils.showSnackBar("We encounter an error, try again later",login_layout, -1 );
                    progressBarLogin.setVisibility(View.INVISIBLE);
                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> param = new HashMap<>();
                    param.put("username", usernamePara);
                    param.put("password", passwordPara);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }

    public void request_blood(View view) {
        Intent request_blood = new Intent(this, SignUp.class);
        request_blood.putExtra(Utils.KEY_TYPE_PASSED, Utils.KEY_RECEIVER);
        startActivity(request_blood);
        finish();
    }

    public void register_donor(View view) {
        Intent register_donor = new Intent(this, SignUp.class);
        register_donor.putExtra(Utils.KEY_TYPE_PASSED, Utils.KEY_DONOR);
        startActivity(register_donor);
        finish();
    }
}