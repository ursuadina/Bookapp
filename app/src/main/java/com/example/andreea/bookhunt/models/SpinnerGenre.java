package com.example.andreea.bookhunt.models;

public class SpinnerGenre {
    private String genre;
    private int number;

    public SpinnerGenre(String genre, int number) {
        this.genre = genre;
        this.number = number;
    }

    public SpinnerGenre() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}
