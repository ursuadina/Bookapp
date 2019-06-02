package com.example.andreea.bookhunt.models;

public class ResultIDB {
    private String mSnippet;
    private String mSource;
    private float mRating;

    public ResultIDB(String mSnippet, String mSource, float mRating) {
        this.mSnippet = mSnippet;
        this.mSource = mSource;
        this.mRating = mRating;
    }

    public String getmSnippet() {
        return mSnippet;
    }

    public void setmSnippet(String mSnippet) {
        this.mSnippet = mSnippet;
    }

    public String getmSource() {
        return mSource;
    }

    public void setmSource(String mSource) {
        this.mSource = mSource;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }
}
