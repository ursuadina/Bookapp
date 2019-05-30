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

    @Element(required = false, name = "reviews_widget")
    private String reviews_widget;

    public BookGoodreads() {
    }

    public BookGoodreads(String id, String title, String reviews_widget) {
        this.id = id;
        this.title = title;
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

    public String getReviews_widget() {
        return reviews_widget;
    }

    public void setReviews_widget(String reviews_widget) {
        this.reviews_widget = reviews_widget;
    }
}
