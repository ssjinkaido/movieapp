package com.example.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.User;
import com.example.movieapp.repository.FirebaseDatabaseRepository;
import com.example.movieapp.utils.Util;

public class ProfileViewModel extends ViewModel {
    private FirebaseDatabaseRepository mFirebaseDatabaseRepository = new FirebaseDatabaseRepository();

    private LiveData<User> user;

    public ProfileViewModel() {
        user = mFirebaseDatabaseRepository.getUserDetails(Util.getUserUid());
    }

    /**
     * Get the live data of user to be observed
     *
     * @return live data of the user.
     */
    public LiveData<User> getUser() {
        return user;
    }
}
