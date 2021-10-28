package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;
import com.visionarymindszm.zededitoptions.ZedUtils;

import java.util.Collections;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.*;

public class ProfileActivity extends AppCompatActivity {
    private EditText your_name, your_email, your_number, your_province, your_city, your_physical_address;
    private Button updateButton;
    private SharedPreferencesManager preferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferencesManager = new SharedPreferencesManager(this);
        init();
        onLoad();
        Log.d("Profile", "Hash: "+ Collections.singletonList(preferencesManager.userDetails()));
        onListenToMe();
    }


    private void init() {
        your_city = findViewById(R.id.your_city);
        your_name = findViewById(R.id.your_name);
        your_email = findViewById(R.id.your_email);
        your_number = findViewById(R.id.your_number);
        your_province = findViewById(R.id.your_province);
        your_physical_address = findViewById(R.id.your_physical_address);
        updateButton = findViewById(R.id.updateButton);
    }

    private void onLoad(){
        your_city.setText(preferencesManager.userDetails().get(KEY_CITY));
        your_name.setText(preferencesManager.userDetails().get(KEY_USERNAME));
        your_email.setText(preferencesManager.userDetails().get(KEY_EMAIL));
        your_number.setText(preferencesManager.userDetails().get(KEY_PHONE_NUMBER));
        your_province.setText(preferencesManager.userDetails().get(KEY_PROVINCE));
        your_physical_address.setText(preferencesManager.userDetails().get(KEY_PHYSICAL_ADD));

        updateButton.setEnabled(false);



    }
    private void onListenToMe() {
        your_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!your_name.getText().toString().equalsIgnoreCase(preferencesManager.userDetails().get(KEY_USERNAME))){
                    updateButton.setEnabled(true);
                }else{
                    updateButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        your_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!your_email.getText().toString().equalsIgnoreCase(preferencesManager.userDetails().get(KEY_EMAIL))){
                    updateButton.setEnabled(true);
                }else{
                    updateButton.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        your_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!your_number.getText().toString().equalsIgnoreCase(preferencesManager.userDetails().get(KEY_PHONE_NUMBER))){
                    updateButton.setEnabled(true);
                }else{
                    updateButton.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        your_physical_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        your_physical_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!your_physical_address.getText().toString().equalsIgnoreCase(preferencesManager.userDetails().get(KEY_PHYSICAL_ADD))){
                    updateButton.setEnabled(true);
                }else{
                    updateButton.setEnabled(false);

                }
            }
        });
        your_province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZedUtils.showDialog("Select A New Province", ProfileActivity.this, ZedUtils.PROVINCES, your_province);
                if (!your_physical_address.getText().toString().equalsIgnoreCase(preferencesManager.userDetails().get(KEY_CITY))){
                    updateButton.setEnabled(true);
                }else{
                    updateButton.setEnabled(false);

                }
            }
        });

        your_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZedUtils.showDialog("Select New City", ProfileActivity.this,
                        ZedUtils.getDistrictArray(your_province.getText().toString()), your_city);
                if (!your_city.getText().toString().equalsIgnoreCase(preferencesManager.userDetails().get(KEY_CITY))){
                    updateButton.setEnabled(true);
                }else{
                    updateButton.setEnabled(false);

                }
            }
        });



    }



}