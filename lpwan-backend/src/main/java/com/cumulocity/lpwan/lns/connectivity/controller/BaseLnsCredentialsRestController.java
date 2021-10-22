package com.cumulocity.lpwan.lns.connectivity.controller;

import com.cumulocity.lpwan.lns.connectivity.Service.BaseLnsCredentialsService;
import com.cumulocity.lpwan.lns.connectivity.util.JsonSchema;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BaseLnsCredentialsRestController {

    @Autowired
    BaseLnsCredentialsService lnsConnectivityService;

    @GetMapping(value = "/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonSchema getMetaData() {
        return lnsConnectivityService.getMetaData();
    }

    @PostMapping(value = "/saveAndValidateCredentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> saveCredentials(@RequestBody Map<String,String> connectivitySettingData) {
        return lnsConnectivityService.isValidSession(connectivitySettingData);
    }

    @GetMapping(value = "/getConnectivitySettings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> getCredentials() {
        return lnsConnectivityService.getCredentials();
    }
}
