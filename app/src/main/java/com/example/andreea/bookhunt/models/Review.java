package com.example.andreea.bookhunt.models;

import java.util.Date;

public class Review {
    private String review;
    private String userId;
    private float rating;
    private Date createdAt;
    private String bookId;

    public Review() {
    }

    public Review(String review, String userId, float rating, Date createdAt, String bookId) {
        this.review = review;
        this.userId = userId;
        this.rating = rating;
        this.createdAt = createdAt;
        this.bookId = bookId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
