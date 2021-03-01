package com.example.coffeenest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeenest.Listener.NetworkChangeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LaunchActivity extends AppCompatActivity {

    //Init Variables
    TextView calendarText;
    TextView appNameText;

    Button registerButton;
    Button loginButton;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //Hook Elements From xml File - activity_launch.xml
        calendarText = findViewById(R.id.calendar_text);
        appNameText = findViewById(R.id.app_name_text);
        registerButton = findViewById(R.id.register_btn);
        loginButton = findViewById(R.id.login_btn);


        //Take current date and show it in Calendar Text View

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        calendarText.setText(date);

        // Custom Font For App Name Text in Launch Activity

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Texturina-Italic-VariableFont_opsz,wght.ttf");
        appNameText.setTypeface(customFont);

        //Register Button Listener
        //When Register Button Pressed, then the Register Activity will start
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchActivity.this, RegisterActivity.class));
            }
        });

        //Login Button Listener
        //When Login Button Pressed, then the Login Activity will start
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
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
    //Alert Dialog "exit_app_dialog" will appeared
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //moveTaskToBack(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.exit_app_dialog, null);

        builder.setView(view)
                .setNegativeButton("Όχι", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                    }
                });

        AlertDialog alertDialog  = builder.create();
        alertDialog .show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setAllCaps(false);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setAllCaps(false);
        alertDialog .show();

    }

}