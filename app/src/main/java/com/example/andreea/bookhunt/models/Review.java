package com.example.andreea.bookhunt.models;

public class Review {
    private String review;
    private String userId;
    private float rating;
    private String createdAt;
    private String bookId;
    private String reviewId;
    private long createdAtMiliseconds;
    private String userName;

    public Review() {
    }


    public Review(String review, String userId, float rating, String createdAt, String bookId, String reviewId, long createdAtMiliseconds, String userName) {
        this.review = review;
        this.userId = userId;
        this.rating = rating;
        this.createdAt = createdAt;
        this.bookId = bookId;
        this.reviewId = reviewId;
        this.createdAtMiliseconds = createdAtMiliseconds;
        this.userName = userName;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public long getCreatedAtMiliseconds() {
        return createdAtMiliseconds;
    }

    public void setCreatedAtMiliseconds(long createdAtMiliseconds) {
        this.createdAtMiliseconds = createdAtMiliseconds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
