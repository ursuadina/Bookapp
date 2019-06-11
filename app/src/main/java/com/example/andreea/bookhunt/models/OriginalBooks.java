package com.example.andreea.bookhunt.models;

public class OriginalBooks {
    private String bookId;
    private String bookTitle;
    private String description;
    private String bookAuthor;
    private String titleAuthor;


    public OriginalBooks(String bookId, String bookTitle, String description, String bookAuthor, String titleAuthor) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.description = description;
        this.bookAuthor = bookAuthor;
        this.titleAuthor = titleAuthor;
    }

    public OriginalBooks() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getTitleAuthor() {
        return titleAuthor;
    }

    public void setTitleAuthor(String titleAuthor) {
        this.titleAuthor = titleAuthor;
    }
}
