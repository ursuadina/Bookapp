package com.example.andreea.bookhunt.models;

import java.util.ArrayList;

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
    private long lastSearch;
    private String lastSearchDate;
    private String username;
    private ArrayList<String> genres;

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


    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public long getLastSearch() {
        return lastSearch;
    }

    public void setLastSearch(long lastSearch) {
        this.lastSearch = lastSearch;
    }

    public String getLastSearchDate() {
        return lastSearchDate;
    }

    public void setLastSearchDate(String lastSearchDate) {
        this.lastSearchDate = lastSearchDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookTitle='" + bookTitle + '\'' +
                ", author='" + author + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }


    public Book(String bookId, String bookTitle, String author, String photoUrl, float rating, String description, String originalBookId, String review_widget, boolean fav, long lastSearch, String lastSearchDate, ArrayList<String> genres) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.description = description;
        this.originalBookId = originalBookId;
        this.review_widget = review_widget;
        this.fav = fav;
        this.lastSearch = lastSearch;
        this.lastSearchDate = lastSearchDate;
        this.genres = genres;
    }

    public Book() {

    }
}
