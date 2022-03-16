package com.cumulocity.lpwan.connectivity.rest;

import com.cumulocity.lpwan.connectivity.service.LnsInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
public class LNSInstanceController {

    @Autowired
    LnsInstanceService lnsInstanceService;

    @PostMapping(value = "/lns-instance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createAnLnsInstance(@RequestBody @NotNull Map<String, Map<String, String>> lnsInstance) {
        lnsInstanceService.createLnsInstance(lnsInstance);
    }

    @GetMapping(value = "/lns-instance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map<String, String>> getLnsInstances(){
        return lnsInstanceService.getLnsInstances();
    }

    @GetMapping(value = "/lns-instance/{lnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map<String, String>> getAnLnsInstance(@PathVariable String lnsInstanceName){
        return lnsInstanceService.getAnLnsInstance(lnsInstanceName);
    }

    @PutMapping(value = "/lns-instance/{lnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateAnLnsInstance(@PathVariable String existingLnsInstanceName, @RequestBody @NotNull Map<String, Map<String, String>> lnsInstanceToUpdate){
        lnsInstanceService.updateAnLnsInstance(existingLnsInstanceName, lnsInstanceToUpdate);
    }

    @DeleteMapping(value = "/lns-instance/{lnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAnLnsInstance(@PathVariable String lnsInstanceName){
        lnsInstanceService.deleteAnLnsInstance(lnsInstanceName);
    }
}
