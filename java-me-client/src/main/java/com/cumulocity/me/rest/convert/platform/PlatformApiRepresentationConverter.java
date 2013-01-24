package com.cumulocity.me.rest.convert.platform;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.me.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.me.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;

public class PlatformApiRepresentationConverter extends BaseResourceRepresentationConverter {

    protected Class supportedRepresentationType() {
        return PlatformApiRepresentation.class;
    }
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }
    
    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setSelf(getString(json, "self"));
        $(representation).setInventory((InventoryRepresentation) getObject(json, "inventory", InventoryRepresentation.class));
        $(representation).setMeasurement((MeasurementsApiRepresentation) getObject(json, "measurement", MeasurementsApiRepresentation.class));
        $(representation).setAlarm((AlarmsApiRepresentation) getObject(json, "alarm", AlarmsApiRepresentation.class));
        $(representation).setAudit((AuditRecordsRepresentation) getObject(json, "audit", AuditRecordsRepresentation.class));
        $(representation).setDeviceControl((DeviceControlRepresentation) getObject(json, "deviceControl", DeviceControlRepresentation.class));
        $(representation).setEvent((EventsApiRepresentation) getObject(json, "event", EventsApiRepresentation.class));
        $(representation).setIdentity((IdentityRepresentation) getObject(json, "identity", IdentityRepresentation.class));
    }

    private PlatformApiRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (PlatformApiRepresentation) baseRepresentation;
    }
}
