package com.cumulocity.lpwan.device.registrant;

import com.cumulocity.lpwan.lns.connectivity.util.JsonSchema;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseDeviceRegistrationService implements DeviceRegistrant{
    DeviceRegistrantService deviceRegistrantService = new DeviceRegistrantService(this);
    public abstract JsonSchema getDeviceRegistrationMetadata();
    public abstract void createDefaultCallbacks();
    public ManagedObjectRepresentation registerNewDevice(DeviceRegisterProperties deviceRegisterProperties){
        ManagedObjectRepresentation mo = deviceRegistrantService.newRegisterRequest(deviceRegisterProperties);
        createDefaultCallbacks();
        return mo;
    }
}
