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
package com.cumulocity.me.rest.representation.inventory;

import java.util.Date;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.me.rest.representation.BaseResourceRepresentationWithId;

/**
 * A Java Representation for the MediaType ManagedObject
 *
 */

public class ManagedObjectRepresentation extends AbstractExtensibleRepresentation
        implements BaseResourceRepresentationWithId {
    
    private GId id;

    private String type;

    private String name;
    
    private String owner;

    private Date lastUpdated;

    private ManagedObjectReferenceCollectionRepresentation childDevices;

    private ManagedObjectReferenceCollectionRepresentation childAssets;

    private ManagedObjectReferenceCollectionRepresentation parents;

    /**
     * Default constructor is needed for reflection based class instantiation.
     */
    public ManagedObjectRepresentation() {
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
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdate) {
        this.lastUpdated = lastUpdate;
    }

    public ManagedObjectReferenceCollectionRepresentation getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(ManagedObjectReferenceCollectionRepresentation childDevices) {
        this.childDevices = childDevices;
    }
    
    public ManagedObjectReferenceCollectionRepresentation getChildAssets() {
        return childAssets;
    }

    public void setChildAssets(ManagedObjectReferenceCollectionRepresentation childAssets) {
        this.childAssets = childAssets;
    }

    public ManagedObjectReferenceCollectionRepresentation getParents() {
        return parents;
    }

    public void setParents(ManagedObjectReferenceCollectionRepresentation parents) {
        this.parents = parents;
    }

}
