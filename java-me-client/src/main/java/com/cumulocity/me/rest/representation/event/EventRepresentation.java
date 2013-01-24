package com.cumulocity.me.rest.representation.event;

import java.util.Date;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

/**
 * A Java Representation for the Media Type {@link EventMediaType#EVENT}. 
 */
public class EventRepresentation extends AbstractExtensibleRepresentation {

    private GId id;

    private String type;

    private Date time;

    private Date creationTime;

    private String text;

    private ManagedObjectRepresentation managedObject;

    public EventRepresentation() {
    }
 
    public GId getId() {
        return id;
    }

    public void setId(GId id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public ManagedObjectRepresentation getSource() {
        return managedObject;
    }

    public void setSource(ManagedObjectRepresentation managedObject) {
        this.managedObject = managedObject;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

}
