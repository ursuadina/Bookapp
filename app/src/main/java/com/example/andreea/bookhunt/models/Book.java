package com.example.andreea.bookhunt.models;

public class Book {
    private String mBookName;
    private String mAuthor;
    private String mPhotoUrl;

    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mBookName='" + mBookName + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mPhotoUrl='" + mPhotoUrl + '\'' +
                '}';
    }

    public Book(String mBookName, String mAuthor, String mPhotoUrl) {
        this.mBookName = mBookName;
        this.mAuthor = mAuthor;
        this.mPhotoUrl = mPhotoUrl;
    }
}
