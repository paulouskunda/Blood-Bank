package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.utils.SharedPreferencesManager;

import java.util.HashMap;
import java.util.Objects;

import static com.visionarymindszm.bloodbank.utils.SharedPreferencesManager.KEY_TYPE;

public class MainScreenActivity extends AppCompatActivity {
    ConstraintLayout main_screen_layout;
    CardView donorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        main_screen_layout = findViewById(R.id.main_screen_layout);
        donorList = findViewById(R.id.donorList);

        if (Objects.equals(sharedPreferencesManager.userDetails().get(KEY_TYPE), "donor")){
            donorList.setVisibility(View.GONE);
        }


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
        startActivity(new Intent(this, DonorActivity.class));
    }
}