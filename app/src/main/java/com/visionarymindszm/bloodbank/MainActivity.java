package com.visionarymindszm.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.visionarymindszm.bloodbank.screens.LoginActivity;
import com.visionarymindszm.bloodbank.screens.MainScreenActivity;
import com.visionarymindszm.bloodbank.screens.SignUp;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.bloodbank.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(this);
        if (!preferencesManager.checkLogin()){
            startActivity(new Intent(this, MainScreenActivity.class));
            finish();
        }

    }

    public void request_blood(View view) {
        Intent request_blood = new Intent(this, SignUp.class);
        request_blood.putExtra(Utils.KEY_TYPE_PASSED, Utils.KEY_RECEIVER);
        startActivity(request_blood);
    }

    public void register_donor(View view) {
        Intent register_donor = new Intent(this, SignUp.class);
        register_donor.putExtra(Utils.KEY_TYPE_PASSED, Utils.KEY_DONOR);
        startActivity(register_donor);
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}