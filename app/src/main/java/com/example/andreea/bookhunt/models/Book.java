package com.example.andreea.bookhunt.models;

public class Book {
    private String bookId;
    private String bookTitle;
    private String author;
    private String photoUrl;
    private float rating;
    private String description;
    private String originalBookId;
    private String review_widget;
    private boolean fav;

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public float getRating() { return rating; }

    public void setRating(float rating) { this.rating = rating; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getOriginalBookId() {
        return originalBookId;
    }

    public void setOriginalBookId(String originalBookId) {
        this.originalBookId = originalBookId;
    }

    public String getReview_widget() {
        return review_widget;
    }

    public void setReview_widget(String review_widget) {
        this.review_widget = review_widget;
    }

    public boolean isfav() {
        return fav;
    }

    public void setfav(boolean fav) {
        this.fav = fav;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookTitle='" + bookTitle + '\'' +
                ", author='" + author + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public Book(String bookId, String bookTitle, String author, String photoUrl, float rating, String description, String originalBookId, String review_widget, boolean fav) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.description = description;
        this.originalBookId = originalBookId;
        this.review_widget = review_widget;
        this.fav = fav;
    }

    public Book() {

    }
}
