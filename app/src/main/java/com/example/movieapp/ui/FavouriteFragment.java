package com.example.movieapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.adapter.FavouriteAdapter;
import com.example.movieapp.databinding.FragmentFavouriteBinding;
import com.example.movieapp.model.Favourite;
import com.example.movieapp.utils.Util;
import com.example.movieapp.viewmodel.FavouriteViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteFragment extends Fragment {
    private FragmentFavouriteBinding binding;
    private FavouriteAdapter favouriteAdapter;
    private ArrayList<Favourite> favouriteList;
    private FavouriteViewModel favouriteViewModel;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return (view);
    }

    @Override
    public void onStart() {
        super.onStart();
        observeData();
//        readFavouriteList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteList = new ArrayList<>();
        favouriteViewModel=new ViewModelProvider(FavouriteFragment.this).get(FavouriteViewModel.class);
        initRecyclerView();
        binding.clearFavList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Likes").child(Util.getUserUid()).removeValue();
                Toast.makeText(getContext(), "Delete from favourite successfully", Toast.LENGTH_SHORT).show();
                favouriteList.clear();
                favouriteAdapter.setFavouriteList(favouriteList);
                favouriteAdapter.notifyDataSetChanged();
            }
        });
    }

    private void observeData(){
        favouriteViewModel.getAllFavourites().observe(getViewLifecycleOwner(), new Observer<List<Favourite>>() {
            @Override
            public void onChanged(List<Favourite> favourites) {
                favouriteAdapter.setFavouriteList((ArrayList<Favourite>) favourites);
                favouriteAdapter.notifyDataSetChanged();
            }
        });
    }

//    private void readFavouriteList() {
//        FirebaseDatabase.getInstance().getReference().child("Likes").child(Util.getUserUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    favouriteList.add(snapshot.getValue(Favourite.class));
//                    Log.d("Favourite", "test:" + snapshot.getValue(Favourite.class));
//
//                }
//                favouriteAdapter.setFavouriteList(favouriteList);
//                favouriteAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void initRecyclerView() {
        binding.favListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
        binding.favListRecyclerView.setAdapter(favouriteAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}