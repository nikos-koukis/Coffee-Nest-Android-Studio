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

public class DrinkDetails extends AppCompatActivity {

    //Init Variables
    TextView drinkName, drinkPrice,drinkDescription;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar drinkDetailsToolbar;
    ImageView imageDrink;

    String drinkNameFromFragment = "Drink Name Not Set";
    String drinkDescriptionFromFragment = "Drink Description Not Set";
    String drinkPriceFromFragment = "Drink Price Not Set";
    String drinkImageFromFragment="Drink Image Not Set";

    FirebaseDatabase database;
    DatabaseReference drinkRef;

    Button buttonAddCart;

    String drinkId = "";
    int ID;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);


        //Hook Elements From xml File - drink_details.xml
        drinkDetailsToolbar = findViewById(R.id.drink_details_toolbar);
        drinkName = findViewById(R.id.drink_name);
        drinkDescription = findViewById(R.id.drink_description);
        drinkPrice = findViewById(R.id.drink_price);
        imageDrink = findViewById(R.id.image_drink);
        buttonAddCart = findViewById(R.id.btn_add_cart);

        collapsingToolbarLayout = findViewById(R.id.drink_collapsing);

        setSupportActionBar(drinkDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        database = FirebaseDatabase.getInstance();
        drinkRef = database.getReference("Drink");


        //Take drink_name,drink_price,drink_description and drink_image from Drink Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drinkNameFromFragment = extras.getString("drinkNameFromFragment");
            drinkPriceFromFragment = extras.getString("drinkPriceFromFragment");
            drinkDescriptionFromFragment = extras.getString("drinkDescriptionFromFragment");
            drinkImageFromFragment = extras.getString("drinkImageFromFragment");
        }


        collapsingToolbarLayout.setTitle(drinkNameFromFragment);


        drinkDetailsToolbar.setTitle(drinkNameFromFragment);

        drinkDescription.setText(drinkDescriptionFromFragment);

        drinkName.setText(drinkNameFromFragment);
        drinkPrice.setText(drinkPriceFromFragment + " \u20ac");

        Picasso.get().load(drinkImageFromFragment)
                .into(imageDrink);

        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new CoffeeOrder(
                        ID,
                        drinkId,
                        drinkNameFromFragment,
                        "1",
                        drinkPriceFromFragment
                ));

                Toast.makeText(getApplication(), "Το προϊόν προστέθηκε στο καλάθι σας!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DrinkDetails.this, MainFragment.class));


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

            Intent intent = new Intent(DrinkDetails.this, CartActivity.class);
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