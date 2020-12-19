package com.example.restaurantapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.restaurantapp.R;
import com.example.restaurantapp.database.Restaurant;

import java.util.ArrayList;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private ArrayList<Restaurant> restaurants;
    public RestaurantAdapter (Context context , int ressource , ArrayList <Restaurant> restaurants){
        super(context,ressource,restaurants);
        mcontext=context;
        this.restaurants=restaurants;
    }

    Context mcontext;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.restaurant_single_item,parent,false);

        TextView rest_etat = (TextView) convertView.findViewById(R.id.restaurant_etat);
        rest_etat.setText(restaurants.get(position).getEtat());

        TextView rest_name = (TextView) convertView.findViewById(R.id.restaurant_name);
        rest_name.setText(restaurants.get(position).getNom());

        TextView rest_desc = (TextView) convertView.findViewById(R.id.retaurant_description);
        rest_desc.setText(restaurants.get(position).getDescription());

        ImageView rest_img = (ImageView) convertView.findViewById(R.id.restaurant_image);
        int img_id=mcontext.getResources().getIdentifier(
                restaurants.get(position).getImage(),"drawable",mcontext.getPackageName()
        );
        rest_img.setImageResource(img_id);

        return convertView;
    }
}