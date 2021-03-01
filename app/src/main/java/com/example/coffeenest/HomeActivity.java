package com.example.coffeenest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.coffeenest.Fragments.MainFragment;
import com.example.coffeenest.Listener.NetworkChangeListener;


public class HomeActivity extends AppCompatActivity {

    //Init Variables
    ImageView buttonUserProfile;
    Button buttonOrder;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hook Elements From xml File - activity_home.xml
        buttonUserProfile = findViewById(R.id.btn_user_profile);
        buttonOrder = findViewById(R.id.btn_order);

        //User Profile Button Listener
        //When User Profile Button Pressed, then then the User Profile Activity will start
        buttonUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
            }
        });

        //Order Button Listener
        //When Order Button Pressed, then Main Fragment will started
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity
                startActivity(new Intent(HomeActivity.this, MainFragment.class));
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
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}