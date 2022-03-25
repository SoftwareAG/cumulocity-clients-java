/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.rest;

import com.cumulocity.lpwan.exception.LpwanServiceException;
import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.lpwan.lns.instance.service.LnsInstanceService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
public class LnsInstanceController {

    @Autowired
    LnsInstanceService lnsInstanceService;

    @GetMapping(value = "/lns-instance", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(LnsInstance.PublicView.class)
    public @ResponseBody @NotNull Collection<LnsInstance> getAll() {
        return lnsInstanceService.getAll();
    }

    @GetMapping(value = "/lns-instance/{lnsInstanceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(LnsInstance.PublicView.class)
    public @ResponseBody @NotNull LnsInstance getByName(@PathVariable @NotBlank String lnsInstanceName) throws LpwanServiceException {
        return lnsInstanceService.getByName(lnsInstanceName);
    }

    @PostMapping(value = "/lns-instance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(LnsInstance.PublicView.class)
    public @ResponseBody @NotNull LnsInstance create(@RequestBody @NotNull LnsInstance lnsInstance) throws LpwanServiceException {
        return lnsInstanceService.create(lnsInstance);
    }

    @PutMapping(value = "/lns-instance/{existingLnsInstanceName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(LnsInstance.PublicView.class)
    public @ResponseBody @NotNull LnsInstance updateAnLnsInstance(@PathVariable @NotBlank String existingLnsInstanceName, @RequestBody @NotNull LnsInstance lnsInstanceToUpdate) throws LpwanServiceException {
        return lnsInstanceService.update(existingLnsInstanceName, lnsInstanceToUpdate);
    }

    @DeleteMapping(value = "/lns-instance/{lnsInstanceName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnLnsInstance(@PathVariable @NotNull String lnsInstanceName) throws LpwanServiceException {
        lnsInstanceService.delete(lnsInstanceName);
    }
}
