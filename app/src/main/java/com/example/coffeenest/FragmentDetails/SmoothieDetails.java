package com.example.coffeenest.FragmentDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeenest.CartActivity;
import com.example.coffeenest.Database.Database;
import com.example.coffeenest.Fragments.MainFragment;
import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.CoffeeOrder;
import com.example.coffeenest.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SmoothieDetails extends AppCompatActivity {

    //Init Variables
    TextView smoothieName, smoothiePrice, smoothieDescription;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar smoothieDetailsToolbar;
    ImageView imageSmoothie;

    String smoothieNameFromFragment = "Smoothie Name Not Set";
    String smoothieDescriptionFromFragment = "Smoothie Description Not Set";
    String smoothiePriceFromFragment = "Smoothie Price Not Set";
    String smoothieImageFromFragment = "Smoothie Image Not Set";

    FirebaseDatabase database;
    DatabaseReference smoothieRef;

    Button buttonAddCart;

    String smoothieId = "";
    int ID;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoothie_details);

        //Hook Elements From xml File - drink_details.xml
        smoothieDetailsToolbar = findViewById(R.id.smoothie_details_toolbar);
        smoothieName = findViewById(R.id.smoothie_name);
        smoothieDescription = findViewById(R.id.smoothie_description);
        smoothiePrice = findViewById(R.id.smoothie_price);
        imageSmoothie = findViewById(R.id.image_smoothie);
        buttonAddCart = findViewById(R.id.btn_add_cart);

        collapsingToolbarLayout = findViewById(R.id.smoothie_collapsing);

        setSupportActionBar(smoothieDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        database = FirebaseDatabase.getInstance();
        smoothieRef = database.getReference("Smoothie");

        //Take smoothie_name,smoothie_price,smoothie_image and smoothie_image from Smoothie Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            smoothieNameFromFragment = extras.getString("smoothieNameFromFragment");
            smoothieDescriptionFromFragment = extras.getString("smoothieDescriptionFromFragment");
            smoothiePriceFromFragment = extras.getString("smoothiePriceFromFragment");
            smoothieImageFromFragment = extras.getString("smoothieImageFromFragment");
        }

        collapsingToolbarLayout.setTitle(smoothieNameFromFragment);


        smoothieDetailsToolbar.setTitle(smoothieNameFromFragment);

        smoothieDescription.setText(smoothieDescriptionFromFragment);


        smoothieName.setText(smoothieNameFromFragment);
        smoothiePrice.setText(smoothiePriceFromFragment + " \u20ac");

        Picasso.get().load(smoothieImageFromFragment)
                .into(imageSmoothie);


        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new CoffeeOrder(
                        ID,
                        smoothieId,
                        smoothieNameFromFragment,
                        "1",
                        smoothiePriceFromFragment
                ));

                Toast.makeText(getApplication(), "Το προϊόν προστέθηκε στο καλάθι σας!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SmoothieDetails.this, MainFragment.class));


            }
        });

    }

    // Add shopping cart icon in toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //When cart icon pressed, then Cart Activity will started
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.cart) {

            Intent intent = new Intent(SmoothieDetails.this, CartActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
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
}