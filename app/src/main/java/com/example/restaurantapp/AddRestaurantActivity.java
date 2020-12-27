package com.example.restaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.database.LatLon;
import com.example.restaurantapp.database.Restaurant;
import com.example.restaurantapp.database.RestaurantDB;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class AddRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        ButterKnife.bind(this);
        catId=getIntent().getExtras().getInt("categoryID");
        int unicode=0x1F4CD;
        String location_mark_emoji = getEmoji(unicode);
        addlocation_btn.setText(location_mark_emoji+" "+addlocation_btn.getText());
    }

    String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }

    @BindView(R.id.addrestaurant_addLocation)
    TextView addlocation_btn;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4){
            if(resultCode==Activity.RESULT_OK){
               LatLon local= (LatLon) data.getExtras().getSerializable("restaurant_location");
               restaurant_location=new LatLng(local.getLat(),local.getLon());

               TextView location_text=findViewById(R.id.addrestaurant_addLocation);
               location_text.setText("Location details : \n"+" "+getAddress(local.getLat(),local.getLon()));
            }
        }
    }

    public int catId;
    public LatLng restaurant_location=null;


    @OnClick(R.id.addrestaurant_back_btn)
    void backToPreviousAvtivity(){
        finish();
    }

    @BindView(R.id.addrestaurant_switch)
    Switch etat_switch;

    @BindView(R.id.addrestaurant_name)
    EditText resto_name;

    @BindView(R.id.addrestaurant_description)
    EditText resto_description;

    @BindView(R.id.addrestaurant_tel)
    EditText resto_tel;

    @OnClick(R.id.addrestaurant_addLocation)
    void locationPicker(){
        Intent locationPicker=new Intent(this,MapsPickerActivity.class);
        if(restaurant_location!=null){
            locationPicker.putExtra("location",new LatLon(restaurant_location.latitude,restaurant_location.longitude));
        }
        startActivityForResult(locationPicker,4);
    }

    @OnClick(R.id.addreastaurant_addbtn)
    void addNewRestaurant(){
        String name=resto_name.getText().toString().trim();
        String tel=resto_tel.getText().toString().trim();
        String description=resto_description.getText().toString().trim();
        double latitude=restaurant_location.latitude;
        double longitude=restaurant_location.longitude;
        String image="restaurant_default_img";
        boolean isOpen = etat_switch.isChecked();
        String etat="Close";
        if(isOpen) {
            etat="Open";
        }
        if(name.equals("") || tel.equals("") || description.equals("") || restaurant_location==null){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.map_resturant),"Invalid informations... ", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        ////insert restaurant to sqlite
        RestaurantDB database=new RestaurantDB(this);
        long  idR=database.insertInRestaurant(name,tel,latitude,longitude,etat,description,image);
        database.insertInRestaurantCategorie(catId,idR);
        Restaurant restaurant=new Restaurant(idR,name,tel,latitude,longitude,etat,description,image);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("new_restaurant",restaurant);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);//adresse
            //add = add + "\n name : " + obj.getFeatureName();
            // add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + "\n" + obj.getAdminArea();//region
//            add = add + "\n" + obj.getPostalCode();
            //          add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();//ville
            //Log.v("IGA", "Address" + add);
            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "NO ADRESSE FOUND";
        }
    }

private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "yog";
            String description ="yog";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("yog", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
void Notification(){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CharSequence name = "yog";
        String description = "yog";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("yog", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
// notificationId is a unique int for each notification that you must defin
    }
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "yog")
            .setSmallIcon(R.drawable.fastfood_icon)
            .setContentTitle("Notification")
            .setContentText("this is your first notification in android world")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
    notificationManager.notify(1, builder.build());

}
}