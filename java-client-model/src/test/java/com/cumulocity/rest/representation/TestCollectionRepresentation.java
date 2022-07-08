package com.cumulocity.rest.representation;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;

public class TestCollectionRepresentation<T> extends BaseCollectionRepresentation<T> {

    private List<T> contents;

    public TestCollectionRepresentation() {
    }

    public TestCollectionRepresentation(List<T> contents) {
        this.contents = contents;
    }

    @JSONProperty(ignore = true)
    public void setContents(List<T> contents) {
        this.contents = contents;
    }

    public List<T> getContents() {
        return contents;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<T> iterator() {
        return contents.iterator();
    }

    @JSONProperty(ignore = true)
    public TestCollectionRepresentation<T> withContents(List<T> contents) {
        this.contents = contents;
        return this;
    }

    @JSONProperty(ignore = true)
    public TestCollectionRepresentation<T> withNext(String next) {
        setNext(next);
        return this;
    }

}
