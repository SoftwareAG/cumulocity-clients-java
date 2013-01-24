package com.cumulocity.me.rest.representation.platform;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.me.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.me.rest.representation.operation.DeviceControlRepresentation;

public class PlatformApiRepresentation extends BaseCumulocityResourceRepresentation {
	
	private InventoryRepresentation inventory;
	
	private IdentityRepresentation identity;
	
	private EventsApiRepresentation event;
	
	private MeasurementsApiRepresentation measurement;
	
	private AuditRecordsRepresentation audit;
	
	private AlarmsApiRepresentation alarm;
	
//	private UsersApiRepresentation user;
	
	private DeviceControlRepresentation deviceControl;

	public InventoryRepresentation getInventory() {
		return inventory;
	}

	public void setInventory(InventoryRepresentation inventory) {
		this.inventory = inventory;
	}

	public IdentityRepresentation getIdentity() {
		return identity;
	}

	public void setIdentity(IdentityRepresentation identity) {
		this.identity = identity;
	}

	public EventsApiRepresentation getEvent() {
		return event;
	}

	public void setEvent(EventsApiRepresentation event) {
		this.event = event;
	}

	public MeasurementsApiRepresentation getMeasurement() {
		return measurement;
	}

	public void setMeasurement(MeasurementsApiRepresentation measurement) {
		this.measurement = measurement;
	}

	public AuditRecordsRepresentation getAudit() {
		return audit;
	}

	public void setAudit(AuditRecordsRepresentation audit) {
		this.audit = audit;
	}

	public AlarmsApiRepresentation getAlarm() {
		return alarm;
	}

	public void setAlarm(AlarmsApiRepresentation alarm) {
		this.alarm = alarm;
	}

//	public UsersApiRepresentation getUser() {
//		return user;
//	}
//
//	public void setUser(UsersApiRepresentation user) {
//		this.user = user;
//	}

	public DeviceControlRepresentation getDeviceControl() {
		return deviceControl;
	}

	public void setDeviceControl(DeviceControlRepresentation deviceControl) {
		this.deviceControl = deviceControl;
	}
}
