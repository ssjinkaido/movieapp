package com.example.movieapp.repository;

import androidx.lifecycle.LiveData;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Favourite;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.model.ReviewResponse;
import com.example.movieapp.network.MovieApiService;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.QueryMap;

public class Repository {
    private static final String TAG = "Repository";
    private MovieApiService movieApiService;

    @Inject
    public Repository(MovieApiService apiService) {
        this.movieApiService = apiService;
    }

    public Observable<MovieResponse> getCurrentlyShowing(@QueryMap HashMap<String, String> map) {
        return movieApiService.getCurrentlyShowing(map);
    }

    public Observable<MovieResponse> getPopular(@QueryMap HashMap<String, String> map) {
        return movieApiService.getPopular(map);
    }

    public Observable<MovieResponse> getUpcoming(@QueryMap HashMap<String, String> map) {
        return movieApiService.getUpcoming(map);
    }

    public Observable<MovieResponse> getTopRated(@QueryMap HashMap<String, String> map) {
        return movieApiService.getTopRated(map);
    }

    public Observable<ReviewResponse> getReviews(int id, @QueryMap HashMap<String, String> map) {
        return movieApiService.getReviews(id, map);
    }

    public Observable<Movie> getMovieDetails(int movieId, @QueryMap HashMap<String, String> map) {
        return movieApiService.getMovieDetails(movieId, map);
    }

    public Observable<JsonObject> getCast(int movieId, HashMap<String, String> map) {
        return movieApiService.getCast(movieId, map);
    }

    public Observable<Actor> getActorDetails(int personId, HashMap<String, String> map) {
        return movieApiService.getActorDetails(personId, map);
    }

    public Observable<JsonObject> getMoviesBySearch(HashMap<String, String> map) {
        return movieApiService.getMoviesBySearch(map);
    }
}
