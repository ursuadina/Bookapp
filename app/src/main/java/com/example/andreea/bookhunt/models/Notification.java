package com.example.andreea.bookhunt.models;

public class Notification {
    private String from;
    private String message;
    private String receivedAt;

    public Notification(String from, String message, String receivedAt) {
        this.from = from;
        this.message = message;
        this.receivedAt = receivedAt;
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
}
