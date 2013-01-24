/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.measurement;

import java.util.Date;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

/**
 * A filter to be used in measurement queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code MeasurementFilter filter = new MeasurementFilter().byType(type).bySource(source);}
 */
public class MeasurementFilter {

    private String type;

    private ManagedObjectRepresentation source;

    private Class<?> fragmentType;

    private Date fromDate;

    private Date toDate;

    /**
     * Specifies the {@code type} query parameter
     *
     * @param type the type of the measurement(s)
     * @return the measurement filter with {@code type} set
     */
    public MeasurementFilter byType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Specifies the {@code source} query parameter
     *
     * @param source the type of the measurement(s)
     * @return the measurement filter with {@code source} set
     */
    public MeasurementFilter bySource(ManagedObjectRepresentation source) {
        this.source = source;
        return this;
    }

    /**
     * Specifies the {@code fragmentType} query parameter
     *
     * @param fragmentType the type of the measurement(s)
     * @return the measurement filter with {@code fragment} set
     */
    public MeasurementFilter byFragmentType(Class<?> fragmentType) {
        this.fragmentType = fragmentType;
        return this;
    }

    /**
     * Specifies the {@code fromDate} and {@code toDate} query parameters
     *
     * @param fromDate the lower bound of the date interval
     * @param toDate   the upper bound of the date interval
     * @return the measurement filter with {@code fromDate} and {@code toDate} set
     */
    public MeasurementFilter byDate(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    /**
     * @return the {@code fragmentType} parameter of the query
     */
    public Class<?> getFragmentType() {
        return fragmentType;
    }

    /**
     * @return the {@code fromDate} parameter of the query
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @return the {@code toDate} parameter of the query
     */
    public Date getToDate() {
        return toDate;
    }
}
