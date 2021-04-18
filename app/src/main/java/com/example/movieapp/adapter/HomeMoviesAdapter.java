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
import com.example.movieapp.databinding.HomeMovieItemBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.ui.HomeFragment;
import com.example.movieapp.ui.HomeFragmentDirections;
import com.example.movieapp.utils.Constants;

import java.util.ArrayList;

public class HomeMoviesAdapter extends RecyclerView.Adapter<HomeMoviesAdapter.HomeMovieViewHolder> {
    private static final String TAG = "ViewPagerAdapter";
    private ArrayList<Movie> movieList;
    private Context mContext;
    private HomeMovieItemBinding binding;

    public HomeMoviesAdapter(Context mContext, ArrayList<Movie> movieList) {
        this.movieList = movieList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HomeMoviesAdapter.HomeMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = HomeMovieItemBinding.inflate(inflater, parent, false);
        return new HomeMovieViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull HomeMoviesAdapter.HomeMovieViewHolder holder, int position) {
        holder.binding.movieItemRelativeLayout.setClipToOutline(true);
        holder.binding.movieItemName.setText(movieList.get(position).getTitle());
        Glide.with(mContext).load(Constants.ImageBaseURL + movieList.get(position).getPoster_path())
                .into(holder.binding.movieItemImage);
        holder.binding.movieItemVotes.setText(Integer.toString(movieList.get(position).getVote_count()));
        holder.binding.movieItemRelativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHome3ToMovieDetails action = HomeFragmentDirections.actionHome3ToMovieDetails(movieList.get(position).getId());
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class HomeMovieViewHolder extends RecyclerView.ViewHolder {
        private HomeMovieItemBinding binding;

        public HomeMovieViewHolder(HomeMovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setMovieListResults(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }
}
