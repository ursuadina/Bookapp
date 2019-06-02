package com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB;

import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews.CriticReviews;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "book", strict = false)
public class BookIDB implements Serializable {

    @Element(required = false, name = "critic-reviews")
    private CriticReviews criticReviews;

    public BookIDB(CriticReviews criticReviews) {
        this.criticReviews = criticReviews;
    }

    public BookIDB() {
    }

    public CriticReviews getCriticReviews() {
        return criticReviews;
    }

    public void setCriticReviews(CriticReviews criticReviews) {
        this.criticReviews = criticReviews;
    }

}
