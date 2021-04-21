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
import com.example.movieapp.ui.MoviesFragmentDirections;
import com.example.movieapp.utils.Constants;

import java.util.ArrayList;

public class CategoryMoviesAdapter extends RecyclerView.Adapter<CategoryMoviesAdapter.CategoryMoviesViewHolder> {
    private MovieItemBinding binding;
    private Context mContext;
    private ArrayList<Movie> movies;
    private String temp;

    public CategoryMoviesAdapter(Context mContext, ArrayList<Movie> movies) {
        this.mContext = mContext;
        this.movies = movies;
    }

    @NonNull
    @Override
    public CategoryMoviesAdapter.CategoryMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = MovieItemBinding.inflate(inflater,parent,false);
        return new CategoryMoviesViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CategoryMoviesViewHolder holder, int position) {
        holder.binding.movieName.setText(movies.get(position).getTitle());

        temp = "";

        for (int i = 0; i < movies.get(position).getGenre_ids().size(); i++){
            if(i ==  movies.get(position).getGenre_ids().size() -1)
                temp+= Constants.getGenreMap().get(movies.get(position).getGenre_ids().get(i));
            else
                temp+= Constants.getGenreMap().get(movies.get(position).getGenre_ids().get(i)) + " | ";
        }

        holder.binding.movieGenre.setText(temp);
        holder.binding.movieRating.setRating(movies.get(position).getVote_average().floatValue()/2);
        String[] movieYear = movies.get(position).getRelease_date()
                .split("-");
        holder.binding.movieYear.setText(movieYear[0]);
        Glide.with(mContext).load(Constants.ImageBaseURL + movies.get(position).getPoster_path())
                .into(holder.binding.movieImage);

        holder.binding.movieItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoviesFragmentDirections.ActionMoviesToMovieDetails action =
                        MoviesFragmentDirections.actionMoviesToMovieDetails(movies.get(position).getId());
                Navigation.findNavController(view).navigate(action);
            }
        });

        holder.binding.movieItemLayout.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class CategoryMoviesViewHolder extends RecyclerView.ViewHolder{
        private MovieItemBinding binding;
        public CategoryMoviesViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public void setMovieListResults(ArrayList<Movie> movies) {
        this.movies= movies;
    }
}
