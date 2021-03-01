package com.example.coffeenest.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.coffeenest.CartActivity;
import com.example.coffeenest.Database.Database;
import com.example.coffeenest.Model.CoffeeOrder;
import com.example.coffeenest.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CoffeeOrder> listData = new ArrayList<>();
    CartActivity cart;
    private OnCartListener mOnCartListener;


    public  CartAdapter(CartActivity cart, List<CoffeeOrder> listData, OnCartListener onCartListener){
        this.listData = listData;
        this.cart = cart;
        this.mOnCartListener = onCartListener;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(cart).inflate(R.layout.cart_item , parent, false);
        return new CartViewHolder(v,mOnCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CoffeeOrder order = listData.get(position);
        holder.txt_cart_name.setText( order.getProductName());
        holder.txt_price.setText(order.getPrice() + " \u20ac");


        //change total price if quantity change from elegant number
        holder.img_cart_count.setNumber(listData.get(position).getQuantity());
        holder.img_cart_count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                CoffeeOrder coffeeOrder = listData.get(position);
                coffeeOrder.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(coffeeOrder);

                //Update total price
                //Calculate total price
                Double double_total;
                double_total = 0.00;
                List <CoffeeOrder> coffeeOrders = new Database(cart).getCarts();

                for(CoffeeOrder item:coffeeOrders) {
                    double_total += (Double.parseDouble(item.getPrice())) * (Double.parseDouble(item.getQuantity()));
                }

                String total = String.format("%.2f", double_total);

                cart.txtTotalPrice.setText(" " + total + " \u20ac");


            }

        });


        //remove item from shopping cart
        holder.itemDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoffeeOrder coffeeOrder = listData.get(holder.getAdapterPosition());
                listData.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                new Database(cart).removeFromCart(coffeeOrder);

                //Update total price
                //Calculate total price

                Double double_total;
                double_total = 0.00;
                List <CoffeeOrder> coffeeOrders = new Database(cart).getCarts();
                for(CoffeeOrder item:coffeeOrders) {
                    double_total += (Double.parseDouble(item.getPrice())) * (Double.parseDouble(item.getQuantity()));
                }

                String total = String.format("%.2f", double_total);

                cart.txtTotalPrice.setText(total + " \u20ac");
            }
        });





    }

    @Override
    public int getItemCount() {

        if (listData.size() == 0){

            //making it semi-transparent

            cart.btnPlace.setAlpha(.5f);
            cart.btnPlace.setEnabled(false);
            cart.empty_list.setVisibility(LinearLayout.VISIBLE);
        }
        return listData.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_cart_name,txt_price;
        public ElegantNumberButton img_cart_count;
        public ImageView itemDeleteBtn;
        OnCartListener onCartListener;



        public CartViewHolder(View itemView, OnCartListener onCartListener){
            super(itemView);
            txt_cart_name = itemView.findViewById(R.id.cart_item_name);
            txt_price = itemView.findViewById(R.id.cart_item_price);
            img_cart_count = itemView.findViewById(R.id.btn_quantity);
            itemDeleteBtn = itemView.findViewById(R.id.btn_item_delete);

            this.onCartListener = onCartListener;

        }

        @Override
        public void onClick(View v) {

            onCartListener.onCartItemDelete(getAdapterPosition());
        }


    }


    public interface OnCartListener{
        void onCartItemDelete(int position);
    }


}
