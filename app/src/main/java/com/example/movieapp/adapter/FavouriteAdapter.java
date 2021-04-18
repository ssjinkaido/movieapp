package com.example.movieapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.databinding.HomeMovieItemBinding;
import com.example.movieapp.model.Favourite;
import com.example.movieapp.utils.Constants;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{
    private HomeMovieItemBinding binding;
    private Context mContext;
    private ArrayList<Favourite> favouritesList;

    public FavouriteAdapter(Context mContext, ArrayList<Favourite> favouritesList) {
        this.mContext = mContext;
        this.favouritesList = favouritesList;
    }

    @NonNull
    @Override
    public FavouriteAdapter.FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = HomeMovieItemBinding.inflate(inflater, parent, false);
        return new FavouriteViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.FavouriteViewHolder holder, int position) {
        holder.binding.movieItemRelativeLayout.setClipToOutline(true);
        holder.binding.movieItemName.setText(favouritesList.get(position).getTitle());

        holder.binding.movieItemVotes.setText(favouritesList.get(position).getVote_count());

        Glide.with(mContext).load(Constants.ImageBaseURL + favouritesList.get(position).getPoster_path())
                .into(holder.binding.movieItemImage);
//        holder.binding.movieItemRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FavoriteDirections.ActionFavoriteToMovieDetails action =
//                        FavoriteDirections.actionFavoriteToMovieDetails(moviesList.get(position).getId());
//                Navigation.findNavController(view).navigate(action);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder{
        private HomeMovieItemBinding binding;
        public FavouriteViewHolder(@NonNull HomeMovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void setFavouriteList(ArrayList<Favourite> favouritesList) {
        this.favouritesList = favouritesList;
        notifyDataSetChanged();
    }
}
