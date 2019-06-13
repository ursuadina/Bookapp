package com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.book.popularShelves;

import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.book.popularShelves.shelf.Shelf;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "popular_shelves", strict = false)
public class PopularShelves implements Serializable {
    @ElementList(required = false, name = "shelf", inline = true)
    List<Shelf> shelf;

    public PopularShelves(List<Shelf> shelf) {
        this.shelf = shelf;
    }

    public List<Shelf> getShelf() {
        return shelf;
    }

    public void setShelf(List<Shelf> shelf) {
        this.shelf = shelf;
    }

    public PopularShelves() {
    }
}
