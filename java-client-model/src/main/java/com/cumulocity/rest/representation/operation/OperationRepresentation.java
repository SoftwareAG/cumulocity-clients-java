package com.cumulocity.rest.representation.operation;

import com.cumulocity.rest.representation.connector.ConnectorReferenceRepresentation;
import lombok.*;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;

import java.util.Date;

import static com.cumulocity.model.util.DateTimeUtils.newLocal;


@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderMethodName = "aOperation")
public class OperationRepresentation extends AbstractExtensibleRepresentation {

    @Null(operation = { Command.CREATE })
    private GId id;
    
    @NotNull(operation = Command.CREATE)
    @Null(operation = Command.UPDATE)
    private GId deviceId;

    private String deviceName;

    @NotNull(operation = Command.UPDATE)
    @Null(operation = Command.CREATE)
    private String status;
    
    @Null(operation = Command.CREATE)
    private String failureReason;

    @Null(operation = { Command.CREATE, Command.UPDATE })
    private DateTime creationTime;

    @Null(operation = { Command.CREATE, Command.UPDATE })
    private ExternalIDCollectionRepresentation deviceExternalIDs;

    private Long bulkOperationId;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    @Null(operation = { Command.CREATE, Command.UPDATE })
    private DeliveryRepresentation delivery;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    @Null(operation = { Command.UPDATE })
    private ConnectorReferenceRepresentation connector;

    @JSONConverter(type = IDTypeConverter.class)
    @JSONProperty(ignoreIfNull = true)
    public GId getId() {
        return id;
    }

    public void setId(GId id) {
        this.id = id;
    }

    @JSONConverter(type = IDTypeConverter.class)
    @JSONProperty(ignoreIfNull = true)
    public GId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(GId deviceId) {
        this.deviceId = deviceId;
    }
    
    @JSONProperty(ignoreIfNull = true)
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    @JSONProperty(ignoreIfNull = true)
    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
    
    @JSONProperty(ignoreIfNull = true)
    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(final String failureReason) {
        this.failureReason = failureReason;
    }

    @JSONProperty(value = "deprecated_CreationTime", ignore = true)
    @Deprecated
    public Date getCreationTime() {
        return creationTime == null ? null : creationTime.toDate();
    }

    @Deprecated
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
    public ExternalIDCollectionRepresentation getDeviceExternalIDs() {
        return deviceExternalIDs;
    }

    public void setDeviceExternalIDs(ExternalIDCollectionRepresentation deviceExternalIDs) {
        this.deviceExternalIDs = deviceExternalIDs;
    }

    @JSONProperty(ignoreIfNull = true)
    public Long getBulkOperationId() {
        return bulkOperationId;
    }

    public void setBulkOperationId(Long bulkOperationId) {
        this.bulkOperationId = bulkOperationId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((deviceExternalIDs == null) ? 0 : deviceExternalIDs.hashCode());
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        result = prime * result + ((failureReason == null) ? 0 : failureReason.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((bulkOperationId == null) ? 0 : bulkOperationId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OperationRepresentation other = (OperationRepresentation) obj;
        if (creationTime == null) {
            if (other.creationTime != null)
                return false;
        } else if (!creationTime.equals(other.creationTime))
            return false;
        if (deviceExternalIDs == null) {
            if (other.deviceExternalIDs != null)
                return false;
        } else if (!deviceExternalIDs.equals(other.deviceExternalIDs))
            return false;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        if (failureReason == null) {
            if (other.failureReason != null)
                return false;
        } else if (!failureReason.equals(other.failureReason))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (bulkOperationId == null) {
            if (other.bulkOperationId != null)
                return false;
        } else if (!bulkOperationId.equals(other.status))
            return false;
        return true;
    }
}
