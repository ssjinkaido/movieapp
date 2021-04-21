package com.example.movieapp.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieapp.adapter.HomeMoviesAdapter;
import com.example.movieapp.adapter.ViewPagerAdapter;
import com.example.movieapp.databinding.FragmentHomeBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private static final String TAG = "Home";
    private HomeViewModel viewModel;
    private ArrayList<Movie> currentMovies, upComingMovies, popularMovies, topRatedMovies;
    private ViewPagerAdapter currentlyShowingMoviesAdapter;
    private HomeMoviesAdapter upComingMoviesAdapter, popularMoviesAdapter, topRatedMoviesAdapter;
    private HashMap<String, String> map = new HashMap<>();

    //this shit makes me waste tons of time, since it does not load data whenever I come back this
    //it loads at first, but after i move between fragment and comeback to home, it does not load data
    //putting this helps
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentMovies = new ArrayList<>();
        upComingMovies = new ArrayList<>();
        popularMovies = new ArrayList<>();
        topRatedMovies = new ArrayList<>();

        viewModel = new ViewModelProvider(HomeFragment.this).get(HomeViewModel.class);
        map.put("api_key", Constants.API_KEY);
        map.put("page", "1");

        binding.progressBar.setVisibility(View.VISIBLE);

        observeData();
        getMoviesList();
        setUpRecyclerViewsAndViewPager();
        setUpOnClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
        observeData();
        binding.progressBar.setVisibility(View.VISIBLE);
        if (Constants.isNetworkAvailable(getActivity())) {
            getMoviesList();
        } else {
            observeData();
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMoviesList() {
        viewModel.getCurrentlyShowingMovies(map);
        viewModel.getUpcomingMovies(map);
        viewModel.getPopularMovies(map);
        viewModel.getTopRatedMovies(map);
    }

    private void setUpRecyclerViewsAndViewPager() {
        currentlyShowingMoviesAdapter = new ViewPagerAdapter(getContext(), currentMovies);
        binding.currentlyShowingViewPager.setAdapter(currentlyShowingMoviesAdapter);

        upComingMoviesAdapter = new HomeMoviesAdapter(getContext(), upComingMovies);
        binding.upComingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.upComingRecyclerView.setAdapter(upComingMoviesAdapter);

        popularMoviesAdapter = new HomeMoviesAdapter(getContext(), popularMovies);
        binding.popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.popularRecyclerView.setAdapter(popularMoviesAdapter);

        topRatedMoviesAdapter = new HomeMoviesAdapter(getContext(), topRatedMovies);
        binding.topRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.topRatedRecyclerView.setAdapter(topRatedMoviesAdapter);
    }

    //getViewLifecycleOwner uses for fragment
    private void observeData() {
        viewModel.getCurrentlyShowingList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies.size() == 0 || movies == null) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    currentlyShowingMoviesAdapter.setMovieListResults(movies);
                    currentlyShowingMoviesAdapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.getUpcomingMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                upComingMoviesAdapter.setMovieListResults(movies);
                upComingMoviesAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getPopularMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                popularMoviesAdapter.setMovieListResults(movies);
                popularMoviesAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getTopRatedMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {

            @Override
            public void onChanged(ArrayList<Movie> movies) {
                topRatedMoviesAdapter.setMovieListResults(movies);
                topRatedMoviesAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setUpOnClick() {
        binding.viewAllCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHome3ToMovies action = HomeFragmentDirections.actionHome3ToMovies();
                action.setMovieCategory(Constants.Current);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.viewAllTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHome3ToMovies action = HomeFragmentDirections.actionHome3ToMovies();
                action.setMovieCategory(Constants.TopRated);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.viewAllPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHome3ToMovies action = HomeFragmentDirections.actionHome3ToMovies();
                action.setMovieCategory(Constants.Popular);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.viewAllUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHome3ToMovies action = HomeFragmentDirections.actionHome3ToMovies();
                action.setMovieCategory(Constants.Upcoming);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}