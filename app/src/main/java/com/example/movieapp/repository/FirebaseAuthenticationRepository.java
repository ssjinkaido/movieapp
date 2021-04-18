package com.example.movieapp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.model.User;
import com.example.movieapp.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FirebaseAuthenticationRepository {


    /**
     * Login Firebase auth utility
     *
     * @param email        user's email
     * @param password     user's password
     * @param isSuccessful live data of whether it is successful or not.
     */
    public void login(String email, String password, final MutableLiveData<Boolean> isSuccessful) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            isSuccessful.postValue(false);
                        } else {
                            isSuccessful.postValue(true);
                        }
                    }
                });
    }

    /**
     * Register Firebase auth utility
     *
     * @param username     user's username
     * @param email        user's email
     * @param password     user's password
     * @param isSuccessful keep track of successfullness.
     */
    public void register(final String username, final String email, String password, final MutableLiveData<Boolean> isSuccessful) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            isSuccessful.postValue(false);
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //get Users branch.
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", username);
                            userMap.put("email", email);
                            userMap.put("image", "default");
                            final DatabaseReference ref = database.getReference("Users");
                            ref.child(Util.getUserUid()).setValue(userMap);
                            isSuccessful.postValue(true);
                        }
                    }


                });
    }

    /**
     * Forget password firebase auth utility
     *
     * @param email        user's email
     * @param isSuccessful keep track of successfulness.
     */
    public void forgetPassword(String email, final MutableLiveData<Boolean> isSuccessful) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            isSuccessful.postValue(false);
                        } else {
                            isSuccessful.postValue(true);
                        }
                    }
                });
    }


}

