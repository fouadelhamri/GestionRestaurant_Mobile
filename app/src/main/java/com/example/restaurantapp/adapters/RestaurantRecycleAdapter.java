package com.example.restaurantapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.MapsActivity;
import com.example.restaurantapp.R;
import com.example.restaurantapp.database.Restaurant;

import java.util.ArrayList;

public class RestaurantRecycleAdapter extends RecyclerView.Adapter<RestaurantRecycleAdapter.ViewHolder> {
    public RestaurantRecycleAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.mcontext=context;
        this.inflater= LayoutInflater.from(context);
        this.restaurants=restaurants;
    }
    public Context mcontext;
    public LayoutInflater inflater;
    public ArrayList<Restaurant> restaurants;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.restaurant_single_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int img_id=mcontext.getResources().getIdentifier(
                restaurants.get(position).getImage(),"drawable",mcontext.getPackageName()
        );
        holder.rest_img.setImageResource(img_id);
        holder.rest_desc.setText(restaurants.get(position).getDescription());
        holder.rest_name.setText(restaurants.get(position).getNom());
        holder.rest_etat.setText(restaurants.get(position).getEtat());
        holder.itemView.setOnClickListener(v -> {
           Restaurant restaurant_clicked = restaurants.get(position);
            if (restaurant_clicked != null) {
                Intent sendRestaurant = new Intent(mcontext, MapsActivity.class);
                sendRestaurant.putExtra("restaurant",restaurant_clicked);
                sendRestaurant.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(sendRestaurant);
            }else{
                Toast.makeText(mcontext, "ERROR", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rest_etat;
        TextView rest_name;
        TextView rest_desc;
        ImageView rest_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rest_desc=itemView.findViewById(R.id.retaurant_description);
            rest_name=itemView.findViewById(R.id.restaurant_name);
            rest_etat=itemView.findViewById((R.id.restaurant_etat));
            rest_img=itemView.findViewById(R.id.restaurant_image);
        }
    }
}
