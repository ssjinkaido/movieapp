package com.example.movieapp.network;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.model.ReviewResponse;
import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Flowable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MovieApiService {
    @GET("movie/now_playing")
    Observable<MovieResponse> getCurrentlyShowing(@QueryMap HashMap<String, String> queries);

    @GET("movie/popular")
    Observable<MovieResponse> getPopular(@QueryMap HashMap<String, String> queries);

    @GET("movie/upcoming")
    Observable<MovieResponse> getUpcoming(@QueryMap HashMap<String, String> queries);

    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRated(@QueryMap HashMap<String, String> queries);

    @GET("movie/{movie_id}/reviews")
    Observable<ReviewResponse> getReviews(@Path("movie_id") int id, @QueryMap HashMap<String, String> queries);

    @GET("movie/{movie_id}")
    Observable<Movie> getMovieDetails(@Path("movie_id") int id, @QueryMap HashMap<String, String> queries);

    @GET("movie/{movie_id}/credits")
    Observable<JsonObject> getCast(@Path("movie_id") int id, @QueryMap HashMap<String, String> queries);

    @GET("person/{person_id}")
    Observable<Actor> getActorDetails(@Path("person_id") int id, @QueryMap HashMap<String, String> queries);

    @GET("person/{person_id}/images")
    Observable<JsonObject> getActorImages(@Path("person_id") int id, @Query("api_key") String api);

    @GET("search/movie")
    Observable<JsonObject> getMoviesBySearch(@QueryMap HashMap<String, String> queries);

}
