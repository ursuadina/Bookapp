package com.example.andreea.bookhunt.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultIDB implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSnippet);
        dest.writeString(mSource);
        dest.writeFloat(mRating);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ResultIDB createFromParcel(Parcel in) {
            return new ResultIDB(in);
        }

        public ResultIDB[] newArray(int size) {
            return new ResultIDB[size];
        }
    };

    public ResultIDB(Parcel in) {
        mSnippet = in.readString();
        mSource = in.readString();
        mRating = in.readFloat();
    }

}
