package com.example.movieapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.databinding.MovieItemBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.ui.SearchMoviesFragmentDirections;
import com.example.movieapp.utils.Constants;

import java.util.ArrayList;

public class SearchMoviesAdapter extends RecyclerView.Adapter<SearchMoviesAdapter.SearchMoviesHolder> {
    private MovieItemBinding binding;
    private ArrayList<Movie> moviesList;
    private Context mContext;
    private String temp;

    public SearchMoviesAdapter(Context mContext, ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SearchMoviesAdapter.SearchMoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = MovieItemBinding.inflate(inflater, parent, false);
        return new SearchMoviesHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull SearchMoviesAdapter.SearchMoviesHolder holder, int position) {
        holder.binding.movieName.setText(moviesList.get(position).getTitle());

        temp = "";

        for (int i = 0; i < moviesList.get(position).getGenre_ids().size(); i++) {
            if (i == moviesList.get(position).getGenre_ids().size() - 1)
                temp += Constants.getGenreMap().get(moviesList.get(position).getGenre_ids().get(i));
            else
                temp += Constants.getGenreMap().get(moviesList.get(position).getGenre_ids().get(i)) + " | ";
        }

        holder.binding.movieGenre.setText(temp);
        holder.binding.movieRating.setRating(moviesList.get(position).getVote_average().floatValue() / 2);
        String[] movieYear = moviesList.get(position).getRelease_date()
                .split("-");
        holder.binding.movieYear.setText(movieYear[0]);
        Glide.with(mContext).load(Constants.ImageBaseURL + moviesList.get(position).getPoster_path())
                .into(holder.binding.movieImage);

        holder.binding.movieItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchMoviesFragmentDirections.ActionSearchMoviesToMovieDetails action =
                        SearchMoviesFragmentDirections.actionSearchMoviesToMovieDetails(moviesList.get(position).getId());
                Navigation.findNavController(view).navigate(action);
            }
        });
        holder.binding.movieItemLayout.setClipToOutline(true);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class SearchMoviesHolder extends RecyclerView.ViewHolder {
        private MovieItemBinding binding;

        public SearchMoviesHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setMovieListResults(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }
}
