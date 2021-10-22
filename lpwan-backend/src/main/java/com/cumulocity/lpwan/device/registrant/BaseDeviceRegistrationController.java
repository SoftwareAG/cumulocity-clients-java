package com.cumulocity.lpwan.device.registrant;

import com.cumulocity.lpwan.lns.connectivity.Service.BaseLnsCredentialsService;
import com.cumulocity.lpwan.lns.connectivity.util.JsonSchema;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BaseDeviceRegistrationController {

    @Autowired
    BaseDeviceRegistrationService deviceRegistrationService;

    @GetMapping(value = "/deviceRegistrationMetadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonSchema getMetaData() {
        return deviceRegistrationService.getDeviceRegistrationMetadata();
    }

    @PostMapping(value = "/newDeviceRegistration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ManagedObjectRepresentation newDeviceRegistration(@RequestBody DeviceRegisterProperties deviceRegisterProperties) {
        return deviceRegistrationService.registerNewDevice(deviceRegisterProperties);
    }
}
