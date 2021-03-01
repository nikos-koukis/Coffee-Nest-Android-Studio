package com.example.coffeenest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeenest.Adapter.SmoothieAdapter;
import com.example.coffeenest.Adapter.TeaAdapter;
import com.example.coffeenest.FragmentDetails.SmoothieDetails;
import com.example.coffeenest.FragmentDetails.TeaDetails;
import com.example.coffeenest.Model.Smoothie;
import com.example.coffeenest.Model.Tea;
import com.example.coffeenest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeaFragment extends Fragment implements TeaAdapter.OnTeaListener{

    //Init Variables

    RecyclerView recyclerTea;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference().child("Tea");
    private TeaAdapter adapter;
    private ArrayList<Tea> list;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tea, container, false);

        recyclerTea = view.findViewById(R.id.recycler_tea);
        recyclerTea.setHasFixedSize(true);
        recyclerTea.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new TeaAdapter(view.getContext(), list, this);
        recyclerTea.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Tea tea = dataSnapshot.getValue(Tea.class);
                    list.add(tea);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onTeaClick(int position) {
        Intent teaDetail = new Intent(getActivity(), TeaDetails.class);
        teaDetail.putExtra("teaNameFromFragment", list.get(position).getTeaName());
        teaDetail.putExtra("teaPriceFromFragment", list.get(position).getTeaPrice());
        teaDetail.putExtra("teaDescriptionFromFragment", list.get(position).getTeaDescription());
        teaDetail.putExtra("teaImageFromFragment", list.get(position).getTeaImage());
        startActivity(teaDetail);

    }
}

