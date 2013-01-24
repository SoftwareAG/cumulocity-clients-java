/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client;

import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.validate.RepresentationValidationService;
import com.cumulocity.me.sdk.client.alarm.AlarmApi;
import com.cumulocity.me.sdk.client.audit.AuditRecordApi;
import com.cumulocity.me.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.me.sdk.client.event.EventApi;
import com.cumulocity.me.sdk.client.identity.IdentityApi;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;
import com.cumulocity.me.sdk.client.measurement.MeasurementApi;

public interface Platform {

    InventoryApi getInventoryApi();

    MeasurementApi getMeasurementApi();

    AlarmApi getAlarmApi();

    IdentityApi getIdentityApi();

    DeviceControlApi getDeviceControlApi();

    EventApi getEventApi();

    AuditRecordApi getAuditRecordApi();

    void setConversionService(JsonConversionService conversionService);

    JsonConversionService getConversionService();

    void setValidationService(RepresentationValidationService validationService);

    RepresentationValidationService getValidationService();
}
