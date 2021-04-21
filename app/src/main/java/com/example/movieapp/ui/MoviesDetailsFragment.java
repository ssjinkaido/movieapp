package com.example.movieapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.adapter.CastAdapter;
import com.example.movieapp.adapter.ReviewAdapter;
import com.example.movieapp.databinding.MovieDetailsBinding;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Comment;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Review;
import com.example.movieapp.model.User;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.utils.Util;
import com.example.movieapp.viewmodel.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

import static androidx.core.content.ContextCompat.getSystemService;


@AndroidEntryPoint
public class MoviesDetailsFragment extends Fragment {

    private static final String TAG = "MovieDetailsFragment";
    private MovieDetailsBinding binding;
    private HomeViewModel viewModel;
    private Integer movieId;
    private int hour = 0, min = 0;
    private Movie mMovie;
    private HashMap<String, String> queryMap;
    private String temp, videoId;
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Cast> casts;
    private ArrayList<Comment> comments;

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
        comments = new ArrayList<>();
        if (getArguments() != null) {
            MoviesDetailsFragmentArgs args = MoviesDetailsFragmentArgs.fromBundle(getArguments());
            movieId = args.getMovieId();
        }


        Log.d(TAG, "movieId " + movieId);

        observeData();
        getComment();
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

        reviewAdapter = new ReviewAdapter(getContext(), comments);
        binding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.reviewRecyclerView.setAdapter(reviewAdapter);
        reviewAdapter.setCommentList(comments);
        reviewAdapter.notifyDataSetChanged();

        binding.playTrailer.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View view) {
                if (videoId != null) {
                    VideoDialog dialog = new VideoDialog(videoId);
                    dialog.show(getParentFragmentManager(), "Video Dialog");
                } else
                    Toast.makeText(getContext(), "Sorry trailer not found!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.downloadPoster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "You need to grant Permission", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                } else {
                    String fileName = mMovie.getTitle() + "" + UUID.randomUUID().toString() + ".jpg";
                    downloadImageNew(fileName, Constants.ImageBaseURL + mMovie.getPoster_path());

                }
            }
        });

        binding.post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.addComment.getText().toString())) {
                    Toast.makeText(getContext(), "No comment added", Toast.LENGTH_SHORT).show();
                } else {
                    putComment();
                }
            }
        });

    }
    private void getComment(){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(String.valueOf(movieId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comment comment = ds.getValue(Comment.class);
                    comments.add(comment);
                }
                Collections.reverse(comments);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void downloadImageNew(String filename, String downloadUrlOfImage) {
        try {
            DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(getContext(), "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Image download failed.", Toast.LENGTH_SHORT).show();
        }
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
                JsonArray array = movie.getVideos().getAsJsonArray("results");
                videoId = array.get(0).getAsJsonObject().get("key").getAsString();
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
        FirebaseDatabase.getInstance().getReference().child("Users").child(Util.getUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImage().equals("default")) {
                    binding.imageProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImage()).into(binding.imageProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void putComment(){
        HashMap<String, Object> map = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(String.valueOf(mMovie.getId()));
        String id = reference.push().getKey();
        Calendar calForDAte = Calendar.getInstance();

        Long tsLong = System.currentTimeMillis();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String date = currentDate.format(calForDAte.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String time = currentTime.format(calForDAte.getTime());
        map.put("id", id);
        map.put("comment", binding.addComment.getText().toString());
        map.put("publisherId", Util.getUserUid());
        map.put("date", date + "  " + time);
        map.put("time", tsLong);
        binding.addComment.setText("");
        reference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Comment added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
