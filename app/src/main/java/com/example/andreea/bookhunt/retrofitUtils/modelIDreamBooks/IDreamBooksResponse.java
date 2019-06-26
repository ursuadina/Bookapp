package com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks;

import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.BookIDB;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="hash", strict = false)
public class IDreamBooksResponse implements Serializable {
    @Element(required = false, name="total-results")
    private Integer totalResults;
    @Element(name = "book")
    private BookIDB bookIDB;

    public BookIDB getBookIDB() {
        return bookIDB;
    }

    public void setBookIDB(BookIDB bookIDB) {
        this.bookIDB = bookIDB;
    }

    public IDreamBooksResponse(BookIDB bookIDB) {
        this.bookIDB = bookIDB;
    }

    public IDreamBooksResponse() {
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
