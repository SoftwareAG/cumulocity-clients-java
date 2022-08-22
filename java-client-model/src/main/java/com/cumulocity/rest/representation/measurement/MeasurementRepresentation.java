package com.cumulocity.rest.representation.measurement;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.SourceableConverter;
import com.cumulocity.rest.representation.SourceableRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import java.util.Date;

import static com.cumulocity.model.util.DateTimeUtils.newLocal;

public class MeasurementRepresentation extends AbstractExtensibleRepresentation implements Cloneable, SourceableRepresentation {

    private GId id;

    private String type;

    private DateTime time;

    private ManagedObjectRepresentation source;

    private ExternalIDRepresentation externalSource;

    @JSONConverter(type = IDTypeConverter.class)
    public void setId(GId id) {
        this.id = id;
    }

    @JSONProperty(ignoreIfNull = true)
    public GId getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getType() {
        return type;
    }

    public void setExternalSource(ExternalIDRepresentation externalSource) {
        this.externalSource = externalSource;
    }

    @JSONProperty(ignoreIfNull = true)
    public ExternalIDRepresentation getExternalSource() {
        return externalSource;
    }

    @JSONProperty(value = "deprecated_Time", ignore = true)
    @Deprecated
    public Date getTime() {
        return time == null ? null : time.toDate();
    }

    @Deprecated
    public void setTime(Date time) {
        this.time = time == null ? null : newLocal(time);
    }

    @JSONProperty(value = "time", ignoreIfNull = true)
    @JSONConverter(type = DateTimeConverter.class)
    public DateTime getDateTime() {
        return time;
    }

    public void setDateTime(DateTime time) {
        this.time = time;
    }

    public void setSource(ManagedObjectRepresentation source) {
        this.source = source;
    }

    @JSONProperty(ignoreIfNull = true)
    @JSONConverter(type = SourceableConverter.class)
    public ManagedObjectRepresentation getSource() {
        return source;
    }

//    used in conversion to make sure not to do side effects
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
