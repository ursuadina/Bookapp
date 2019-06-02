package com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews.criticReview;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "critic-review", strict = false)
public class CriticReview implements Serializable {
    @Element(required = false, name = "snippet")
    private String snippet;

    @Element(required = false, name = "source")
    private String source;

    @Element(required = false, name = "star-rating")
    private float starRating;

    public CriticReview() {
    }

    public CriticReview(String snippet, String source, float starRating) {
        this.snippet = snippet;
        this.source = source;
        this.starRating = starRating;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }

}

