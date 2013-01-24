/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.event;

import java.util.Date;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

/**
 * A filter to be used in event queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code EventFilter filter = new EventFilter().byType(type).bySource(source);}
 */
public class EventFilter {

    private Class<?> fragmentType;

    private Date fromDate;

    private Date toDate;

    private String type;

    private ManagedObjectRepresentation source;

    /**
     * Specifies the {@code type} query parameter
     *
     * @param type the type of the event(s)
     * @return the event filter with {@code type} set
     */
    public EventFilter byType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Specifies the {@code source} query parameter
     *
     * @param source the managed object that generated the event(s)
     * @return the event filter with {@code source} set
     */
    public EventFilter bySource(ManagedObjectRepresentation source) {
        this.source = source;
        return this;
    }

    /**
     * @return the {@code type} parameter of the query
     */
    public String getType() {
        return type;
    }

    /**
     * @return the {@code source} parameter of the query
     */
    public ManagedObjectRepresentation getSource() {
        return source;
    }

    public EventFilter byFragmentType(Class<?> fragmentType) {
        this.fragmentType = fragmentType;
        return this;
    }

    public Class<?> getFragmentType() {
        return fragmentType;
    }

    public EventFilter byDate(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        return this;
    }

    public EventFilter byFromDate(Date fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

}
