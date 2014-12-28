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
package com.cumulocity.me.rest.representation.alarm;

import com.cumulocity.me.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.representation.event.EventRepresentation;

public class AlarmRepresentation extends EventRepresentation {

    private String status;

    private String severity;

    private AuditRecordCollectionRepresentation history;
    
    public AlarmRepresentation() {
    }

    public String getStatus() {
        return status;
    }

    /**
     * Set the status of this alarm.
     * @param status Typical values are active, acknowledged or cleared
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    public String getSeverity() {
        return severity;
    }

    /**
     * Set the severity of this alarm. 
     * @param severity Typical values are: critical, major, minor and warning
     */
    public void setSeverity(final String severity) {
        this.severity = severity;
    }
    
    /**
     * Returns the audit history containing the changes made to this alarm. 
     * @return
     */
    public AuditRecordCollectionRepresentation getHistory() {
        return history;
    }

    public void setHistory(final AuditRecordCollectionRepresentation history) {
        this.history = history;
    }
}
