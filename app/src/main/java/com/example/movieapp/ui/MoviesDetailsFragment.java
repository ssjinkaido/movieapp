package com.example.movieapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.adapter.CastAdapter;
import com.example.movieapp.adapter.HomeMoviesAdapter;
import com.example.movieapp.adapter.ReviewAdapter;
import com.example.movieapp.databinding.MovieDetailsBinding;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Review;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.utils.Util;
import com.example.movieapp.viewmodel.HomeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MoviesDetailsFragment extends Fragment {

    private static final String TAG = "MovieDetailsFragment";
    private MovieDetailsBinding binding;
    private HomeViewModel viewModel;
    private Integer movieId;
    private int hour = 0, min = 0;
    private Movie mMovie;
    private HashMap<String, String> queryMap;
    private String temp;
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Cast> casts;
    private ArrayList<Review> reviews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MovieDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(MoviesDetailsFragment.this).get(HomeViewModel.class);
        queryMap = new HashMap<>();
        casts = new ArrayList<>();
        reviews = new ArrayList<>();
        if (getArguments() != null) {
            MoviesDetailsFragmentArgs args = MoviesDetailsFragmentArgs.fromBundle(getArguments());
            movieId = args.getMovieId();
        }


        Log.d(TAG, "movieId " + movieId);

        observeData();
        isLiked(movieId, binding.addToWishList);

        queryMap.put("api_key", Constants.API_KEY);
        queryMap.put("page", "1");
        queryMap.put("append_to_response", "videos");

        viewModel.getMovieDetails(movieId, queryMap);
        viewModel.getCast(movieId, queryMap);
        viewModel.getReviewsMovies(movieId, queryMap);

        castAdapter = new CastAdapter(getContext(), casts);
        binding.castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.castRecyclerView.setAdapter(castAdapter);
        binding.moviePoster.setClipToOutline(true);

        reviewAdapter = new ReviewAdapter(getContext(), reviews);
        binding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.reviewRecyclerView.setAdapter(reviewAdapter);
    }

    private void observeData() {
        viewModel.getMovie().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                mMovie = movie;
                Glide.with(getContext()).load(Constants.ImageBaseURL + movie.getPoster_path())
                        .centerCrop()
                        .into(binding.moviePoster);

                binding.movieName.setText(movie.getTitle());

                hour = movie.getRuntime() / 60;
                min = movie.getRuntime() % 60;
                binding.movieRuntime.setText(hour + "h " + min + "m");
                binding.moviePlot.setText(movie.getOverview());
                temp = "";
                for (int i = 0; i < movie.getGenres().size(); i++) {
                    if (i == movie.getGenres().size() - 1)
                        temp += movie.getGenres().get(i).getName();
                    else
                        temp += movie.getGenres().get(i).getName() + " | ";
                }
                binding.movieGenre.setText(temp);
                binding.playTrailer.setVisibility(View.VISIBLE);
                binding.movieCastText.setVisibility(View.VISIBLE);
                binding.moviePlotText.setVisibility(View.VISIBLE);
                binding.addToWishList.setVisibility(View.VISIBLE);
                isLiked(movieId, binding.addToWishList);
                binding.addToWishList.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (binding.addToWishList.getTag().equals("like")) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", String.valueOf(mMovie.getId()));
                            map.put("title", mMovie.getTitle());
                            map.put("vote_count", String.valueOf(mMovie.getVote_count()));
                            map.put("poster_path", mMovie.getPoster_path());
                            FirebaseDatabase.getInstance().getReference().child("Likes")
                                    .child(Util.getUserUid()).child(String.valueOf(movieId)).setValue(map);
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Likes")
                                    .child(Util.getUserUid()).child(String.valueOf(movieId)).removeValue();
                        }
                    }
                });

            }
        });
        viewModel.getMovieCastList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Cast>>() {
            @Override
            public void onChanged(ArrayList<Cast> actors) {
                Log.e(TAG, "onChanged: " + actors.size());
                castAdapter.setCastListResults(actors);
                castAdapter.notifyDataSetChanged();

            }
        });
        viewModel.getReviewList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Review>>() {

            @Override
            public void onChanged(ArrayList<Review> reviews) {
                Log.e(TAG, "onChanged: Review " + reviews.size());
                reviewAdapter.setReviewList(reviews);
                reviewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void isLiked(int movieId, ImageView imageview) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(Util.getUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(String.valueOf(movieId)).exists()) {
                    imageview.setImageResource(R.drawable.ic_playlist);
                    imageview.setTag("liked");
                } else {
                    imageview.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                    imageview.setTag("like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
