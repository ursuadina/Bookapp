package com.example.andreea.bookhunt.models;

public class LastSearchBook {
    private String title;
    private String author;
    private String lastSearched;
    private String username;
    private long lastSearchLong;

    public LastSearchBook(String title, String author, String lastSearched, String username, long lastSearchLong) {
        this.title = title;
        this.author = author;
        this.lastSearched = lastSearched;
        this.username = username;
        this.lastSearchLong = lastSearchLong;
    }

    public LastSearchBook() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLastSearched() {
        return lastSearched;
    }

    public void setLastSearched(String lastSearched) {
        this.lastSearched = lastSearched;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLastSearchLong() {
        return lastSearchLong;
    }

    public void setLastSearchLong(long lastSearchLong) {
        this.lastSearchLong = lastSearchLong;
    }
}
