package com.cumulocity.rest.representation.audit;

import java.util.Set;

import lombok.EqualsAndHashCode;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.event.EventRepresentation;

@EqualsAndHashCode(callSuper=true)
public class AuditRecordRepresentation extends EventRepresentation {

    private String user;

    private String application;

    @NotNull(operation = Command.CREATE)
    private String activity;

    private String severity;

    @Null(operation = Command.UPDATE)
    private Set<ChangeRepresentation> changes;

    @JSONProperty(ignoreIfNull = true)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JSONTypeHint(ChangeRepresentation.class)
    @JSONProperty(ignoreIfNull = true)
    public Set<ChangeRepresentation> getChanges() {
        return changes;
    }

    public void setChanges(Set<ChangeRepresentation> changes) {
        this.changes = changes;
    }

}
