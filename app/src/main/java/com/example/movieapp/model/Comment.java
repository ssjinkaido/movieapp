package com.example.movieapp.model;

public class Comment {
    private String comment;
    private String id;
    private String publisherId;
    private String date;
    private long time;

    public Comment(String comment, String id, String publisherId, String date, long time) {
        this.comment = comment;
        this.id = id;
        this.publisherId = publisherId;
        this.date = date;
        this.time = time;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
