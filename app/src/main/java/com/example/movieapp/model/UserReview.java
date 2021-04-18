package com.example.movieapp.model;

public class UserReview {
    private String username;
    private String avatar_path;
    private String rating;

    public UserReview(String username, String avatar_path, String rating) {
        this.username = username;
        this.avatar_path = avatar_path;
        this.rating = rating;
    }

    public UserReview() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
