package com.example.movieapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.databinding.FragmentProfileBinding;
import com.example.movieapp.model.User;
import com.example.movieapp.viewmodel.ProfileViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ProfileViewModel mProfileViewModel;
    private FragmentProfileBinding binding;
    private TextView profileName;
    private CircleImageView imageProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileName = view.findViewById(R.id.profile_displayName);
        imageProfile=view.findViewById(R.id.profile_image);
        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        observeUser();
    }

    private void observeUser() {
        mProfileViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Log.d(TAG, "Name: " + user.getName());
                    profileName.setText(user.getName());
                }
            }
        });
    }
}