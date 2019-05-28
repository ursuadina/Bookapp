package com.example.andreea.bookhunt.models;

public class Book {
    private String bookId;
    private String bookTitle;
    private String author;
    private String photoUrl;

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

    @Override
    public String toString() {
        return "Book{" +
                "bookTitle='" + bookTitle + '\'' +
                ", author='" + author + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public Book(String bookId, String bookTitle, String author, String mPhotoUrl) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.photoUrl = mPhotoUrl;
    }

    public Book() {

    }
}
