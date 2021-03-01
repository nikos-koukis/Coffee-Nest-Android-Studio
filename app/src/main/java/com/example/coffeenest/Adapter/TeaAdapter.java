package com.example.coffeenest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeenest.Model.Tea;
import com.example.coffeenest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeaAdapter extends RecyclerView.Adapter<TeaAdapter.TeaViewHolder> {

    ArrayList<Tea> myList;
    Context context;
    private OnTeaListener mOnTeaListener;

    public TeaAdapter(Context context, ArrayList<Tea> myList, OnTeaListener onTeaListener) {
        this.myList = myList;
        this.context = context;
        this.mOnTeaListener = onTeaListener;
    }

    @NonNull
    @Override
    public TeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.tea_item, parent, false);
        return new TeaViewHolder(v, mOnTeaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeaViewHolder holder, int position) {

        Tea tea = myList.get(position);

        holder.teaName.setText(tea.getTeaName());
        holder.teaDescription.setText(tea.getTeaDescription());
        holder.teaPrice.setText(tea.getTeaPrice());
        Picasso.get().load(tea.getTeaImage())
                .into(holder.teaImage);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class TeaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView teaName,teaDescription,teaPrice;
        ImageView teaImage;

        OnTeaListener onTeaListener;

        public TeaViewHolder(View itemView, OnTeaListener onTeaListener) {
            super(itemView);

            teaName = itemView.findViewById(R.id.tea_name);
            teaDescription = itemView.findViewById(R.id.tea_description);
            teaPrice = itemView.findViewById(R.id.tea_price);

            teaImage = itemView.findViewById(R.id.tea_image);

            this.onTeaListener = onTeaListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onTeaListener.onTeaClick(getAdapterPosition());

        }
    }

    public interface OnTeaListener {
        void onTeaClick(int position);
    }


}