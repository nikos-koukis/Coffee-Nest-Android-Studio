package com.example.coffeenest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeenest.Database.Database;
import com.example.coffeenest.Model.CoffeeOrder;
import com.example.coffeenest.Model.Request;
import com.example.coffeenest.Model.UserOrder;
import com.example.coffeenest.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHolder> {

    ArrayList<UserOrder> myList;
    Context context;

    public UserOrderAdapter(Context context, ArrayList<UserOrder> myList) {
        this.myList = myList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new UserOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position) {

        UserOrder userOrder = myList.get(position);

        holder.orderDateText.setText(userOrder.getUserOrderDateTime());
        holder.orderTotalPriceText.setText(userOrder.getUserOrderTotalPrice());
        holder.paymentMethodText.setText(userOrder.getUserOrderPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class UserOrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderDate,orderDateText,orderTotalPrice,orderTotalPriceText,paymentMethod,paymentMethodText;


        public UserOrderViewHolder(View itemView) {
            super(itemView);

            orderDate = (itemView).findViewById(R.id.order_date);
            orderDateText = (itemView).findViewById(R.id.order_date_text);
            orderTotalPrice = (itemView).findViewById(R.id.order_total_price);
            orderTotalPriceText = (itemView).findViewById(R.id.order_total_price_text);
            paymentMethod = (itemView).findViewById(R.id.payment_method);
            paymentMethodText = (itemView).findViewById(R.id.payment_method_text);


        }

    }


}
