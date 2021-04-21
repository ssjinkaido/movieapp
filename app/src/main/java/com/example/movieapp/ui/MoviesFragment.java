package com.example.movieapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.movieapp.adapter.CategoryMoviesAdapter;
import com.example.movieapp.databinding.FragmentMoviesBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.HomeViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MoviesFragment extends Fragment {
    private FragmentMoviesBinding binding;
    private HomeViewModel viewModel;
    private String moviesCategory="";
    private CategoryMoviesAdapter adapter;
    private ArrayList<Movie> moviesList;
    private int currentPage=1;
    private int totalPages=1;

    public MoviesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(MoviesFragment.this).get(HomeViewModel.class);
        if(getArguments()!=null){
            MoviesFragmentArgs args=MoviesFragmentArgs.fromBundle(getArguments());
            moviesCategory=args.getMovieCategory();
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        moviesList=new ArrayList<>();
        binding.moviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter = new CategoryMoviesAdapter(getContext(),moviesList);
        binding.moviesRecyclerView.setAdapter(adapter);
        binding.moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.moviesRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalPages) {
                        currentPage += 1;
                        getMoviesList();
                    }
                }
            }
        });
        getMoviesList();
    }



    private void getMoviesList() {
        switch (moviesCategory){
            case Constants.Current:
                viewModel.getCurrentlyShowingMovie(Constants.API_KEY,currentPage).observe(getViewLifecycleOwner(), new Observer<MovieResponse>() {
                    @Override
                    public void onChanged(MovieResponse movieResponse) {
                        if(movieResponse!=null){
                            totalPages=movieResponse.getTotal_pages();
                            if(movieResponse.getResults()!=null){
                                int oldCount=moviesList.size();
                                moviesList.addAll(movieResponse.getResults());
                                adapter.notifyItemRangeInserted(oldCount,moviesList.size());
                            }
                        }
                    }
                });
                binding.moviesCategoryTitle.setText("Currently Showing");
                break;
            case Constants.Upcoming:
                viewModel.getUpcomingMovie(Constants.API_KEY,currentPage).observe(getViewLifecycleOwner(), new Observer<MovieResponse>() {
                    @Override
                    public void onChanged(MovieResponse movieResponse) {
                        if(movieResponse!=null){
                            totalPages=movieResponse.getTotal_pages();
                            if(movieResponse.getResults()!=null){
                                int oldCount=moviesList.size();
                                moviesList.addAll(movieResponse.getResults());
                                adapter.notifyItemRangeInserted(oldCount,moviesList.size());
                            }
                        }
                    }
                });
                binding.moviesCategoryTitle.setText("Upcoming Movies");
                break;
            case Constants.TopRated:
                viewModel.getTopRatedMovie(Constants.API_KEY,currentPage).observe(getViewLifecycleOwner(), new Observer<MovieResponse>() {
                    @Override
                    public void onChanged(MovieResponse movieResponse) {
                        if(movieResponse!=null){
                            totalPages=movieResponse.getTotal_pages();
                            if(movieResponse.getResults()!=null){
                                int oldCount=moviesList.size();
                                moviesList.addAll(movieResponse.getResults());
                                adapter.notifyItemRangeInserted(oldCount,moviesList.size());
                            }
                        }
                    }
                });
                binding.moviesCategoryTitle.setText("Top Rated Movies");
                break;
            case Constants.Popular:
                viewModel.getPopularMovie(Constants.API_KEY,currentPage).observe(getViewLifecycleOwner(), new Observer<MovieResponse>() {
                    @Override
                    public void onChanged(MovieResponse movieResponse) {
                        if(movieResponse!=null){
                            totalPages=movieResponse.getTotal_pages();
                            if(movieResponse.getResults()!=null){
                                int oldCount=moviesList.size();
                                moviesList.addAll(movieResponse.getResults());
                                adapter.notifyItemRangeInserted(oldCount,moviesList.size());
                            }
                        }
                    }
                });
                binding.moviesCategoryTitle.setText("Popular Movies");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}