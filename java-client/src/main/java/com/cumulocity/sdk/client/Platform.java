/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.audit.AuditRecordApi;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;

public interface Platform {

    InventoryApi getInventoryApi();

    IdentityApi getIdentityApi();

    MeasurementApi getMeasurementApi();

    DeviceControlApi getDeviceControlApi();

    AlarmApi getAlarmApi();

    EventApi getEventApi();

    AuditRecordApi getAuditRecordApi();

}
