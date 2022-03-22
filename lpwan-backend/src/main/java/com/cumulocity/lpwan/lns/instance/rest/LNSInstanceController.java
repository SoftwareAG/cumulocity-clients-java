package com.cumulocity.lpwan.lns.instance.rest;

import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.lpwan.lns.instance.service.LnsInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class LNSInstanceController {

    @Autowired
    LnsInstanceService lnsInstanceService;

    @PostMapping(value = "/lns-instance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createAnLnsInstance(@RequestBody @NotNull LnsInstance lnsInstance) throws Exception {
        lnsInstanceService.createLnsInstance(lnsInstance);
    }

    @GetMapping(value = "/lns-instance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<LnsInstance> getLnsInstances() throws Exception {
        return lnsInstanceService.getLnsInstances();
    }

    @GetMapping(value = "/lns-instance/{lnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LnsInstance getAnLnsInstance(@PathVariable String lnsInstanceName) throws Exception {
        return lnsInstanceService.getAnLnsInstance(lnsInstanceName);
    }

    @PutMapping(value = "/lns-instance/{lnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateAnLnsInstance(@PathVariable String existingLnsInstanceName, @RequestBody @NotNull LnsInstance lnsInstanceToUpdate) throws Exception {
        lnsInstanceService.updateAnLnsInstance(existingLnsInstanceName, lnsInstanceToUpdate);
    }

    @DeleteMapping(value = "/lns-instance/{lnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAnLnsInstance(@PathVariable String lnsInstanceName) throws Exception {
        lnsInstanceService.deleteAnLnsInstance(lnsInstanceName);
    }
}
