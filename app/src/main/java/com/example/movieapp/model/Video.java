package com.example.movieapp.model;

public class Video {

    private String id,key,type;

    public Video(String id, String key, String type) {
        this.id = id;
        this.key = key;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}