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
package com.cumulocity.me.rest.representation.platform;

import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.me.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.me.rest.representation.operation.DeviceControlRepresentation;

public class PlatformApiRepresentation extends BaseResourceRepresentation {
	
	private InventoryRepresentation inventory;
	
	private IdentityRepresentation identity;
	
	private EventsApiRepresentation event;
	
	private MeasurementsApiRepresentation measurement;
	
	private AuditRecordsRepresentation audit;
	
	private AlarmsApiRepresentation alarm;
	
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

	public DeviceControlRepresentation getDeviceControl() {
		return deviceControl;
	}

	public void setDeviceControl(DeviceControlRepresentation deviceControl) {
		this.deviceControl = deviceControl;
	}
}
