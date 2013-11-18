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

package com.cumulocity.sdk.client.event;

import java.util.Date;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.util.ExtensibilityConverter;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.Name;

/**
 * A filter to be used in event queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code EventFilter filter = new EventFilter().byType(type).bySource(source);}
 */
public class EventFilter extends Filter {

    @Name("fragmentType")
    private String fragmentType;

    @Name("dateFrom")
    private String fromDate;

    @Name("dateTo")
    private String toDate;

    @Name("type")
    private String type;

    @Name("source")
    private String source;
    
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
    public EventFilter bySource(GId id) {
        this.source = id.getValue();
        return this;
    }
    
    /**
     * Specifies the {@code source} query parameter
     *
     * @param source the managed object that generated the event(s)
     * @return the event filter with {@code source} set
     */
    @Deprecated
    public EventFilter bySource(ManagedObjectRepresentation source) {
        this.source = source.getId().getValue();
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
    public String getSource() {
        return source;
    }

    public EventFilter byFragmentType(Class<?> fragmentType) {
        this.fragmentType = ExtensibilityConverter.classToStringRepresentation(fragmentType);
        return this;
    }

    public String getFragmentType() {
        return fragmentType;
    }

    public EventFilter byDate(Date fromDate, Date toDate) {
        this.fromDate = DateConverter.date2String(fromDate);
        this.toDate = DateConverter.date2String(toDate);
        return this;
    }

    public EventFilter byFromDate(Date fromDate) {
        this.fromDate = DateConverter.date2String(fromDate);
        return this;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

}
