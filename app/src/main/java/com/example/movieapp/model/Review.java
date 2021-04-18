package com.example.movieapp.model;

import java.util.ArrayList;

public class Review {
    private UserReview author_details;
    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public Review() {
    }

    public UserReview getUserReview() {
        return author_details;
    }

    public void setUserReview(UserReview author_details) {
        this.author_details = author_details;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
