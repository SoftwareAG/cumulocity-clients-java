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
