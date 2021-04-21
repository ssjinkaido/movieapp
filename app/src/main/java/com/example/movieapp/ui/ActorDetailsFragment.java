package com.example.movieapp.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieapp.adapter.KnownForMoviesAdapter;
import com.example.movieapp.databinding.FragmentActorDetailsBinding;
import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.HomeViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActorDetailsFragment extends Fragment {
    private static final String TAG = "ActorDetails";
    private FragmentActorDetailsBinding binding;
    private Integer personID;
    private HashMap<String, String> queries;
    private KnownForMoviesAdapter adapter;
    private ArrayList<Movie> popularMovies;
    private Actor mActor;

    public ActorDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActorDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel viewModel = new ViewModelProvider(ActorDetailsFragment.this).get(HomeViewModel.class);
        ActorDetailsFragmentArgs args = ActorDetailsFragmentArgs.fromBundle(getArguments());
        personID = args.getPersonId();

        queries = new HashMap<>();
        queries.put("api_key", Constants.API_KEY);
        queries.put("append_to_response", "movie_credits");

        viewModel.getActorDetails(personID, queries);
        viewModel.getActor().observe(getViewLifecycleOwner(), new Observer<Actor>() {
            @Override
            public void onChanged(Actor actor) {
                mActor = actor;
                binding.actorName.setText(actor.getName());
                binding.actorBirthday.setText(actor.getBirthday());
                binding.actorBio.setText(actor.getBiography());
                binding.actorPlace.setText(actor.getPlace_of_birth());
                Glide.with(getContext()).load(Constants.ImageBaseURL + actor.getProfile_path())
                        .into(binding.actorImage);
                binding.actorPopularity.setText(actor.getPopularity() + "");
                binding.actorBioText.setVisibility(View.VISIBLE);
                binding.knownForText.setVisibility(View.VISIBLE);
                binding.popularityIcon.setVisibility(View.VISIBLE);
                JsonArray jsonArray = actor.getMovie_credits().getAsJsonArray("cast");
                popularMovies = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<ArrayList<Movie>>() {
                        }.getType());
                knownForMovies(popularMovies);
            }
        });

        binding.downloadPoster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "You need to grant Permission", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                } else {
                    String fileName = mActor.getName() + "" + UUID.randomUUID().toString() + ".jpg";
                    downloadImageNew(fileName, Constants.ImageBaseURL + mActor.getProfile_path());

                }
            }
        });

    }

    private void downloadImageNew(String filename, String downloadUrlOfImage) {
        try {
            DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(getContext(), "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void knownForMovies(ArrayList<Movie> movies) {
        Log.e(TAG, "initKnownFor: " + movies.size());
        binding.knownForRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        adapter = new KnownForMoviesAdapter(getContext(), movies);
        binding.knownForRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}