package com.example.coffeenest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeenest.Model.Coffee;
import com.example.coffeenest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> {

    ArrayList<Coffee> mList;
    Context context;
    private OnCoffeeListener mOnCoffeeListener;

    public  CoffeeAdapter(Context context, ArrayList<Coffee> mList, OnCoffeeListener onCoffeeListener){
        this.mList = mList;
        this.context = context;
        this.mOnCoffeeListener = onCoffeeListener;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.coffee_item, parent, false);
        return new CoffeeViewHolder(v, mOnCoffeeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        Coffee coffee = mList.get(position);
        holder.coffee_name.setText( coffee.getName());
        holder.coffee_price.setText(coffee.getPrice() + " \u20ac");
        Picasso.get().load(coffee.getImage())
                .into(holder.coffee_image);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView coffee_name;
        TextView coffee_price;
        ImageView coffee_image;
        OnCoffeeListener onCoffeeListener;

        public CoffeeViewHolder(View itemView, OnCoffeeListener onCoffeeListener){
            super(itemView);

            coffee_name = itemView.findViewById(R.id.coffee_name);
            coffee_price = itemView.findViewById(R.id.coffee_price);
            coffee_image = itemView.findViewById(R.id.coffee_image);

            this.onCoffeeListener = onCoffeeListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onCoffeeListener.onCoffeeClick(getAdapterPosition());

        }
    }


    public interface OnCoffeeListener{
        void onCoffeeClick(int position);
    }


}
