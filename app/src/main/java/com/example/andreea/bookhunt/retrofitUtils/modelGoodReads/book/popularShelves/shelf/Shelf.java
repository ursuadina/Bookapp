package com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.book.popularShelves.shelf;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "shelf", strict = false)
public class Shelf implements Serializable {
    @Attribute(name = "name", required = false)
    private String name;

    public Shelf(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shelf() {}
}
