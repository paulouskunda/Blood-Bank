package com.visionarymindszm.bloodbank.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.utils.Utils;

public class ViewOneDonor extends AppCompatActivity {
    TextView donorName, donorBloodGroup, donorPhone, donorEmail, donorAddress, donorCity;
    String phoneNumber;
    Intent getDonor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_donor);
       getDonor = getIntent();

        init();
        loadValues();

    }

    private void init() {
        donorName= findViewById(R.id.donorName);
        donorBloodGroup= findViewById(R.id.donorBloodGroup);
        donorPhone= findViewById(R.id.donorPhone);
        donorEmail= findViewById(R.id.donorEmail);
        donorAddress= findViewById(R.id.donorAddress);
        donorCity= findViewById(R.id.donorCity);
    }

    private void loadValues(){

        donorName.setText(getDonor.getStringExtra(Utils.DONOR_NAME));
        donorBloodGroup.setText("Blood Group: "+getDonor.getStringExtra(Utils.DONOR_BLOOD_GROUP));
        donorPhone.setText("Phone Number: "+getDonor.getStringExtra(Utils.DONOR_PHONE_NUMBER));
        phoneNumber = getDonor.getStringExtra(Utils.DONOR_PHONE_NUMBER);
        donorAddress.setText("Donor Address: "+getDonor.getStringExtra(Utils.DONOR_ADDRESS));
        donorEmail.setText("Donor Email Address: "+getDonor.getStringExtra(Utils.DONOR_EMAIL));
        donorCity.setText("Donor City: "+getDonor.getStringExtra(Utils.DONOR_TOWN));

    }
    public void onCall(View view) {
        Intent onCallIntent = new Intent(Intent.ACTION_DIAL);
        onCallIntent.setData(Uri.parse("tel:"+phoneNumber));
        startActivity(onCallIntent);

    }

    public void onBookBloodDonation(View view) {
        Intent bookBloodDonation = new Intent(this, RequestBlood.class);
        bookBloodDonation.putExtra(Utils.DONOR_NAME, getDonor.getStringExtra(Utils.DONOR_NAME));
        bookBloodDonation.putExtra(Utils.DONOR_BLOOD_GROUP, getDonor.getStringExtra(Utils.DONOR_BLOOD_GROUP));
        bookBloodDonation.putExtra(Utils.DONOR_TOWN, getDonor.getStringExtra(Utils.DONOR_TOWN));
        bookBloodDonation.putExtra(Utils.DONOR_ID, getDonor.getStringExtra(Utils.DONOR_ID));
        startActivity(bookBloodDonation);
    }
}