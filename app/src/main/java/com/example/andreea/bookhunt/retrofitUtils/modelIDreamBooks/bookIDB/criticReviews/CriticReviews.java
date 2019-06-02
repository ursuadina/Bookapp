package com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews;

import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews.criticReview.CriticReview;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "critic-reviews", strict = false)
public class CriticReviews implements Serializable {
    @ElementList(required = false, name = "critic-review", inline = true)
    private List<CriticReview> criticReview;

    public CriticReviews() {
    }

    public CriticReviews(List<CriticReview> criticReview) {
        this.criticReview = criticReview;
    }

    public List<CriticReview> getCriticReview() {
        return criticReview;
    }

    public void setCriticReview(List<CriticReview> criticReview) {
        this.criticReview = criticReview;
    }
}
