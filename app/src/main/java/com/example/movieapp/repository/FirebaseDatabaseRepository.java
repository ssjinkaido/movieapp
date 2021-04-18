package com.example.movieapp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.model.Favourite;
import com.example.movieapp.model.User;
import com.example.movieapp.utils.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseRepository {

    /**
     * Get user details
     * @param userUid the user firebase token id
     * @return the live data of user (to be observed).
     */
    public LiveData<User> getUserDetails(String userUid) {
        final MutableLiveData<User> user = new MutableLiveData<>();

        Util.getDatabaseReference("Users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.setValue(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                user.setValue(null);
            }
        });

        return user;
    }

}
