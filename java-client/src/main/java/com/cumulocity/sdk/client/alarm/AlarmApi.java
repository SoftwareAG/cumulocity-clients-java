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

package com.cumulocity.sdk.client.alarm;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;

/**
 * API for creating, updating and retrieving alarms from the platform.
 */
public interface AlarmApi {

    /**
     * Gets an alarm by id
     *
     * @param gid id of the alarm to search for
     * @return the alarm with the given id
     * @throws SDKException if the alarm is not found or if the query failed
     */
    AlarmRepresentation getAlarm(GId gid) throws SDKException;

    /**
     * Creates an alarm in the platform. The id of the alarm must not be set, since it will be generated by the platform
     *
     * @param alarm alarm to be created
     * @return the created alarm with the generated id
     * @throws SDKException if the alarm could not be created
     */
    AlarmRepresentation create(AlarmRepresentation alarm) throws SDKException;

    /**
     * Creates an alarm in the platform. Immediate response is available through the Future object. 
     * In case of lost connection, buffers data in persistence provider. 
     *
     * @param alarm alarm to be created
     * @return the created alarm with the generated id
     * @throws SDKException if the alarm could not be created
     */
    Future createAsync(AlarmRepresentation alarm) throws SDKException;
    
    /**
     * Updates an alarm in the platform.
     * The alarm to be updated is identified by the id within the given alarm.
     *
     * @param alarm to be updated
     * @return the updated alarm
     * @throws SDKException if the alarm could not be updated
     */
    AlarmRepresentation update(AlarmRepresentation alarm) throws SDKException;

    /**
     * Gets all alarms from the platform
     *
     * @return collection of alarms with paging functionality
     * @throws SDKException if the query failed
     */
    AlarmCollection getAlarms() throws SDKException;

    /**
     * Gets alarms from the platform based on the specified filter
     *
     * @param filter the filter criteria(s)
     * @return collection of alarms matched by the filter with paging functionality
     * @throws SDKException if the query failed
     */
    AlarmCollection getAlarmsByFilter(AlarmFilter filter) throws SDKException;

    /**
     * Delete alarms from the platform based on the specified filter
     *
     * @param filter the filter criteria(s)
     * @throws SDKException if the query failed
     */
    void deleteAlarmsByFilter(AlarmFilter filter) throws IllegalArgumentException, SDKException;
    
    /**
     * Updates an alarm in the platform.
     * The alarm to be updated is identified by the id within the given alarm.
     *
     * @param alarm to be updated
     * @return the updated alarm
     * @throws SDKException if the alarm could not be updated
     */
    @Deprecated
    AlarmRepresentation updateAlarm(AlarmRepresentation alarm) throws SDKException;
}
