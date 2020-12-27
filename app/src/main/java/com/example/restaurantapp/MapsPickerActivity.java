package com.example.restaurantapp;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.restaurantapp.database.LatLon;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsPickerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location currentLocation;
    private LatLng new_restaurant_location =null;
    Task<Location> fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_picker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /////to render the activity in fullscreen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        //////
        ButterKnife.bind(this);
        if(getIntent().getExtras()!=null){
            LatLon latLon= (LatLon) getIntent().getExtras().getSerializable("location");
            new_restaurant_location=new LatLng(latLon.getLat(),latLon.getLon());
        }

    }


    @OnClick(R.id.mylocation_btn2)
    void markCurrentLocation(){
        MarkCurrentLocation();
    }

    @OnClick(R.id.pick_location_btn)
    void pickLocationBtnClicked(){
        if(new_restaurant_location!=null){
            Intent returnIntent = new Intent();
            LatLon location=new LatLon(new_restaurant_location.latitude,new_restaurant_location.longitude);
            returnIntent.putExtra("restaurant_location",location);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(new_restaurant_location!=null){
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(new_restaurant_location)
                    .title("")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new_restaurant_location, 15));
        }else{
            MarkCurrentLocation();
        }

        mMap.setOnMapLongClickListener(latLng -> {
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.clear();
            new_restaurant_location=latLng;
            mMap.addMarker(markerOptions.title(""));
        });
    }

    public void MarkCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        fusedLocationProviderClient.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                new_restaurant_location =new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()))
                        .title("My location ")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
            }
        });
    }
}