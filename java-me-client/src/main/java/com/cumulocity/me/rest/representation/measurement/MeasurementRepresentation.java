package com.cumulocity.me.rest.representation.measurement;

import java.util.Date;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class MeasurementRepresentation extends AbstractExtensibleRepresentation {

    private GId id;

    private String type;

    private Date time;

    private ManagedObjectRepresentation source;

    public void setId(GId id) {
        this.id = id;
    }

    public GId getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setSource(ManagedObjectRepresentation source) {
        this.source = source;
    }

    public ManagedObjectRepresentation getSource() {
        return source;
    }
}
