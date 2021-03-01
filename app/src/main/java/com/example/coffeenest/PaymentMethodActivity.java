package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.coffeenest.Database.Database;
import com.example.coffeenest.Dialogs.GpsDialog;
import com.example.coffeenest.Fragments.HomeFragment;
import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.CoffeeOrder;
import com.example.coffeenest.Model.Request;
import com.example.coffeenest.Model.User;
import com.example.coffeenest.Model.UserOrder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PaymentMethodActivity extends AppCompatActivity {

    //Init Variables

    public static final String DATE_FORMAT_1 = "dd/MM/yy - HH:mm:ss";

    Toolbar toolbar;
    EditText addressInputText;
    EditText usernameInputText;
    EditText totalPriceInputText;
    Button btnCompleteOrder, btnFindLocation;

    TextInputLayout layoutAddress;

    String userNameFromCartActivityDetails = "Anonymous";
    String totalPriceFromCartActivityDetails = "0.00";

    RadioGroup radioGroupChoosePaymentMethod;
    RadioButton btnMethodPayment,radioButtonCredit,radioButtonPayPal;

    private ProgressDialog progressDialog;

    FirebaseDatabase database;
    DatabaseReference requests;
    DatabaseReference user_order;
    DatabaseReference ref_address;
    DatabaseReference ref_user;

    private FirebaseUser user;
    public String userID;

    List<CoffeeOrder> listCart = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        //Hook Elements From xml File - coffee_details.xml


        toolbar = findViewById(R.id.toolbar);
        addressInputText = findViewById(R.id.address_input_text);
        usernameInputText = findViewById(R.id.username_input_text);
        totalPriceInputText = findViewById(R.id.total_price_input_text);
        btnCompleteOrder = findViewById(R.id.btn_complete_order);
        btnFindLocation = findViewById(R.id.btn_find_location);
        layoutAddress = findViewById(R.id.layout_address);
        radioGroupChoosePaymentMethod = findViewById(R.id.radioGroup_choose_payment_method);
        radioButtonCredit = findViewById(R.id.radio_button_credit);
        radioButtonPayPal = findViewById(R.id.radio_button_payPal);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        user_order = database.getReference("UserOrder");

        ref_address = database.getReference("UserAddress").push();

        ref_user = FirebaseDatabase.getInstance().getReference("Users");

        listCart = new Database(this).getCarts();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PaymentMethodActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userNameFromCartActivityDetails = extras.getString("userNameFromCartActivityDetails");
            totalPriceFromCartActivityDetails = extras.getString("totalPriceFromCartActivityDetails");
        }

        usernameInputText.setText(userNameFromCartActivityDetails);
        usernameInputText.setEnabled(false);

        totalPriceInputText.setText(totalPriceFromCartActivityDetails);
        totalPriceInputText.setEnabled(false);

        //Take username of current user save address - for username of userLocation
        ref_user.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
         

        btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_1);
                String date = simpleDateFormat.format(new Date());

                String address = addressInputText.getText().toString().trim();

                String currentDateTimeString = String.valueOf(date);

                int isSelected1 = radioGroupChoosePaymentMethod.getCheckedRadioButtonId();

                btnMethodPayment = findViewById(isSelected1);

                if (address.isEmpty() && isSelected1 == -1) {
                    layoutAddress.setErrorEnabled(true);
                    layoutAddress.setError("Το πεδίο Διεύθυνση είναι υποχρεωτικό!");
                    layoutAddress.requestFocus();
                    Toast.makeText(PaymentMethodActivity.this, "Πρέπει να συμπληρώσετε την διεύθυνσή σας και να εισάγεται και τρόπο πληρωμής", Toast.LENGTH_SHORT).show();
                } else if (!address.isEmpty() && isSelected1 == -1) {
                    Toast.makeText(PaymentMethodActivity.this, "Πρέπει να εισάγεται και τρόπο πληρωμής", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty() && isSelected1 != -1) {
                    layoutAddress.setErrorEnabled(true);
                    layoutAddress.setError("Το πεδίο Διεύθυνση είναι υποχρεωτικό!");
                    layoutAddress.requestFocus();
                } else {

                    Request request = new Request(
                            userNameFromCartActivityDetails,
                            address,
                            totalPriceFromCartActivityDetails,
                            currentDateTimeString,
                            btnMethodPayment.getText().toString(),
                            listCart
                    );


                    UserOrder userOrder = new UserOrder(
                            userNameFromCartActivityDetails,
                            address,
                            currentDateTimeString,
                            btnMethodPayment.getText().toString(),
                            totalPriceFromCartActivityDetails
                    );


                    //Submit to Firebase
                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);

                    user_order.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(userOrder);

                    //Create Notification
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(PaymentMethodActivity.this,"My Notification");
                    builder.setContentTitle("Η παραγγελία σας ολοκληρώθηκε");
                    builder.setContentText("Πατήστε για να δείτε το ιστορικό παραγγελιών σας");
                    builder.setSmallIcon(R.drawable.ic_check);
                    builder.setAutoCancel(true);

                    Intent intent = new Intent(PaymentMethodActivity.this, UserOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(PaymentMethodActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(PaymentMethodActivity.this);
                    managerCompat.notify(1,builder.build());


                    //Delete Cart
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(getApplication(), "Σας ευχαριστούμε για την παραγγελία σας!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentMethodActivity.this, OrderConfirmationActivity.class));
                    finish();
                }
            }
        });


        //If an input text has error, when user type inside the edit_text, then the error disable

        addressInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutAddress.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Permission
                if (ActivityCompat.checkSelfPermission(PaymentMethodActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(PaymentMethodActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //When permission granted
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageGps();

                    }else{
                        getCurrentLocation();
                    }
                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(PaymentMethodActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Check condition
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //When permission granted
            //Call method
            getCurrentLocation();

        } else {
            //When permission are denied
            //Display toast
            Toast.makeText(this, "Η άδεια απορρίφθηκε", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildAlertMessageGps() {
        GpsDialog gpsDialog = new GpsDialog();
        gpsDialog.show(getSupportFragmentManager(),"GPS DIALOG");
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Init location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check condition

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //When location service is enabled
            //Get last location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    //Init location
                    Location location = task.getResult();

                    //Check condition
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(PaymentMethodActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            addressInputText.setText(String.valueOf(addresses.get(0).getAddressLine(0)));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //When location result is null
                        //Init location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //Init location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Init location
                                Location location1 = locationResult.getLastLocation();
                            }
                        };

                        //Request location updates
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest
                                , locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //When Location service is not enabled
            //Open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

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
        startActivity(new Intent(PaymentMethodActivity.this, CartActivity.class));
    }


}