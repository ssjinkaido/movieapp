package com.example.movieapp.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.repository.FirebaseAuthenticationRepository;
import com.example.movieapp.utils.Validator;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> mIsSuccessful;
    private FirebaseAuthenticationRepository mFirebaseAuthenticationRepository = new FirebaseAuthenticationRepository();
    //should be declared final because bindings only detect changes in the field's value, not of the field itself.
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    public final ObservableField<String> errorEmail = new ObservableField<>();
    public final ObservableField<String> errorPassword = new ObservableField<>();

    public LoginViewModel() {
        mIsSuccessful = new MutableLiveData<>();
    }

    /**
     * When the login button is clicked. Call the repository to call the login button.
     */
    public void onLoginClick() {
        if (validateInputs()) {
            mFirebaseAuthenticationRepository.login(email.get(), password.get(), mIsSuccessful);
        } else {
            mIsSuccessful.postValue(false);
        }
    }

    /**
     * Input validation for login handler.
     *
     * @return
     */
    private boolean validateInputs() {
        boolean isValid = true;

        if (email.get() == null || !Validator.isEmailValid(email.get())) {

            errorEmail.set("Invalid Email");
            isValid = false;

        } else {
            errorEmail.set(null);
        }

        if (password.get() == null || password.get().length() < 4) {
            errorPassword.set("Password too short");

            isValid = false;

        } else {
            errorPassword.set(null);
        }

        return isValid;
    }

    /**
     * Check the user's session in the application
     *
     * @return true or false
     */
    public boolean getIsLoggedIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    /**
     * Check whether the logic ins successful.
     *
     * @return the live data of successfullness
     */
    public LiveData<Boolean> getIsSuccessful() {
        return mIsSuccessful;
    }
}
