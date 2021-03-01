package com.example.coffeenest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeenest.Model.Snack;
import com.example.coffeenest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.SnackViewHolder> {

    ArrayList<Snack> myList;
    Context context;
    private OnSnackListener mOnSnackListener;

    public SnackAdapter(Context context, ArrayList<Snack> myList, OnSnackListener onSnackListener) {
        this.myList = myList;
        this.context = context;
        this.mOnSnackListener = onSnackListener;
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.snack_item, parent, false);
        return new SnackViewHolder(v, mOnSnackListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SnackViewHolder holder, int position) {

        Snack snack = myList.get(position);

        holder.snackName.setText(snack.getSnackName());
        holder.snackDescription.setText(snack.getSnackDescription());
        holder.snackPrice.setText(snack.getSnackPrice());
        Picasso.get().load(snack.getSnackImage())
                .into(holder.snackImage);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class SnackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView snackName,snackDescription,snackPrice;
        ImageView snackImage;

        OnSnackListener onSnackListener;

        public SnackViewHolder(View itemView, OnSnackListener onSnackListener) {
            super(itemView);

            snackName = itemView.findViewById(R.id.snack_name);
            snackDescription = itemView.findViewById(R.id.snack_description);
            snackPrice = itemView.findViewById(R.id.snack_price);

            snackImage = itemView.findViewById(R.id.snack_image);

            this.onSnackListener = onSnackListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onSnackListener.onSnackClick(getAdapterPosition());

        }
    }

    public interface OnSnackListener {
        void onSnackClick(int position);
    }


}