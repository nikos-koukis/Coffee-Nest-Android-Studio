package com.example.coffeenest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coffeenest.Adapter.DrinkAdapter;
import com.example.coffeenest.Adapter.SmoothieAdapter;
import com.example.coffeenest.FragmentDetails.DrinkDetails;
import com.example.coffeenest.FragmentDetails.SmoothieDetails;
import com.example.coffeenest.Model.Drink;
import com.example.coffeenest.Model.Smoothie;
import com.example.coffeenest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SmoothieFragment extends Fragment implements SmoothieAdapter.OnSmoothieListener{

    //Init Variables

    RecyclerView recyclerSmoothie;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference().child("Smoothie");
    private SmoothieAdapter adapter;
    private ArrayList<Smoothie> list;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smoothie, container, false);

        recyclerSmoothie = view.findViewById(R.id.recycler_smoothies);
        recyclerSmoothie.setHasFixedSize(true);
        recyclerSmoothie.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new SmoothieAdapter(view.getContext(), list, this);
        recyclerSmoothie.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Smoothie smoothie = dataSnapshot.getValue(Smoothie.class);
                    list.add(smoothie);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    //Move values of smoothie fragment in smoothieDetails Activity
    @Override
    public void onSmoothieClick(int position) {
        Intent smoothieDetail = new Intent(getActivity(), SmoothieDetails.class);
        smoothieDetail.putExtra("smoothieNameFromFragment", list.get(position).getSmoothieName());
        smoothieDetail.putExtra("smoothiePriceFromFragment", list.get(position).getSmoothiePrice());
        smoothieDetail.putExtra("smoothieDescriptionFromFragment", list.get(position).getSmoothieDescription());
        smoothieDetail.putExtra("smoothieImageFromFragment", list.get(position).getSmoothieImage());
        startActivity(smoothieDetail);

    }
}