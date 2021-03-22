package com.visionarymindszm.bloodbank.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_ADDRESS;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_BLOOD_GROUP;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_CITY;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_EMAIL;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_PHONE_NUMBER;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_TYPE;
import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_USERNAME;


public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    ConstraintLayout login_layout;
    SharedPreferencesManager preferencesManager;
    private String TAG = "LoginActivity";
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
    }

    public void loginMeIn(View view) {
        startActivity(new Intent(this, MainScreenActivity.class));
    }

    private void loginFunction(){
        if (TextUtils.isEmpty(username.getText().toString())){
            username.setError("Please provider your username or email");
        }else if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please provider your password");
        }else{
            final String usernamePara = username.getText().toString();
            final String passwordPara = password.getText().toString();

            // volley

            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Utils.LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject loginObject = new JSONObject(response);

                                if (loginObject.optString("error").equals("false")){
                                    JSONArray loginResults = loginObject.getJSONArray("message");
                                    HashMap<String, String> userDetails = new HashMap<>();
                                    for (int i = 0; i < loginResults.length(); i++){
                                        JSONObject messagePicked = loginResults.getJSONObject(i);
                                        userDetails.put(KEY_ADDRESS, messagePicked.getString("address"));
                                        userDetails.put(KEY_BLOOD_GROUP,messagePicked.getString("blood_group"));
                                        userDetails.put(KEY_EMAIL, messagePicked.getString("email"));
                                        userDetails.put(KEY_USERNAME, messagePicked.getString("name"));
                                        userDetails.put(KEY_CITY, messagePicked.getString("city"));
                                        userDetails.put(KEY_PHONE_NUMBER, messagePicked.getString("phone"));
                                        userDetails.put(KEY_TYPE, messagePicked.getString("type"));
                                    }


                                    preferencesManager.createUserLoginPref(userDetails);
                                    startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                                    finish();
                                }
                            }catch (JSONException error){
                                Log.d(TAG, "Error: "+error);
                                Utils.showSnackBar("We encounter an error, try again later",login_layout, -1 );

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: "+error);
                    Utils.showSnackBar("We encounter an error, try again later",login_layout, -1 );

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

}