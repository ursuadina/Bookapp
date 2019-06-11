package com.example.andreea.bookhunt.models;

public class Book {
    private String bookId;
    private String bookTitle;
    private String author;
    private String photoUrl;
    private float rating;
    private String description;
    private String originalBookId;

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

    @Override
    public String toString() {
        return "Book{" +
                "bookTitle='" + bookTitle + '\'' +
                ", author='" + author + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public Book(String bookId, String bookTitle, String author, String photoUrl, float rating, String description, String originalBookId) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.description = description;
        this.originalBookId = originalBookId;
    }


    public Book() {

    }
}
