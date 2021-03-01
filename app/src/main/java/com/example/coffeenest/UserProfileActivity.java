package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class UserProfileActivity extends AppCompatActivity {

    //Init variables
    ImageButton userProfileBackButton;
    TextView userNameProfText;
    Button buttonMyAccount, btnUserLocations, btnUserOrders;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Hook Elements From xml File - activity_user_profile.xml
        userProfileBackButton = findViewById(R.id.user_profile_back_button);
        userNameProfText = findViewById(R.id.username_prof_text);
        buttonMyAccount = findViewById(R.id.btn_my_account);
        btnUserLocations = findViewById(R.id.btn_user_locations);
        btnUserOrders = findViewById(R.id.btn_user_orders);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        btnUserOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, UserOrderActivity.class));
            }
        });


        //User Profile Back Button Listener
        //When User Profile Back Button pressed, then start HomeActivity
        userProfileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
            }
        });


        //Replace the Text of TextView userProfileBackButton with the username of user after Log In
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String username = userProfile.username;
                    userNameProfText.setText("Γεια σου, " + username);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Button My Account Listener
        //When My Account Back Button pressed, then start My Account Activity
        buttonMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start My Account Activity
                startActivity(new Intent(UserProfileActivity.this, MyAccountActivity.class));
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

    //When user click back android button
    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
    }
}