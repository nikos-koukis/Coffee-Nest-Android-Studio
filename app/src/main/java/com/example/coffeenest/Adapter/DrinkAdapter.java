package com.example.coffeenest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeenest.Model.Drink;
import com.example.coffeenest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    ArrayList<Drink> myList;
    Context context;
    private OnDrinkListener mOnDrinkListener;

    public  DrinkAdapter(Context context, ArrayList<Drink> myList, OnDrinkListener onDrinkListener){
        this.myList = myList;
        this.context = context;
        this.mOnDrinkListener = onDrinkListener;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.drinks_item, parent, false);
        return new DrinkViewHolder(v, mOnDrinkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = myList.get(position);

        holder.drinkDescription.setText( drink.getDrinkDescription());
        holder.drinkName.setText( drink.getDrinkName());
        holder.drinkPrice.setText(drink.getDrinkPrice() + " \u20ac");
        Picasso.get().load(drink.getDrinkImage())
                .into(holder.drinkImage);



    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView drinkName;
        TextView drinkPrice;
        TextView drinkDescription;
        ImageView drinkImage;
        OnDrinkListener onDrinkListener;

        public DrinkViewHolder(View itemView, OnDrinkListener onDrinkListener){
            super(itemView);

            drinkName = itemView.findViewById(R.id.drink_name);
            drinkPrice = itemView.findViewById(R.id.drink_price);
            drinkDescription = itemView.findViewById(R.id.drink_description);
            drinkImage = itemView.findViewById(R.id.drink_image);

            this.onDrinkListener = onDrinkListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onDrinkListener.onDrinkClick(getAdapterPosition());

        }
    }

    public interface OnDrinkListener{
        void onDrinkClick(int position);
    }


}
