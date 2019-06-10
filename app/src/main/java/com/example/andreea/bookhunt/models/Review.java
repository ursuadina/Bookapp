package com.example.andreea.bookhunt.models;

public class Review {
    private String review;
    private String user;
    private float rating;

    public Review(String review, String user, float rating) {
        this.review = review;
        this.user = user;
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
