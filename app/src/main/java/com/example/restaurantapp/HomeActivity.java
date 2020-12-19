package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.adapters.CategoryAdapter;
import com.example.restaurantapp.authentification.SignInActivity;
import com.example.restaurantapp.database.Categorie;
import com.example.restaurantapp.database.RestaurantDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        ref = getSharedPreferences("myapp", MODE_PRIVATE);
        /*if(!ref.getBoolean("isLogged",false)){
            Intent login=new Intent(this, SignInActivity.class);
            startActivity(login);
        }*/
        int unicode=0x1F64C;
        String hello_emoji = getEmoji(unicode);
        TextView useremail=(TextView)findViewById(R.id.usermail);
        useremail.setText(hello_emoji+" "+ref.getString("username","display usermail here !")+" "+hello_emoji);
        //////////INSERT CATEGORIES IN SQLITE
        databaseManager.databaseInitialisation();
        ////get DATA

        categories_db =databaseManager.readCategorie();

        initData();
        //initialize adapter
        CategoryAdapter adapter=new CategoryAdapter(this,categories_db,images);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        categories_list.setLayoutManager(gridLayoutManager);
        categories_list.setAdapter(adapter);

    }

    RestaurantDB databaseManager=new RestaurantDB(this);
    SharedPreferences ref;
    List<Categorie> categories_db;
    List<Integer>images;

    @BindView(R.id.categories_list)
    RecyclerView categories_list;

public void initData(){
    images=new ArrayList<>();
    boolean isfastfood=false;
    if(categories_db.size()>0){
        for(Categorie title : categories_db){
            if (isfastfood == true) {
                images.add(R.drawable.restaurant_icon);
                isfastfood=!isfastfood;
            } else {
                images.add(R.drawable.fastfood_icon);
                isfastfood=!isfastfood;
            }
        }
    }




}
    String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }
}