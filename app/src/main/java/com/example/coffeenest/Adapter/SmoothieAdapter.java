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
import com.example.coffeenest.Model.Smoothie;
import com.example.coffeenest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SmoothieAdapter extends RecyclerView.Adapter<SmoothieAdapter.SmoothieViewHolder> {

    ArrayList<Smoothie> myList;
    Context context;
    private OnSmoothieListener mOnSmoothieListener;

    public SmoothieAdapter(Context context, ArrayList<Smoothie> myList, OnSmoothieListener onSmoothieListener) {
        this.myList = myList;
        this.context = context;
        this.mOnSmoothieListener = onSmoothieListener;
    }

    @NonNull
    @Override
    public SmoothieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.smoothie_item, parent, false);
        return new SmoothieViewHolder(v, mOnSmoothieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SmoothieViewHolder holder, int position) {

        Smoothie smoothie = myList.get(position);

        holder.smoothieName.setText(smoothie.getSmoothieName());
        holder.smoothieDescription.setText(smoothie.getSmoothieDescription());
        holder.smoothiePrice.setText(smoothie.getSmoothiePrice());
        Picasso.get().load(smoothie.getSmoothieImage())
                .into(holder.smoothieImage);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class SmoothieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView smoothieName,smoothieDescription,smoothiePrice;
        ImageView smoothieImage;

        OnSmoothieListener onSmoothieListener;

        public SmoothieViewHolder(View itemView, OnSmoothieListener onSmoothieListener) {
            super(itemView);

            smoothieName = itemView.findViewById(R.id.smoothie_name);
            smoothieDescription = itemView.findViewById(R.id.smoothie_description);
            smoothiePrice = itemView.findViewById(R.id.smoothie_price);

            smoothieImage = itemView.findViewById(R.id.smoothie_image);

            this.onSmoothieListener = onSmoothieListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onSmoothieListener.onSmoothieClick(getAdapterPosition());

        }
    }

    public interface OnSmoothieListener {
        void onSmoothieClick(int position);
    }


}