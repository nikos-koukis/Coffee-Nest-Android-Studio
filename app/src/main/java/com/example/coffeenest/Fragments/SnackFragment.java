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

import com.example.coffeenest.Adapter.SnackAdapter;
import com.example.coffeenest.Adapter.TeaAdapter;
import com.example.coffeenest.FragmentDetails.SnackDetails;
import com.example.coffeenest.FragmentDetails.TeaDetails;
import com.example.coffeenest.Model.Snack;
import com.example.coffeenest.Model.Tea;
import com.example.coffeenest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SnackFragment extends Fragment implements SnackAdapter.OnSnackListener{

    //Init Variables

    RecyclerView recyclerSnack;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference().child("Snack");
    private SnackAdapter adapter;
    private ArrayList<Snack> list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_snack, container, false);

        recyclerSnack = view.findViewById(R.id.recycler_snack);
        recyclerSnack.setHasFixedSize(true);
        recyclerSnack.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new SnackAdapter(view.getContext(), list, this);
        recyclerSnack.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Snack snack = dataSnapshot.getValue(Snack.class);
                    list.add(snack);
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
    public void onSnackClick(int position) {
        Intent snackDetail = new Intent(getActivity(), SnackDetails.class);
        snackDetail.putExtra("snackNameFromFragment", list.get(position).getSnackName());
        snackDetail.putExtra("snackPriceFromFragment", list.get(position).getSnackPrice());
        snackDetail.putExtra("snackDescriptionFromFragment", list.get(position).getSnackDescription());
        snackDetail.putExtra("snackImageFromFragment", list.get(position).getSnackImage());
        startActivity(snackDetail);

    }
}