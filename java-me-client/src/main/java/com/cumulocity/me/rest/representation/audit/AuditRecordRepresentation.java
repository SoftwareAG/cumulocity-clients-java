package com.cumulocity.me.rest.representation.audit;

import com.cumulocity.me.lang.Set;
import com.cumulocity.me.rest.representation.event.EventRepresentation;

public class AuditRecordRepresentation extends EventRepresentation {

    private String user;

    private String application;

    private String activity;

    private String severity;

    private Set changes;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Set getChanges() {
        return changes;
    }

    public void setChanges(Set changes) {
        this.changes = changes;
    }

}
