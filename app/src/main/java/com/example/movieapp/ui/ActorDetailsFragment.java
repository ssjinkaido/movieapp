package com.example.movieapp.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActorDetailsFragment extends Fragment {
    private static final String TAG = "ActorDetails";
    private FragmentActorDetailsBinding binding;
    private Integer personID;
    private HashMap<String, String> queries;
    private KnownForMoviesAdapter adapter;
    private ArrayList<Movie> popularMovies;

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
        ActorDetailsFragmentArgs args=ActorDetailsFragmentArgs.fromBundle(getArguments());
        personID=args.getPersonId();

        queries = new HashMap<>();
        queries.put("api_key", Constants.API_KEY);
        queries.put("append_to_response","movie_credits");

        viewModel.getActorDetails(personID,queries);
        viewModel.getActor().observe(getViewLifecycleOwner(), new Observer<Actor>() {
            @Override
            public void onChanged(Actor actor) {
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
                        new TypeToken<ArrayList<Movie>>(){}.getType());
                knownForMovies(popularMovies);
            }
        });

    }

    private void knownForMovies(ArrayList<Movie>movies){
        Log.e(TAG, "initKnownFor: "+ movies.size() );
        binding.knownForRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        adapter = new KnownForMoviesAdapter(getContext(),movies);
        binding.knownForRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}