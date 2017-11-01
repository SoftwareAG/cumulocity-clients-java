package com.cumulocity.microservice.platform.api.annotation;

import com.cumulocity.microservice.platform.api.alarm.AlarmInternalApi;
import com.cumulocity.microservice.platform.api.audit.AuditRecordInternalApi;
import com.cumulocity.microservice.platform.api.cep.CepInternalApi;
import com.cumulocity.microservice.platform.api.devicecontrol.DeviceControlInternalApi;
import com.cumulocity.microservice.platform.api.event.EventInternalApi;
import com.cumulocity.microservice.platform.api.identity.IdentityInternalApi;
import com.cumulocity.microservice.platform.api.inventory.BinariesInternalApi;
import com.cumulocity.microservice.platform.api.inventory.InventoryInternalApi;
import com.cumulocity.microservice.platform.api.measurement.MeasurementInternalApi;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        AlarmInternalApi.class
        , AuditRecordInternalApi.class
        , CepInternalApi.class
        , DeviceControlInternalApi.class
        , EventInternalApi.class
        , IdentityInternalApi.class
        , BinariesInternalApi.class
        , InventoryInternalApi.class
        , MeasurementInternalApi.class
})
public class EnableMicroservicePlatformInternalApiConfiguration {

}
