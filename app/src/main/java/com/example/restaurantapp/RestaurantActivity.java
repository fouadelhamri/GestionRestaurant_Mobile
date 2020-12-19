package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.adapters.RestaurantAdapter;
import com.example.restaurantapp.database.Categorie;
import com.example.restaurantapp.database.Restaurant;
import com.example.restaurantapp.database.RestaurantDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
           categorie =(Categorie) intent.getSerializableExtra("category_object");
            restaurant_acticity_title.setText(categorie.getNom());
            //remplir la liste des restaurant
            RestaurantDB dao=new RestaurantDB(this);
            restaurants = dao.readRestaurantsByCategorie(categorie.getId());
            RestaurantAdapter restaurantAdapter=new RestaurantAdapter(getApplicationContext(),R.layout.restaurant_single_item, (ArrayList<Restaurant>) restaurants);
            restaurant_list.setAdapter(restaurantAdapter);
        }

    }
    private Categorie categorie;
    private List<Restaurant> restaurants;
    private Restaurant restaurant_clicked;
    @BindView(R.id.restaurant_activity_title)
    TextView restaurant_acticity_title;

    @BindView(R.id.restaurant_list)
    ListView restaurant_list;


    @OnItemClick(R.id.restaurant_list)
    void openMapOnRestaurantLocation(int position) {
        {
            this.restaurant_clicked = (Restaurant) restaurant_list.getAdapter().getItem(position);
            Log.d("MSG","EHOOOOOOOOOOO "+restaurant_clicked.getLatitude());

            if (this.restaurant_clicked != null) {
                Intent sendRestaurant = new Intent(this, MapsActivity.class);
                sendRestaurant.putExtra("restaurant", this.restaurant_clicked);
                startActivity(sendRestaurant);
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }
    }
}