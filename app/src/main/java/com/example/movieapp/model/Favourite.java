package com.example.movieapp.model;

public class Favourite {
    private String poster_path,title;
    private String id,vote_count;

    public Favourite() {
    }

    public Favourite(String poster_path, String title, String id, String vote_count) {
        this.poster_path = poster_path;
        this.title = title;
        this.id = id;
        this.vote_count = vote_count;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }
}
