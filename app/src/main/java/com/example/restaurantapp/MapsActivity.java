package com.example.restaurantapp;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.restaurantapp.database.Restaurant;
import com.example.restaurantapp.directionjsonparser.DirectionParser;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location currentLocation;
    Task<Location> fusedLocationProviderClient;

    @TargetApi(Build.VERSION_CODES.R)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        }
        setContentView(R.layout.activity_restaurant_maps);
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

        ButterKnife.bind(this);
    }

    public Restaurant restaurant;
    public LatLng positionD,positionF;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //LatLng location = new LatLng(restaurant.getLatitude(),restaurant.getLongitude());
        LatLng location = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        positionF=location;
        mMap.addMarker(new MarkerOptions().position(location).title(restaurant.getNom()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
         }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);//adresse
            add = add + "\n name : " + obj.getFeatureName();
           // add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + "\n" + obj.getAdminArea();//region
//            add = add + "\n" + obj.getPostalCode();
            //          add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();//ville
            //Log.v("IGA", "Address" + add);
            return add;
        } catch (Exception e) {
            e.printStackTrace();
            return "NO ADRESSE FOUND";
        }
    }

    @OnClick(R.id.call_btn)
    public void calling_number() {
        Log.d("INFORMATION_btnCall", "CaLLING ..... " + restaurant.getTel());
        Intent calling = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurant.getTel()));
        startActivity(calling);
    }

    @OnClick(R.id.mylocation_btn2)
    void mylocation() {
        Log.d("INFORMATION_btnLocation", "your location will be marked in the map soon");
        MarkCurrentLocation();
    }

    @OnClick(R.id.details_btn)
    void details_location() {
        LatLng location = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        Snackbar snackbar = Snackbar.make(findViewById(R.id.map_resturant), getAddress(restaurant.getLatitude(),restaurant.getLongitude()), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @OnClick(R.id.scan_btn)
    void scanner(){
Intent scannerActivity= new Intent(this,ScanQrActivity.class);
startActivity(scannerActivity);
    }

    @OnClick(R.id.direction_btn)
    void direction(){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.map_resturant), "La fonction pour tracerla direction est dans le code source ,mais necessite un payement pour l'activation !", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    public void MarkCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        fusedLocationProviderClient.addOnSuccessListener(location -> {
            Log.d("information current ", "location" +location.toString());
            if (location != null) {
                currentLocation = location;
                Log.d("information current ", "location" + currentLocation.getLatitude() + " , " + currentLocation.getLongitude());
//                SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
                //              supportMapFragment.getMapAsync(MapsActivity.this);
                mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("here i am ! "));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
            }
        });
    }

    void trace_direction(){
        ///GET DIRection
        LatLng positionInitial=new LatLng(33.982581,-6.821499);//Localisation takdoum
        Log.d("INformation","my position in position takdoum = "+positionInitial.toString());
        String url=getRequesUrl(positionInitial,positionF);
        TaskRequestDirection taskRequestDirection=new TaskRequestDirection();
        taskRequestDirection.execute(url);
        Log.d("information",url);

    }

    public String getRequesUrl(LatLng origin,LatLng dest){
        String str_org="origin="+origin.latitude+","+origin.longitude;
        String str_des="destination="+dest.latitude+","+dest.longitude;
        String sensor="sensor=false";
        String mode="mode=driving";
        String key="key="+getResources().getString(R.string.google_maps_key);
        String param=str_org+"&"+str_des+"&"+sensor+"&"+mode+"&"+key;
        String output= "json";
        String url ="https://maps.googleapis.com/maps/api/directions/"+output+"?"+param;
        return url;
    }

    public String requestDirection(String reqUrl) throws IOException {
            String respone="";
        InputStream inputStream=null;
        HttpURLConnection httpURLConnection=null;
        try {
            URL url=new URL(reqUrl);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.connect();
            ///get the response
            inputStream=httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();
            String line="";
            while ( (line = bufferedReader.readLine()) !=null){
                stringBuffer.append(line);
            }
            respone=stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        }  catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                inputStream.close();
            }httpURLConnection.disconnect();
        }
        return respone;
    }

    public class TaskRequestDirection extends AsyncTask<String,Void,String > {

        @Override
        protected String doInBackground(String... strings) {
            String response="";
                try {
                    response=requestDirection(strings[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //parse json here
            TaskParser taskParser=new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String,Void,List<List<HashMap<String,String>>> >{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject=null;
            List<List<HashMap<String,String>>> routes=null;
            try {
                jsonObject=new JSONObject(strings[0]);
                Log.d("information jsonobject",jsonObject.toString());

                DirectionParser directionParser=new DirectionParser();
                routes=directionParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //get list route and displayin into map
            ArrayList points=null;
            PolylineOptions polygonOptions=null;
            for (List<HashMap<String,String>> path : lists){
                points=new ArrayList();
                polygonOptions=new PolylineOptions();
                for ( HashMap<String,String> point :path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat, lon));
                }
                polygonOptions.addAll(points);
                polygonOptions.color(Color.BLUE);
                polygonOptions.geodesic(true);
                polygonOptions.width(15);
            }
            if(polygonOptions != null){
                mMap.addPolyline(polygonOptions);
            }else {
                Toast.makeText(MapsActivity.this, "Direction not found ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}