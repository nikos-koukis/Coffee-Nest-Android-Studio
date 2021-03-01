package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeenest.Adapter.DrinkAdapter;
import com.example.coffeenest.Adapter.UserOrderAdapter;
import com.example.coffeenest.Dialogs.DeleteUserOrderHistoryDialog;
import com.example.coffeenest.Dialogs.GpsDialog;
import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.CoffeeOrder;
import com.example.coffeenest.Model.Drink;
import com.example.coffeenest.Model.Request;
import com.example.coffeenest.Model.User;
import com.example.coffeenest.Model.UserOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class UserOrderActivity extends AppCompatActivity {

    RecyclerView recyclerOrders;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    private UserOrderAdapter adapter;
    private ArrayList<UserOrder> list;

    ImageButton btnBackUserOrder;

    TextView userOrderBlankText;

    GifImageView gifImageView;

    Button btnDeleteUserOrderItems;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        gifImageView = findViewById(R.id.order_giffy);
        userOrderBlankText = findViewById(R.id.user_order_blank_text);

        btnDeleteUserOrderItems = findViewById(R.id.btn_delete_user_order_items);

        btnBackUserOrder = findViewById(R.id.btn_back_user_order);

        ref = FirebaseDatabase.getInstance().getReference().child("UserOrder");

        recyclerOrders = findViewById(R.id.recycler_orders);
        recyclerOrders.setHasFixedSize(true);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new UserOrderAdapter(UserOrderActivity.this, list);
        recyclerOrders.setAdapter(adapter);



        btnDeleteUserOrderItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteUserOrderHistoryDialog();
            }
        });


        btnBackUserOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserOrderActivity.this, UserProfileActivity.class));
            }
        });

        ref.orderByChild("userOrderDateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserOrder userOrder = dataSnapshot.getValue(UserOrder.class);
                    list.add(userOrder);
                }

                adapter.notifyDataSetChanged();

                if (list.size() == 0) {
                    gifImageView.setVisibility(GifImageView.VISIBLE);
                    userOrderBlankText.setVisibility(View.VISIBLE);
                    btnDeleteUserOrderItems.setVisibility(Button.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void DeleteUserOrderHistoryDialog() {
        DeleteUserOrderHistoryDialog deleteUserOrderHistoryDialog = new DeleteUserOrderHistoryDialog();
        deleteUserOrderHistoryDialog.show(getSupportFragmentManager(), "History Dialog");
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


    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserOrderActivity.this, UserProfileActivity.class));
    }
}