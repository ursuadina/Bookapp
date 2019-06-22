package com.example.andreea.bookhunt.models;

public class Notification {
    private String from;
    private String message;
    private String receivedAt;
    private String id;
    private String bookId;

    public Notification(String from, String message, String receivedAt, String id, String bookId) {
        this.from = from;
        this.message = message;
        this.receivedAt = receivedAt;
        this.id = id;
        this.bookId = bookId;
    }

    public Notification() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
