package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.adapters.RestaurantAdapter;
import com.example.restaurantapp.adapters.RestaurantRecycleAdapter;
import com.example.restaurantapp.authentification.SignInActivity;
import com.example.restaurantapp.database.Categorie;
import com.example.restaurantapp.database.Restaurant;
import com.example.restaurantapp.database.RestaurantDB;
import com.example.restaurantapp.utility.VerticalSpacingItemDecorator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class HomeRecycleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_recycle_view);
        ButterKnife.bind(this);
        ref = getSharedPreferences("myapp", MODE_PRIVATE);
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
            categorie =(Categorie) intent.getSerializableExtra("category_object");
            restaurant_acticity_title.setText(categorie.getNom());
            //remplir la liste des restaurant
            RestaurantDB dao=new RestaurantDB(this);
            restaurants = dao.readRestaurantsByCategorie(categorie.getId());
            ///attach swiping to our recycleview
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            restaurant_list.setLayoutManager(linearLayoutManager);
            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
            restaurant_list.addItemDecoration(itemDecorator);
            restaurantAdapter=new RestaurantRecycleAdapter(getApplicationContext(), (ArrayList<Restaurant>) restaurants);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(restaurant_list);
            restaurant_list.setAdapter(restaurantAdapter);
            toggleNoItemFoundContainer((ArrayList<Restaurant>) restaurants);
        }
    }


    RestaurantRecycleAdapter restaurantAdapter;
    private  SharedPreferences ref;
    private Categorie categorie;
    private List<Restaurant> restaurants;
    private Restaurant restaurant_clicked;

    @BindView(R.id.no_item_found_container)
    View no_item_found;

    @BindView(R.id.restaurant_activity_title)
    TextView restaurant_acticity_title;

    @BindView(R.id.restaurant_recycle)
    RecyclerView restaurant_list;

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
        RestaurantRecycleAdapter restaurantAdapter=new RestaurantRecycleAdapter(getApplicationContext(),restaurants_match_search);
        restaurant_list.setAdapter(restaurantAdapter);
        toggleNoItemFoundContainer(restaurants_match_search);
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
                restaurantAdapter=new RestaurantRecycleAdapter(getApplicationContext(), (ArrayList<Restaurant>) restaurants);
                restaurant_list.setAdapter(restaurantAdapter);
                restaurantAdapter.notifyDataSetChanged();
            }
        }
    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
             Restaurant extrait=(Restaurant) restaurants.remove(viewHolder.getAdapterPosition());
            RestaurantRecycleAdapter restaurantAdapter=new RestaurantRecycleAdapter(getApplicationContext(), (ArrayList<Restaurant>) restaurants);
            restaurant_list.setAdapter(restaurantAdapter);
            restaurantAdapter.notifyDataSetChanged();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintLayout),extrait.getNom()+" was deleted", Snackbar.LENGTH_LONG);
            snackbar.setTextColor(Color.MAGENTA);
            snackbar.setAction("UNDO",new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        restaurants.add(0,extrait);
                    RestaurantRecycleAdapter restaurantAdapter=new RestaurantRecycleAdapter(getApplicationContext(), (ArrayList<Restaurant>) restaurants);
                    restaurant_list.setAdapter(restaurantAdapter);
                    restaurantAdapter.notifyDataSetChanged();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
            Log.d("RESTO_ACTIVITY",extrait.toString());
        }
    };

    void toggleNoItemFoundContainer(ArrayList<Restaurant> list){
        if(list.isEmpty()){
            no_item_found.setVisibility(View.VISIBLE);
            restaurant_list.setVisibility(View.INVISIBLE);
        }else {
            no_item_found.setVisibility(View.INVISIBLE);
            restaurant_list.setVisibility(View.VISIBLE);
        }
    }

    String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }
}