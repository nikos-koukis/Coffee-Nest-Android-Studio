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

public class TeaDetails extends AppCompatActivity {

    //Init Variables
    TextView teaName, teaPrice, teaDescription;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar teaDetailsToolbar;
    ImageView imageTea;

    String teaNameFromFragment = "Tea Name Not Set";
    String teaDescriptionFromFragment = "Tea Description Not Set";
    String teaPriceFromFragment = "Tea Price Not Set";
    String teaImageFromFragment = "Tea Image Not Set";

    FirebaseDatabase database;
    DatabaseReference teaRef;

    Button buttonAddCart;

    String teaId = "";
    int ID;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_details);

        //Hook Elements From xml File - drink_details.xml
        teaDetailsToolbar = findViewById(R.id.tea_details_toolbar);
        teaName = findViewById(R.id.tea_name);
        teaDescription = findViewById(R.id.tea_description);
        teaPrice = findViewById(R.id.tea_price);
        imageTea = findViewById(R.id.image_tea);
        buttonAddCart = findViewById(R.id.btn_add_cart);

        collapsingToolbarLayout = findViewById(R.id.tea_collapsing);

        setSupportActionBar(teaDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        database = FirebaseDatabase.getInstance();
        teaRef = database.getReference("Tea");

        //Take smoothie_name,smoothie_price,smoothie_image and smoothie_image from Smoothie Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teaNameFromFragment = extras.getString("teaNameFromFragment");
            teaDescriptionFromFragment = extras.getString("teaDescriptionFromFragment");
            teaPriceFromFragment = extras.getString("teaPriceFromFragment");
            teaImageFromFragment = extras.getString("teaImageFromFragment");
        }

        collapsingToolbarLayout.setTitle(teaNameFromFragment);


        teaDetailsToolbar.setTitle(teaNameFromFragment);

        teaDescription.setText(teaDescriptionFromFragment);


        teaName.setText(teaNameFromFragment);
        teaPrice.setText(teaPriceFromFragment + " \u20ac");

        Picasso.get().load(teaImageFromFragment)
                .into(imageTea);

        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new CoffeeOrder(
                        ID,
                        teaId,
                        teaNameFromFragment,
                        "1",
                        teaPriceFromFragment
                ));

                Toast.makeText(getApplication(), "Το προϊόν προστέθηκε στο καλάθι σας!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TeaDetails.this, MainFragment.class));


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

            Intent intent = new Intent(TeaDetails.this, CartActivity.class);
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