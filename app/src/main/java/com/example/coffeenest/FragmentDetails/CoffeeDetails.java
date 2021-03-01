package com.example.coffeenest.FragmentDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class CoffeeDetails extends AppCompatActivity {

    //Init Variables
    TextView coffeeName,coffeePrice;
    ImageView coffeeImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    String coffeeNameFromFragment="Coffee Name Not Set";
    String coffeePriceFromFragment="Coffee Price Not Set";
    String coffeeImageFromFragment="Coffee Image Not Set";

    Button buttonAddCart;

    FirebaseDatabase database;
    DatabaseReference coffeeRef;

    String coffeeId = "";
    int ID;

    RadioGroup radioGroupChooseVarieties;
    RadioGroup radioGroupChooseSugar;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_details);

        //Hook Elements From xml File - coffee_details.xml

        toolbar = findViewById(R.id.toolbar);
        coffeeName = findViewById(R.id.coffee_name);
        coffeePrice = findViewById(R.id.coffee_price);
        coffeeImage = findViewById(R.id.img_coffee);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        buttonAddCart = findViewById(R.id.btn_add_cart);
        radioGroupChooseVarieties = findViewById(R.id.radioGroup_choose_varieties);
        radioGroupChooseSugar = findViewById(R.id.radioGroup_choose_sugar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        database = FirebaseDatabase.getInstance();
        coffeeRef = database.getReference("Coffee");

        //Take coffee_name,coffee_price & coffee_image from Coffee Fragment
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            coffeeNameFromFragment = extras.getString("coffeeNameFromFragment");
            coffeePriceFromFragment = extras.getString("coffeePriceFromFragment");
            coffeeImageFromFragment = extras.getString("coffeeImageFromFragment");
        }

        coffeeName.setText(coffeeNameFromFragment);
        collapsingToolbarLayout.setTitle(coffeeNameFromFragment);

        coffeePrice.setText(coffeePriceFromFragment + " \u20ac");
        Picasso.get().load(coffeeImageFromFragment)
                .into(coffeeImage);


        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int isSelected1 = radioGroupChooseVarieties.getCheckedRadioButtonId();
                int isSelected2 = radioGroupChooseSugar.getCheckedRadioButtonId();

                if(isSelected1 == -1 && isSelected2 == -1){
                    Toast.makeText(CoffeeDetails.this, "Πρέπει να επιλέξετε ποικιλία και ζάχαρη για να συνεχίσετε...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(isSelected1 != -1 && isSelected2 == -1){
                    Toast.makeText(CoffeeDetails.this, "Πρέπει να επιλέξετε ζάχαρη για να συνεχίσετε...", Toast.LENGTH_SHORT).show();
                }else if(isSelected1 == -1 && isSelected2 != -1){
                    Toast.makeText(CoffeeDetails.this, "Πρέπει να επιλέξετε ποικιλία για να συνεχίσετε...", Toast.LENGTH_SHORT).show();
                }else {
                    new Database(getBaseContext()).addToCart(new CoffeeOrder(
                            ID,
                            coffeeId,
                            coffeeNameFromFragment,
                            "1",
                            coffeePriceFromFragment
                    ));


                    Toast.makeText(getApplication(), "Το προϊόν προστέθηκε στο καλάθι σας!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CoffeeDetails.this, MainFragment.class));

                }



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


    // Add shopping cart icon in toolbar
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //When cart icon pressed, then Cart Activity will started
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();


        if(id == R.id.cart){

            Intent intent = new Intent(CoffeeDetails.this, CartActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}