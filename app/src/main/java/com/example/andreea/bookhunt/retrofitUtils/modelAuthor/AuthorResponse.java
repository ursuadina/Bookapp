package com.example.andreea.bookhunt.retrofitUtils.modelAuthor;

import com.example.andreea.bookhunt.retrofitUtils.modelAuthor.author.Author;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.book.BookGoodreads;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GoodreadsResponse", strict = false)
public class AuthorResponse {
    @Element(name = "author")
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
