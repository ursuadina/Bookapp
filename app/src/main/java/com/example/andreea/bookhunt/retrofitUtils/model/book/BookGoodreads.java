package com.example.andreea.bookhunt.retrofitUtils.model.book;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "book", strict = false)
public class BookGoodreads implements Serializable {
    @Element(required = false, name = "id")
    private String id;

    @Element(required = false, name = "title")
    private String title;

    @Element(required = false, name = "description")
    private String description;

    @Element(required = false, name = "average_rating")
    private float average_rating;

    @Element(required = false, name = "reviews_widget")
    private String reviews_widget;

    public BookGoodreads() {
    }

    public BookGoodreads(String id, String title, String description, float average_rating, String reviews_widget) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.average_rating = average_rating;
        this.reviews_widget = reviews_widget;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public String getReviews_widget() {
        return reviews_widget;
    }

    public void setReviews_widget(String reviews_widget) {
        this.reviews_widget = reviews_widget;
    }
}
