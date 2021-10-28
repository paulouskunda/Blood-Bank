package com.visionarymindszm.bloodbank.screens;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.Camera;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.models.MapItemsExample;
import com.visionarymindszm.bloodbank.screens.hereMaps.PermissionsRequestor;
import com.visionarymindszm.bloodbank.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HereMapsActivity extends AppCompatActivity  {
    private PermissionsRequestor permissionsRequestor;
    private MapViewLite mapView;
    private MapItemsExample mapItemsExample;
    private static final String TAG = HereMapsActivity.class.getSimpleName();
    private Double latitude = null;
    private Double longitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here_maps);
        // Get a MapView instance from layout
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        Button viewListHospital = findViewById(R.id.viewListHospital);
        viewListHospital.setOnClickListener(viewHos -> {
            startActivity(new Intent(this, FindHospital.class));
            finish();
        });
        ArrayList<String> locationFromFindHospital = getIntent().getStringArrayListExtra("Arrays");
        latitude = Double.valueOf(locationFromFindHospital.get(0));
        longitude = Double.valueOf(locationFromFindHospital.get(1));
        Utils.LOCATION_LIST_MODE.forEach(locationsModel -> {
            Log.i(TAG, "Cool Locations =>"+ locationsModel.getPlaceName());
        });
        handleAndroidPermissions();
    }


    private void handleAndroidPermissions() {
        permissionsRequestor = new PermissionsRequestor(this);
        permissionsRequestor.request(new PermissionsRequestor.ResultListener() {

            @Override
            public void permissionsGranted() {
                loadMapScene();
            }

            @Override
            public void permissionsDenied() {
                Log.e(TAG, "Permissions denied by user.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsRequestor.onRequestPermissionsResult(requestCode, grantResults);
    }

    private void loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, errorCode -> {
            if (errorCode == null) {
                mapItemsExample = new MapItemsExample(HereMapsActivity.this, mapView);
                Camera mapViewCamera = mapView.getCamera();
                mapViewCamera.setTarget(new GeoCoordinates(latitude, longitude));
                mapViewCamera.setZoomLevel(11);
                mapItemsExample.showAnchoredMapMarkers(Utils.LOCATION_LIST_MODE);
                mapItemsExample.showAnchoredMapMarkerForCurrentUser(latitude, longitude, "Your Location");
            } else {
                Log.d(TAG, "onLoadScene failed: " + errorCode.toString());
            }
        });
    }


}