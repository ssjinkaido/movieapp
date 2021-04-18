package com.example.movieapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.adapter.SearchMoviesAdapter;
import com.example.movieapp.databinding.FragmentSearchMoviesBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchMoviesFragment extends Fragment {
    private FragmentSearchMoviesBinding binding;
    private ArrayList<Movie> moviesList;
    private SearchMoviesAdapter searchMoviesAdapter;
    private HomeViewModel viewModel;
    private HashMap<String, String> queryMap;
    private String queryText = "";

    public SearchMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchMoviesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryMap = new HashMap<>();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        moviesList=new ArrayList<>();
        queryMap.put("api_key", Constants.API_KEY);
        queryMap.put("query", queryText);

        initRecyclerView();
        observeData();
        viewModel.getQueryMovies(queryMap);

        binding.searchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryText = binding.searchKeyword.getText().toString().trim().toLowerCase();
                queryMap.clear();
                queryMap.put("api_key", Constants.API_KEY);
                queryMap.put("query",queryText);

                viewModel.getQueryMovies(queryMap);
            }
        });
        binding.searchKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    queryText = binding.searchKeyword.getText().toString().trim().toLowerCase();
                    queryMap.clear();
                    queryMap.put("api_key", Constants.API_KEY);
                    queryMap.put("query",queryText);

                    viewModel.getQueryMovies(queryMap);
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        binding.searchMoviesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        searchMoviesAdapter = new SearchMoviesAdapter(getContext(), moviesList);
        binding.searchMoviesRecyclerView.setAdapter(searchMoviesAdapter);
    }

    private void observeData() {
        viewModel.getQueriesMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                searchMoviesAdapter.setMovieListResults(movies);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}