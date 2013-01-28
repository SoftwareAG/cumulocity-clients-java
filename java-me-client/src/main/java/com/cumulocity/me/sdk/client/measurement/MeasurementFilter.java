/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cumulocity.me.sdk.client.measurement;

import java.util.Date;

import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

/**
 * A filter to be used in measurement queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code MeasurementFilter filter = new MeasurementFilter().byType(type).bySource(source);}
 */
public class MeasurementFilter {

    private String type;

    private ManagedObjectRepresentation source;

    private Class fragmentType;

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
    public MeasurementFilter byFragmentType(Class fragmentType) {
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
    public Class getFragmentType() {
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
