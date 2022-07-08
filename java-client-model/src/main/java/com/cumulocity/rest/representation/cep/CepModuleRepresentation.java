package com.cumulocity.rest.representation.cep;

import static com.cumulocity.model.util.DateTimeUtils.newUTC;
import static com.cumulocity.rest.representation.annotation.Command.CREATE;
import static com.cumulocity.rest.representation.annotation.Command.UPDATE;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;

@SuppressWarnings("rawtypes")
@NoArgsConstructor
public class CepModuleRepresentation extends AbstractExtensibleRepresentation {

    @Null(operation = { CREATE })
    private String id;

    @NotNull(operation = { CREATE })
    private String name;

    @Null(operation = { CREATE, UPDATE })
    private DateTime lastModified;

    @Null(operation = { CREATE })
    private String status;
    
    private List statements = new LinkedList();

    private String fileRepresentation;

    @Builder(builderMethodName = "cepModuleRepresentation")
    public CepModuleRepresentation(String id, String name, DateTime lastModified, String status, List statements, String fileRepresentation) {
        this.id = id;
        this.name = name;
        this.lastModified = lastModified;
        this.status = status;
        this.statements = statements == null ? new LinkedList() : statements;
        this.fileRepresentation = fileRepresentation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONProperty(value = "deprecated_LastModified", ignore = true)
    @Deprecated
    public Date getLastModified() {
        return lastModified == null ? null : lastModified.toDate();
    }

    @Deprecated
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified == null ? null : newUTC(lastModified);
    }

    @JSONProperty(value = "lastModified", ignoreIfNull = true)
    @JSONConverter(type = DateTimeConverter.class)
    public DateTime getLastModifiedDateTime() {
        return lastModified;
    }

    public void setLastModifiedDateTime(DateTime lastModified) {
        this.lastModified = lastModified;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JSONProperty(ignore = true)
    public List getStatements() {
        return statements;
    }

    public void setStatements(List statements) {
        this.statements = statements;
    }

    @JSONProperty(ignore = true)
    public String getFileRepresentation() {
        return fileRepresentation;
    }

    public void setFileRepresentation(String fileRepresentation) {
        this.fileRepresentation = fileRepresentation;
    }
}
