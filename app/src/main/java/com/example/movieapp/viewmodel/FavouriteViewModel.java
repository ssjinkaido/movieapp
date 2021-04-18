package com.example.movieapp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.Favourite;
import com.example.movieapp.repository.FirebaseDatabaseRepository;
import com.example.movieapp.utils.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteViewModel extends ViewModel implements ValueEventListener{
    private MutableLiveData<List<Favourite>> favouriteMoviesList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbReference;
    @ViewModelInject
    public FavouriteViewModel() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        dbReference=firebaseDatabase.getReference().child("Likes").child(Util.getUserUid());
        dbReference.addValueEventListener(this);
        favouriteMoviesList=new MutableLiveData<List<Favourite>>();
    }

    public LiveData<List<Favourite>> getAllFavourites(){
        return favouriteMoviesList;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<Favourite> list = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Favourite favourite=snapshot.getValue(Favourite.class);
            list.add(favourite);
            Log.d("Favourite", "test:" + snapshot.getValue(Favourite.class));
        }
        favouriteMoviesList.postValue(list);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
