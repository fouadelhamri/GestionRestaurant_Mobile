package com.example.restaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.adapters.RestaurantAdapter;
import com.example.restaurantapp.authentification.SignInActivity;
import com.example.restaurantapp.database.Categorie;
import com.example.restaurantapp.database.Restaurant;
import com.example.restaurantapp.database.RestaurantDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);
        ref = getSharedPreferences("myapp", MODE_PRIVATE);
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
           categorie =(Categorie) intent.getSerializableExtra("category_object");
            restaurant_acticity_title.setText(categorie.getNom());
            //remplir la liste des restaurant
            RestaurantDB dao=new RestaurantDB(this);
            restaurants = dao.readRestaurantsByCategorie(categorie.getId());
            restaurantAdapter=new RestaurantAdapter(getApplicationContext(),R.layout.restaurant_single_item, (ArrayList<Restaurant>) restaurants);
            restaurant_list.setAdapter(restaurantAdapter);
            restaurant_list.setEmptyView(findViewById(R.id.no_item_found_container));
        }

    }
    RestaurantAdapter restaurantAdapter;
    private  SharedPreferences ref;
    private Categorie categorie;
    private List<Restaurant> restaurants;
    private Restaurant restaurant_clicked;
    @BindView(R.id.restaurant_activity_title)
    TextView restaurant_acticity_title;

    @BindView(R.id.restaurant_list)
    ListView restaurant_list;

    @BindView(R.id.recherche_input)
    EditText searchbare_EditText;

    @OnClick(R.id.restaurant_back_btn)
    void backToPreviousAvtivity(){
        finish();
    }

    @OnClick(R.id.exit_btn)
    void logout(){
        ref.edit().clear().apply();
        ref.edit().putBoolean("isLogged",false).apply();
        Intent signin=new Intent(this, SignInActivity.class);
        startActivity(signin);
        finish();
    }

    @OnTextChanged(R.id.recherche_input)
    void searchRestaurantByName(){
        String restaurant_search_field=searchbare_EditText.getText().toString().toLowerCase();
        ArrayList<Restaurant> restaurants_match_search= new ArrayList<>();
        for(int i=0;i<restaurants.size();i++){
            if(restaurants.get(i).getNom().toLowerCase().contains(restaurant_search_field)){
                restaurants_match_search.add(restaurants.get(i));
            }
        }
        RestaurantAdapter restaurantAdapter=new RestaurantAdapter(getApplicationContext(),R.layout.restaurant_single_item,restaurants_match_search);
        restaurant_list.setAdapter(restaurantAdapter);
    }

    @OnClick(R.id.new_btn)
    void CreateNewRestaurant()
    {
        Intent newResto =new Intent(this, AddRestaurantActivity.class);
        newResto.putExtra("categoryID",categorie.getId());
        startActivityForResult(newResto,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Restaurant new_restaurant = (Restaurant) data.getSerializableExtra("new_restaurant");
                restaurants.add(0,new_restaurant);
                restaurantAdapter=null;
                restaurantAdapter=new RestaurantAdapter(getApplicationContext(),R.layout.restaurant_single_item, (ArrayList<Restaurant>) restaurants);
                restaurant_list.setAdapter(restaurantAdapter);
                restaurant_list.setEmptyView(findViewById(R.id.no_item_found_container));
                restaurantAdapter.notifyDataSetChanged();
            }
        }
    }

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
    String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }
}