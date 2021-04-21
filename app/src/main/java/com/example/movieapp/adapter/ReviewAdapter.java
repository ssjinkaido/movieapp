package com.example.movieapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.R;
import com.example.movieapp.databinding.ItemReviewBinding;
import com.example.movieapp.model.Comment;
import com.example.movieapp.model.Review;
import com.example.movieapp.model.User;
import com.example.movieapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ItemReviewBinding binding;
    private Context mContext;
    private ArrayList<Comment> commentList;

    public ReviewAdapter(Context mContext, ArrayList<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ItemReviewBinding.inflate(inflater, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.binding.textContent.setText(comment.getComment());
        FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getPublisherId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                holder.binding.textAuthor.setText(user.getName());
                if (user.getImage().equals("default")) {
                    holder.binding.imageAuthor.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImage()).into(holder.binding.imageAuthor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        long time = comment.getTime();
        long now = System.currentTimeMillis();
        if (time < 1000000000000L) {
            time *= 1000;
        }
        if (time > now || time <= 0) {
            holder.binding.textDate.setText("");
        }

        final long diff = now - time;
        String convTime="";
        String suffix="ago";
        long second = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hour   = TimeUnit.MILLISECONDS.toHours(diff);
        long day  = TimeUnit.MILLISECONDS.toDays(diff);
        Log.d("TIME:","test "+second+","+minute+"");
        if (second < 60) {
            convTime = second + " Seconds " + suffix;
        } else if (minute < 60) {
            convTime = minute + " Minutes "+suffix;
        } else if (hour < 24) {
            convTime = hour + " Hours "+suffix;
        } else if (day >= 7) {
            if (day > 360) {
                convTime = (day / 360) + " Years " + suffix;
            } else if (day > 30) {
                convTime = (day / 30) + " Months " + suffix;
            } else {
                convTime = (day / 7) + " Week " + suffix;
            }
        } else if (day < 7) {
            convTime = day+" Days "+suffix;
        }
        holder.binding.textDate.setText(convTime);




    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ItemReviewBinding binding;

        public ReviewViewHolder(@NonNull ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }
}
