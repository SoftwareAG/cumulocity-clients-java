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

package com.cumulocity.sdk.client.measurement;

import java.util.Date;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.util.ExtensibilityConverter;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.ParamSource;

/**
 * A filter to be used in measurement queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code MeasurementFilter filter = new MeasurementFilter().byType(type).bySource(source);}
 */
public class MeasurementFilter extends Filter {

    @ParamSource
    private String fragmentType;

    @ParamSource
    private String valueFragmentType;

    @ParamSource
    private String valueFragmentSeries;

    @ParamSource
    private String dateFrom;

    @ParamSource
    private String dateTo;

    @ParamSource
    private String type;

    @ParamSource
    private String source;

    /**
     * Specifies the {@code type} query parameter
     *
     * @param type the type of the event(s)
     * @return the event filter with {@code type} set
     */
    public MeasurementFilter byType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Specifies the {@code source} query parameter
     *
     * @param id the managed object that generated the event(s)
     * @return the event filter with {@code source} set
     */
    public MeasurementFilter bySource(GId id) {
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
    public MeasurementFilter bySource(ManagedObjectRepresentation source) {
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

    public MeasurementFilter byFragmentType(Class<?> fragmentType) {
        this.fragmentType = ExtensibilityConverter.classToStringRepresentation(fragmentType);
        return this;
    }

    public MeasurementFilter byFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
        return this;
    }

    public String getFragmentType() {
        return fragmentType;
    }

    /**
     * Specify value fragment type. This is preferred over the parameter {@code fragmentType}, because working with
     * structured data and filtering via this parameter is lighter than filtering via {@code fragmentType}.
     *
     * @param valueFragmentType the value fragment type to filter.
     * @return the event filter with {@code valueFragmentType} set.
     * @see #byValueFragmentSeries(String)
     * @see #byValueFragmentTypeAndSeries(String, String)
     */
    public MeasurementFilter byValueFragmentType(String valueFragmentType) {
        this.valueFragmentType = valueFragmentType;
        return this;
    }

    /**
     * Specify value fragment series, usually use in conjunction with {@link #byValueFragmentType(String)}
     *
     * @param valueFragmentSeries value fragment series to filter.
     * @return the event filter with {@code valueFragmentSeries} set.
     * @see #byValueFragmentType(String)
     * @see #byValueFragmentTypeAndSeries(String, String)
     */
    public MeasurementFilter byValueFragmentSeries(String valueFragmentSeries) {
        this.valueFragmentSeries = valueFragmentSeries;
        return this;
    }

    /**
     * A short version combining of {@link #byValueFragmentType(String)} and {@link #byValueFragmentSeries(String)}.
     *
     * @param valueFragmentType value fragment type to filter, example: {@code c8y_TemperatureMeasurement}
     * @param valueFragmentSeries value fragment series to filter, example: {@code T}
     * @return the event filter with {@code valueFragmentType} and {@code valueFragmentSeries} set.
     */
    public MeasurementFilter byValueFragmentTypeAndSeries(String valueFragmentType, String valueFragmentSeries) {
        this.valueFragmentType = valueFragmentType;
        this.valueFragmentSeries = valueFragmentSeries;
        return this;
    }

    public String getValueFragmentType() {
        return valueFragmentType;
    }

    public String getValueFragmentSeries() {
        return valueFragmentSeries;
    }

    public MeasurementFilter byDate(Date fromDate, Date toDate) {
        this.dateFrom = DateConverter.date2String(fromDate);
        this.dateTo = DateConverter.date2String(toDate);
        return this;
    }

    public MeasurementFilter byFromDate(Date fromDate) {
        this.dateFrom = DateConverter.date2String(fromDate);
        return this;
    }

    public String getFromDate() {
        return dateFrom;
    }

    public String getToDate() {
        return dateTo;
    }

}
