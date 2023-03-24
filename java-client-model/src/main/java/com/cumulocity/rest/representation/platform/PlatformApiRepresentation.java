package com.cumulocity.rest.representation.platform;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.rest.representation.tenant.TenantApiRepresentation;
import com.cumulocity.rest.representation.user.UsersApiRepresentation;

public class PlatformApiRepresentation extends AbstractExtensibleRepresentation {
	
	private InventoryRepresentation inventory;
	
	private IdentityRepresentation identity;
	
	private EventsApiRepresentation event;
	
	private MeasurementsApiRepresentation measurement;
	
	private AuditRecordsRepresentation audit;
	
	private AlarmsApiRepresentation alarm;
	
	private UsersApiRepresentation user;
	
	private DeviceControlRepresentation deviceControl;
	
	private TenantApiRepresentation tenant;

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

	public UsersApiRepresentation getUser() {
		return user;
	}

	public void setUser(UsersApiRepresentation user) {
		this.user = user;
	}

	public DeviceControlRepresentation getDeviceControl() {
		return deviceControl;
	}

	public void setDeviceControl(DeviceControlRepresentation deviceControl) {
		this.deviceControl = deviceControl;
	}

	public TenantApiRepresentation getTenant() {
		return tenant;
	}

	public void setTenant(TenantApiRepresentation tenant) {
		this.tenant = tenant;
	}
}
