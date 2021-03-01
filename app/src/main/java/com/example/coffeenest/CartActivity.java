package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coffeenest.Adapter.CartAdapter;
import com.example.coffeenest.Database.Database;
import com.example.coffeenest.Fragments.HomeFragment;
import com.example.coffeenest.Fragments.MainFragment;
import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.CoffeeOrder;
import com.example.coffeenest.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartListener{

    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public TextView txtTotalPrice;
    public Button btnPlace;

    List<CoffeeOrder> listCart = new ArrayList<>();
    CartAdapter adapter;

    public LinearLayout empty_list;

    FirebaseDatabase database;
    DatabaseReference requests;

    private FirebaseUser user;
    private DatabaseReference reference;
    public String userID;


    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        listCart = new Database(this).getCarts();

        adapter = new CartAdapter(this, listCart, this);
        recyclerView.setAdapter(adapter);

        empty_list = findViewById(R.id.empty_list);


        //Calculate total price
        Double double_total;
        double_total = 0.00;
        for (CoffeeOrder order : listCart) {
            double_total += (Double.parseDouble(order.getPrice())) * (Double.parseDouble(order.getQuantity()));

        }

        String total = String.format("%.2f", double_total);

        txtTotalPrice.setText(" " + total + " \u20ac");

        // check if cart list is empty. If cart list is empty, then disable button
        if (adapter.getItemCount() == 0) {

            //making it semi-transparent
            btnPlace.setAlpha(.5f);
            btnPlace.setEnabled(false);
            empty_list.setVisibility(LinearLayout.VISIBLE);
        }

        //Take username of current user for complete order - for username of request
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    userID = userProfile.username;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cartActivityDetails = new Intent(CartActivity.this, PaymentMethodActivity.class);
                cartActivityDetails.putExtra("userNameFromCartActivityDetails", userID);
                cartActivityDetails.putExtra("totalPriceFromCartActivityDetails", txtTotalPrice.getText().toString());
                startActivity(cartActivityDetails);

            }
        });


    }


    @Override
    public void onCartItemDelete(int position) {

    }

    //Check For Internet Connection
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    //When user click back android button
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CartActivity.this, MainFragment.class));
    }


}