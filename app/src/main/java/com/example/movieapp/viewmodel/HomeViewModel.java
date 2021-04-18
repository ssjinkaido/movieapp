package com.example.movieapp.viewmodel;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Genre;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.model.Review;
import com.example.movieapp.model.ReviewResponse;
import com.example.movieapp.repository.Repository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class HomeViewModel extends ViewModel {
    private Repository repository;
    private MutableLiveData<ArrayList<Movie>> currentMoviesList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> popularMoviesList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> topRatedMoviesList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> upcomingMoviesList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> queriesMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Review>> reviewMoviesList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Cast>> movieCastList = new MutableLiveData<>();
    private MutableLiveData<Movie> movieDetails = new MutableLiveData<>();
    private MutableLiveData<Actor> actorDetails = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @ViewModelInject
    public HomeViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ArrayList<Movie>> getCurrentlyShowingList() {
        return currentMoviesList;
    }

    public MutableLiveData<ArrayList<Movie>> getPopularMoviesList() {
        return popularMoviesList;
    }

    public MutableLiveData<ArrayList<Movie>> getTopRatedMoviesList() {
        return topRatedMoviesList;
    }

    public MutableLiveData<ArrayList<Movie>> getUpcomingMoviesList() {
        return upcomingMoviesList;
    }

    public MutableLiveData<ArrayList<Review>> getReviewList() {
        return reviewMoviesList;
    }

    public MutableLiveData<Movie> getMovie() {
        return movieDetails;
    }

    public MutableLiveData<Actor> getActor() {
        return actorDetails;
    }

    public MutableLiveData<ArrayList<Cast>> getMovieCastList() {
        return movieCastList;
    }

    public MutableLiveData<ArrayList<Movie>> getQueriesMovies() {
        return queriesMovies;
    }

    public void getCurrentlyShowingMovies(HashMap<String, String> map) {
        disposables.add(repository.getCurrentlyShowing(map).subscribeOn(Schedulers.io()).map(new Function<MovieResponse, ArrayList<Movie>>() {
            @Override
            public ArrayList<Movie> apply(MovieResponse movieResponse) throws Throwable {
                return movieResponse.getResults();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ArrayList<Movie>>() {

            @Override
            public void onNext(@NonNull ArrayList<Movie> movies) {
                currentMoviesList.setValue(movies);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public void getUpcomingMovies(HashMap<String, String> map) {
        disposables.add(repository.getUpcoming(map).subscribeOn(Schedulers.io()).map(new Function<MovieResponse, ArrayList<Movie>>() {
            @Override
            public ArrayList<Movie> apply(MovieResponse movieResponse) throws Throwable {
                return movieResponse.getResults();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ArrayList<Movie>>() {

            @Override
            public void onNext(@NonNull ArrayList<Movie> movies) {
                upcomingMoviesList.setValue(movies);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public void getPopularMovies(HashMap<String, String> map) {
        disposables.add(repository.getPopular(map).subscribeOn(Schedulers.io()).map(new Function<MovieResponse, ArrayList<Movie>>() {
            @Override
            public ArrayList<Movie> apply(MovieResponse movieResponse) throws Throwable {
                return movieResponse.getResults();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ArrayList<Movie>>() {

            @Override
            public void onNext(@NonNull ArrayList<Movie> movies) {
                popularMoviesList.setValue(movies);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public void getTopRatedMovies(HashMap<String, String> map) {
        disposables.add(repository.getTopRated(map).subscribeOn(Schedulers.io()).map(new Function<MovieResponse, ArrayList<Movie>>() {
            @Override
            public ArrayList<Movie> apply(MovieResponse movieResponse) throws Throwable {
                return movieResponse.getResults();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ArrayList<Movie>>() {

            @Override
            public void onNext(@NonNull ArrayList<Movie> movies) {
                topRatedMoviesList.setValue(movies);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public void getReviewsMovies(int movieId, HashMap<String, String> map) {
        disposables.add(repository.getReviews(movieId, map).subscribeOn(Schedulers.io()).map(new Function<ReviewResponse, ArrayList<Review>>() {
            @Override
            public ArrayList<Review> apply(ReviewResponse reviewResponse) throws Throwable {
                return reviewResponse.getResults();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ArrayList<Review>>() {

            @Override
            public void onNext(@NonNull ArrayList<Review> reviews) {
                reviewMoviesList.setValue(reviews);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "reviewMoviesList: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public void getCast(int movieId, HashMap<String, String> map) {
        disposables.add(repository.getCast(movieId, map).subscribeOn(Schedulers.io()).map(new Function<JsonObject, ArrayList<Cast>>() {

            @Override
            public ArrayList<Cast> apply(JsonObject jsonObject) throws Throwable {
                JsonArray jsonArray = jsonObject.getAsJsonArray("cast");
                return new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<Cast>>() {
                }.getType());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> movieCastList.setValue(result),
                        error -> Log.e(TAG, "getCastList: " + error.getMessage())));
    }

    public void getMovieDetails(int movieId, HashMap<String, String> map) {
        disposables.add(repository.getMovieDetails(movieId, map)
                .subscribeOn(Schedulers.io())
                .map(new Function<Movie, Movie>() {

                    @Override
                    public Movie apply(Movie movie) throws Throwable {
                        ArrayList<String> genreNames = new ArrayList<>();
                        // MovieResponse gives list of genre(object) so we will map each id to it genre name here.a

                        for (Genre genre : movie.getGenres()) {
                            genreNames.add(genre.getName());
                        }
                        movie.setGenre_names(genreNames);
                        return movie;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> movieDetails.setValue(result),
                        error -> Log.e(TAG, "getMovieDetails: " + error.getMessage())));
    }

    public void getActorDetails(int personId, HashMap<String, String> map) {
        disposables.add(repository.getActorDetails(personId, map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> actorDetails.setValue(result), error -> Log.e(TAG, "getActorDetails: " + error.getMessage())));

    }

    public void getQueryMovies(HashMap<String, String> map) {
        disposables.add(repository.getMoviesBySearch(map)
                .subscribeOn(Schedulers.io()).map(new Function<JsonObject, ArrayList<Movie>>() {
                    @Override
                    public ArrayList<Movie> apply(JsonObject jsonObject) throws Throwable {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("results");
                        ArrayList<Movie> movieList = new Gson().fromJson(jsonArray.toString(),
                                new TypeToken<ArrayList<Movie>>() {
                                }.getType());
                        return movieList;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> queriesMovies.setValue(result),
                        error -> Log.e(TAG, "getPopularMovies: " + error.getMessage())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}
