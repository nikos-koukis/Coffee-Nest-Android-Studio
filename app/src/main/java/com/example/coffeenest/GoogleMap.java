package com.example.coffeenest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.coffeenest.Fragments.MainFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMap extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_map);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LatLng bd = new LatLng(38.219033, 21.745033);
        googleMap.addMarker(new MarkerOptions().position(bd).title("Coffee Nest").snippet("Πάτρα 263 34"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bd, 17));

    }

    //When user click back android button
    @Override
    public void onBackPressed() {
        startActivity(new Intent(GoogleMap.this, MainFragment.class));
    }
}