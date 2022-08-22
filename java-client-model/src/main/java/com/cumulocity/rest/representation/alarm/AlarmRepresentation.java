package com.cumulocity.rest.representation.alarm;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import java.util.Date;

import static com.cumulocity.model.util.DateTimeUtils.newLocal;

@EqualsAndHashCode(callSuper = true)
public class AlarmRepresentation extends EventRepresentation {

    private String status;

    private String severity;

    private AuditRecordCollectionRepresentation history;

    private Long count;

    private DateTime firstOccurrenceTime;

    public AlarmRepresentation() {
    }

    @JSONProperty(ignoreIfNull = true)
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of this alarm.
     *
     * @param status Typical values are active, acknowledged or cleared
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getSeverity() {
        return severity;
    }

    /**
     * Set the severity of this alarm.
     *
     * @param severity Typical values are: critical, major, minor and warning
     */
    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    /**
     * Severity ordinal property is internally used by platform.
     *
     * @return
     */
    @JSONProperty(ignoreIfNull = true)
    @Deprecated
    public Long getSeverityOrdinal() {
        return null;
    }

    /**
     * Severity ordinal property is internally used by platform and can't be overridden.
     *
     * @param severityOrdinal
     */

    @Deprecated
    public void setSeverityOrdinal(Long severityOrdinal) {

    }

    /**
     * Status ordinal property is internally used by platform.
     *
     * @return
     */
    @JSONProperty(ignoreIfNull = true)
    @Deprecated
    public Long getStatusOrdinal() {
        return null;
    }

    /**
     * Status ordinal property is internally used by platform and can't be overridden.
     *
     * @param statusOrdinal
     */
    @Deprecated
    public void setStatusOrdinal(Long statusOrdinal) {

    }

    /**
     * Kept for backwards compatibility in API. Should not be used.
     * Always empty while reading from DB, and always skipped while saving
     */
    @Deprecated
    @JSONProperty(ignoreIfNull = true)
    public AuditRecordCollectionRepresentation getHistory() {
        return history;
    }

    public void setHistory(final AuditRecordCollectionRepresentation history) {
        this.history = history;
    }

    @JSONProperty(ignoreIfNull = true)
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @JSONProperty(value = "deprecated_FirstOccurrenceTime", ignore = true)
    @Deprecated
    public Date getFirstOccurrenceTime() {
        return firstOccurrenceTime == null ? null : firstOccurrenceTime.toDate();
    }

    @Deprecated
    public void setFirstOccurrenceTime(Date firstOccurenceTime) {
        this.firstOccurrenceTime = firstOccurenceTime == null ? null : newLocal(firstOccurenceTime);
    }

    @JSONProperty(value = "firstOccurrenceTime", ignoreIfNull = true)
    @JSONConverter(type = DateTimeConverter.class)
    public DateTime getFirstOccurrenceDateTime() {
        return firstOccurrenceTime;
    }

    public void setFirstOccurrenceDateTime(DateTime firstOccurenceTime) {
        this.firstOccurrenceTime = firstOccurenceTime;
    }
}
