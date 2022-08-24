package com.cumulocity.rest.representation.inventory;

import com.cumulocity.rest.representation.ResourceRepresentationWithId;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.OwnerSource;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

import java.util.Date;

import static com.cumulocity.model.util.DateTimeUtils.newLocal;

/**
 * A Java Representation for the MediaType ManagedObject
 *
 */

public class ManagedObjectRepresentation extends AbstractExtensibleRepresentation
        implements ResourceRepresentationWithId,OwnerSource {

    private GId id;

    private String type;

    private String name;

    private DateTime lastUpdated;

    private DateTime creationTime;

    private String owner;

    private ManagedObjectReferenceCollectionRepresentation childDevices;

    private ManagedObjectReferenceCollectionRepresentation childAssets;

    private ManagedObjectReferenceCollectionRepresentation childAdditions;

    private ManagedObjectReferenceCollectionRepresentation deviceParents;

    private ManagedObjectReferenceCollectionRepresentation assetParents;

    private ManagedObjectReferenceCollectionRepresentation additionParents;

    /**
     * Default constructor is needed for reflection based class instantiation.
     */
    public ManagedObjectRepresentation() {
    }

    @JSONConverter(type = IDTypeConverter.class)
    @JSONProperty(ignoreIfNull = true)
    public GId getId() {
        return id;
    }

    public void setId(GId id) {
        this.id = id;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONProperty(value = "deprecated_LastUpdated", ignore = true)
    @Deprecated
    public Date getLastUpdated() {
        return lastUpdated == null ? null : lastUpdated.toDate();
    }

    @Deprecated
    public void setLastUpdated(Date lastUpdate) {
        this.lastUpdated = lastUpdate == null ? null : newLocal(lastUpdate);
    }

    @JSONProperty(value = "lastUpdated", ignoreIfNull = true)
    @JSONConverter(type = DateTimeConverter.class)
    public DateTime getLastUpdatedDateTime() {
        return lastUpdated;
    }

    public void setLastUpdatedDateTime(DateTime lastUpdate) {
        this.lastUpdated = lastUpdate;
    }

    @JSONProperty(value = "deprecated_CreationTime", ignore = true)
    @Deprecated
    public Date getCreationTime() {
        return creationTime == null ? null : creationTime.toDate();
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime == null ? null : newLocal(creationTime);
    }

    @JSONProperty(value = "creationTime", ignoreIfNull = true)
    @JSONConverter(type = DateTimeConverter.class)
    public DateTime getCreationDateTime() {
        return creationTime;
    }

    public void setCreationDateTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getOwner() {
		return owner;
	}

    public void setOwner(String owner) {
		this.owner = owner;
	}

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(ManagedObjectReferenceCollectionRepresentation childDevices) {
        this.childDevices = childDevices;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getChildAssets() {
        return childAssets;
    }

    public void setChildAssets(ManagedObjectReferenceCollectionRepresentation childAssets) {
        this.childAssets = childAssets;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getChildAdditions() {
        return childAdditions;
    }

    public void setChildAdditions(ManagedObjectReferenceCollectionRepresentation childAdditions) {
        this.childAdditions = childAdditions;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getDeviceParents() {
        return deviceParents;
    }

    public void setDeviceParents(ManagedObjectReferenceCollectionRepresentation deviceParents) {
        this.deviceParents = deviceParents;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getAdditionParents() {
        return additionParents;
    }

    public void setAdditionParents(ManagedObjectReferenceCollectionRepresentation additionParents) {
        this.additionParents = additionParents;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectReferenceCollectionRepresentation getAssetParents() {
        return assetParents;
    }

    public void setAssetParents(ManagedObjectReferenceCollectionRepresentation assetParents) {
        this.assetParents = assetParents;
    }

}
