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

import com.example.coffeenest.Adapter.CoffeeAdapter;
import com.example.coffeenest.FragmentDetails.CoffeeDetails;
import com.example.coffeenest.Model.Coffee;
import com.example.coffeenest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CoffeeFragment extends Fragment implements CoffeeAdapter.OnCoffeeListener{

    //Init Variables

    RecyclerView recyclerCoffee;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference().child("Coffee");
    private CoffeeAdapter adapter;
    private ArrayList<Coffee> list;


    public CoffeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coffee, container, false);

        recyclerCoffee = view.findViewById(R.id.recycler_coffee);
        recyclerCoffee.setHasFixedSize(true);
        recyclerCoffee.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new CoffeeAdapter(view.getContext(), list, this);
        recyclerCoffee.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Coffee coffee = dataSnapshot.getValue(Coffee.class);
                    list.add(coffee);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


    //Move values of coffee fragment in coffeeDetails Activity
    @Override
    public void onCoffeeClick(int position) {
        Intent coffeeDetail = new Intent(getActivity(), CoffeeDetails.class);
        coffeeDetail.putExtra("coffeeNameFromFragment", list.get(position).getName());
        coffeeDetail.putExtra("coffeePriceFromFragment", list.get(position).getPrice());
        coffeeDetail.putExtra("coffeeImageFromFragment", list.get(position).getImage());
        startActivity(coffeeDetail);

    }
}