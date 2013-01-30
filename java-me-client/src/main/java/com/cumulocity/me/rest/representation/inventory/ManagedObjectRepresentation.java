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

//import org.svenson.JSONProperty;
//import org.svenson.converter.JSONConverter;

//import com.cumulocity.model.DateConverter;
//import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentationWithId;

/**
 * A Java Representation for the MediaType ManagedObject
 *
 */

public class ManagedObjectRepresentation extends AbstractExtensibleRepresentation
        implements BaseCumulocityResourceRepresentationWithId {
    
    private GId id;

    private String type;

    private String name;

    private Date lastUpdated;

    private ManagedObjectReferenceCollectionRepresentation childDevices;

    private ManagedObjectReferenceCollectionRepresentation childAssets;

    private ManagedObjectReferenceCollectionRepresentation parents;

    /**
     * Default constructor is needed for reflection based class instantiation.
     */
    public ManagedObjectRepresentation() {
    }
    
//    @JSONConverter(type = IDTypeConverter.class)
//    @JSONProperty(ignoreIfNull = true)
    public GId getId() {
        return id;
    }

    public void setId(GId id) {
        this.id = id;
    }
    
//    @JSONProperty(ignoreIfNull = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
//    @JSONProperty(ignoreIfNull = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @JSONProperty(value = "lastUpdated", ignoreIfNull = true)
//    @JSONConverter(type = DateConverter.class)
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdate) {
        this.lastUpdated = lastUpdate;
    }

//    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(ManagedObjectReferenceCollectionRepresentation childDevices) {
        this.childDevices = childDevices;
    }
    
//    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getChildAssets() {
        return childAssets;
    }

    public void setChildAssets(ManagedObjectReferenceCollectionRepresentation childAssets) {
        this.childAssets = childAssets;
    }

//    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getParents() {
        return parents;
    }

    public void setParents(ManagedObjectReferenceCollectionRepresentation parents) {
        this.parents = parents;
    }

}
