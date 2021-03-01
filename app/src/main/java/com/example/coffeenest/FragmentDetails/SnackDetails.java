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

public class SnackDetails extends AppCompatActivity {

    //Init Variables
    TextView snackName, snackPrice, snackDescription;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar snackDetailsToolbar;
    ImageView imageSnack;

    String snackNameFromFragment = "Snack Name Not Set";
    String snackDescriptionFromFragment = "Snack Description Not Set";
    String snackPriceFromFragment = "Snack Price Not Set";
    String snackImageFromFragment = "Snack Image Not Set";

    FirebaseDatabase database;
    DatabaseReference snackRef;

    Button buttonAddCart;

    String snackId = "";
    int ID;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_details);

        //Hook Elements From xml File - drink_details.xml
        snackDetailsToolbar = findViewById(R.id.snack_details_toolbar);
        snackName = findViewById(R.id.snack_name);
        snackDescription = findViewById(R.id.snack_description);
        snackPrice = findViewById(R.id.snack_price);
        imageSnack = findViewById(R.id.image_snack);
        buttonAddCart = findViewById(R.id.btn_add_cart);

        collapsingToolbarLayout = findViewById(R.id.snack_collapsing);

        setSupportActionBar(snackDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        database = FirebaseDatabase.getInstance();
        snackRef = database.getReference("Snack");

        //Take smoothie_name,smoothie_price,smoothie_image and smoothie_image from Smoothie Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            snackNameFromFragment = extras.getString("snackNameFromFragment");
            snackDescriptionFromFragment = extras.getString("snackDescriptionFromFragment");
            snackPriceFromFragment = extras.getString("snackPriceFromFragment");
            snackImageFromFragment = extras.getString("snackImageFromFragment");
        }

        collapsingToolbarLayout.setTitle(snackNameFromFragment);


        snackDetailsToolbar.setTitle(snackNameFromFragment);

        snackDescription.setText(snackDescriptionFromFragment);


        snackName.setText(snackNameFromFragment);
        snackPrice.setText(snackPriceFromFragment + " \u20ac");

        Picasso.get().load(snackImageFromFragment)
                .into(imageSnack);


        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new CoffeeOrder(
                        ID,
                        snackId,
                        snackNameFromFragment,
                        "1",
                        snackPriceFromFragment
                ));

                Toast.makeText(getApplication(), "Το προϊόν προστέθηκε στο καλάθι σας!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SnackDetails.this, MainFragment.class));


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

            Intent intent = new Intent(SnackDetails.this, CartActivity.class);
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