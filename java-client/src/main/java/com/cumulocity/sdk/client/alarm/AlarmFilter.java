/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.alarm;

import java.util.Date;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.event.EventFilter;

/**
 * A filter to be used in alarm queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code AlarmFilter filter = new AlarmFilter().byStatus(st).bySource(src);}
 */
public class AlarmFilter {

    private CumulocityAlarmStatuses status;

    private ManagedObjectRepresentation source;

    private Date fromDate;

    private Date toDate;

    /**
     * Specifies the {@code source} query parameter
     *
     * @param source the managed object that generated the alarm(s)
     * @return the alarm filter with {@code source} set
     */
    public AlarmFilter bySource(ManagedObjectRepresentation source) {
        this.source = source;
        return this;
    }

    /**
     * Specifies the {@code status} query parameter
     *
     * @param status status of the alarm(s)
     * @return the alarm filter with {@code status} set
     */
    public AlarmFilter byStatus(CumulocityAlarmStatuses status) {
        this.status = status;
        return this;
    }

    /**
     * @return the {@code source} parameter of the query
     */
    public ManagedObjectRepresentation getSource() {
        return source;
    }

    /**
     * @return the {@code status} parameter of the query
     */
    public CumulocityAlarmStatuses getStatus() {
        return status;
    }

    /**
     * Specifies the {@code fromDate} and {@code toDate} query parameters 
     * for query in a time range.
     *
     * @param fromDate the start date time of the range
     * @param toDate the end date time of the range
     * @return the alarm filter with {@code fromDate} and {@code toDate} set.
     */
    public AlarmFilter byDate(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        return this;
    }

    /**
     * Specifies the {@code fromDate} query parameter 
     * for querying all alarms from the specified date time.
     *
     * @param fromDate the date time from which all alarms to be returned.
     * @return the alarm filter with {@code fromDate} set
     */
    public AlarmFilter byFromDate(Date fromDate) {
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
