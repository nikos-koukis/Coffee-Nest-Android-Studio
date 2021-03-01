package com.example.coffeenest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coffeenest.Fragments.MainFragment;
import com.example.coffeenest.Listener.NetworkChangeListener;

public class OrderConfirmationActivity extends AppCompatActivity {

    //Init Variables
    Button buttonContinueShopping;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        //Hook Elements From xml File - activity_order_confirmation.xml

        buttonContinueShopping = findViewById(R.id.btn_continue_shopping);

        //Button Continue Shopping Listener
        //When this button pressed, Main Fragment will started
        buttonContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Fragment
                startActivity(new Intent(OrderConfirmationActivity.this, MainFragment.class));
            }
        });

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


    //Disable Back Button
    //To do this we must comment the "super.onBackPressed();"
    @Override
    public void onBackPressed() {
         //super.onBackPressed();
    }
}