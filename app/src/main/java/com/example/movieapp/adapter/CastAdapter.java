package com.example.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.databinding.CastItemBinding;
import com.example.movieapp.databinding.HomeMovieItemBinding;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Movie;
import com.example.movieapp.ui.ActorDetailsFragmentDirections;
import com.example.movieapp.ui.MoviesDetailsFragmentDirections;
import com.example.movieapp.utils.Constants;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private CastItemBinding binding;
    private Context mContext;
    private ArrayList<Cast> casts;

    public CastAdapter(Context mContext, ArrayList<Cast> casts) {
        this.mContext = mContext;
        this.casts = casts;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = CastItemBinding.inflate(inflater, parent, false);
        return new CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
        holder.binding.castName.setText(casts.get(position).getName());
        holder.binding.castRole.setText(casts.get(position).getCharacter());
        Glide.with(mContext).load(Constants.ImageBaseURL + casts.get(position).getProfile_path()).apply(RequestOptions.circleCropTransform())
                .into(holder.binding.castImage);
        holder.binding.castImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoviesDetailsFragmentDirections.ActionMovieDetailsToActorDetails action = MoviesDetailsFragmentDirections.actionMovieDetailsToActorDetails
                        (casts.get(position).getId());
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        private final CastItemBinding binding;

        public CastViewHolder(CastItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setCastListResults(ArrayList<Cast> casts) {
        this.casts = casts;
    }
}
