package com.example.andreea.bookhunt.retrofitUtils.model;


import com.example.andreea.bookhunt.retrofitUtils.model.book.BookGoodreads;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "GoodreadsResponse", strict = false)
public class GoodreadsResponse implements Serializable {
    @Element(name = "book")
    private BookGoodreads book;

    public BookGoodreads getBook() {
        return book;
    }

    public void setBook(BookGoodreads book) {
        this.book = book;
    }
}

