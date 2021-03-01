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

import com.example.coffeenest.Adapter.CoffeeAdapter;
import com.example.coffeenest.Adapter.DrinkAdapter;
import com.example.coffeenest.FragmentDetails.CoffeeDetails;
import com.example.coffeenest.FragmentDetails.DrinkDetails;
import com.example.coffeenest.Model.Coffee;
import com.example.coffeenest.Model.Drink;
import com.example.coffeenest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DrinksFragment extends Fragment implements DrinkAdapter.OnDrinkListener{

    //Init Variables

    RecyclerView recyclerDrinks;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference().child("Drink");
    private DrinkAdapter adapter;
    private ArrayList<Drink> list;


    public DrinksFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);

        recyclerDrinks = view.findViewById(R.id.recycler_drinks);
        recyclerDrinks.setHasFixedSize(true);
        recyclerDrinks.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new DrinkAdapter(view.getContext(), list, this);
        recyclerDrinks.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Drink drink = dataSnapshot.getValue(Drink.class);
                    list.add(drink);
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
    public void onDrinkClick(int position) {
        Intent drinkDetail = new Intent(getActivity(), DrinkDetails.class);
        drinkDetail.putExtra("drinkNameFromFragment", list.get(position).getDrinkName());
        drinkDetail.putExtra("drinkPriceFromFragment", list.get(position).getDrinkPrice());
        drinkDetail.putExtra("drinkDescriptionFromFragment", list.get(position).getDrinkDescription());
        drinkDetail.putExtra("drinkImageFromFragment", list.get(position).getDrinkImage());
        startActivity(drinkDetail);
    }


}