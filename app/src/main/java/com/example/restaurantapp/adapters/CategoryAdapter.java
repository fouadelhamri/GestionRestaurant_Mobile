package com.example.restaurantapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.HomeActivity;
import com.example.restaurantapp.HomeRecycleViewActivity;
import com.example.restaurantapp.R;
import com.example.restaurantapp.RestaurantActivity;
import com.example.restaurantapp.database.Categorie;

import java.util.List;

import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<Categorie> categoryList;
    List<Integer> imageList;
    LayoutInflater inflater;
    Context mcontext;
    public CategoryAdapter(Context context, List<Categorie> catList, List<Integer> images){
        this.imageList=images;
        this.categoryList=catList;
        this.mcontext=context;
        this.inflater= LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.category_single_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(categoryList.get(position).getNom());
        holder.img.setImageResource(imageList.get(position));
        holder.itemView.setOnClickListener(v -> {
            //Intent restaurants=new Intent(mcontext, RestaurantActivity.class);
            Intent restaurants=new Intent(mcontext, HomeRecycleViewActivity.class);
            restaurants.putExtra("category_object",categoryList.get(position));
            mcontext.startActivity(restaurants);
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.category_name);
        img=itemView.findViewById(R.id.category_image);
    }
}
}
