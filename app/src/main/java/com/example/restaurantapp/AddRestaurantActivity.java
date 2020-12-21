package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.restaurantapp.database.Restaurant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        ButterKnife.bind(this);
    }

    @BindView(R.id.addrestaurant_name)
    EditText resto_name;

    @BindView(R.id.addrestaurant_description)
    EditText resto_description;

    @BindView(R.id.addrestaurant_tel)
    EditText resto_tel;

    @OnClick(R.id.addreastaurant_addbtn)
    void addNewRestaurant(){
        String name=resto_name.getText().toString();
        String tel=resto_tel.getText().toString();
        String description=resto_description.getText().toString();
        double latitude=33.956621;
        double longitude=33.956621;
        String image="restaurant_default_img";
        Restaurant restaurant=new Restaurant(22,name,tel,latitude,longitude,"fermé",description,image);
        Log.d("INFOR","msg: "+restaurant.toString());
    }

}