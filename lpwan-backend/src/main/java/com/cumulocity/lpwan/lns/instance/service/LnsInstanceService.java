/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.service;

import com.cumulocity.lpwan.lns.instance.exception.InvalidInputDataException;
import com.cumulocity.lpwan.lns.instance.exception.LnsInstanceServiceException;
import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.lpwan.lns.instance.model.LnsInstanceDeserializer;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@TenantScope
public class LnsInstanceService {

    private static final String LNS_INSTANCE_MAP_KEY = "credentials.lns.instance.map";

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Autowired
    private TenantOptionApi tenantOptionApi;

    private OptionPK tenantOptionKeyForLnsInstanceMap;

    private Map<String, LnsInstance> cachedLnsInstances = new ConcurrentHashMap<>();

    public @NotNull LnsInstance getByName(@NotBlank String lnsInstanceName) throws LnsInstanceServiceException {
        if (StringUtils.isBlank(lnsInstanceName)) {
            String message = "LNS instance name can't be null or blank.";
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        if (!cachedLnsInstances.containsKey(lnsInstanceName)) {
            String message = String.format("LNS instance named '%s' doesn't exist.", lnsInstanceName);
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        return cachedLnsInstances.get(lnsInstanceName);
    }

    public Collection<LnsInstance> getAll() {
        return cachedLnsInstances.values();
    }

    public synchronized @NotNull LnsInstance create(@NotNull LnsInstance newLnsInstance) throws LnsInstanceServiceException {
        if (newLnsInstance == null) {
            String message = "New LNS instance can't be null.";
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        newLnsInstance.isValid();

        String newLnsInstanceName = newLnsInstance.getName();
        if (cachedLnsInstances.containsKey(newLnsInstanceName)) {
            String message = String.format("LNS instance named '%s' already exists.", newLnsInstanceName);
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        cachedLnsInstances.put(newLnsInstanceName, newLnsInstance);

        flushCache();

        log.info("New LNS instance named '{}' is created.", newLnsInstanceName);

        return newLnsInstance;
    }

    public synchronized @NotNull LnsInstance update(@NotBlank String existingLnsInstanceName, @NotNull LnsInstance lnsInstanceToUpdate) throws LnsInstanceServiceException {
        if (StringUtils.isBlank(existingLnsInstanceName)) {
            String message = "Existing LNS instance name can't be null or blank.";
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        if (!cachedLnsInstances.containsKey(existingLnsInstanceName)) {
            String message = String.format("LNS instance named '%s' doesn't exist.", existingLnsInstanceName);
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        if (lnsInstanceToUpdate == null) {
            String message = "LNS instance to update can't be null.";
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        lnsInstanceToUpdate.isValid();

        String updatedLnsInstanceName = lnsInstanceToUpdate.getName();
        if (!existingLnsInstanceName.equals(updatedLnsInstanceName)) {
            if (cachedLnsInstances.containsKey(updatedLnsInstanceName)) {
                String message = String.format("LNS instance named '%s' already exists.", updatedLnsInstanceName);
                log.error(message);
                throw new InvalidInputDataException(message);
            }
        }

        LnsInstance existingLnsInstance = cachedLnsInstances.remove(existingLnsInstanceName);
        existingLnsInstance.initializeWith(lnsInstanceToUpdate);

        cachedLnsInstances.put(updatedLnsInstanceName, existingLnsInstance);

        flushCache();

        if (!existingLnsInstanceName.equals(updatedLnsInstanceName)) {
            log.info("LNS instance named '{}', is renamed to '{}' and updated.", existingLnsInstanceName, updatedLnsInstanceName);
        } else {
            log.info("LNS instance named '{}' is updated.", existingLnsInstanceName);
        }

        return existingLnsInstance;
    }

    public synchronized void delete(@NotBlank String lnsInstanceNametoDelete) throws LnsInstanceServiceException {
        if (StringUtils.isBlank(lnsInstanceNametoDelete)) {
            String message = "LNS instance name to delete can't be null or blank.";
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        if (!cachedLnsInstances.containsKey(lnsInstanceNametoDelete)) {
            String message = String.format("LNS instance named '%s' doesn't exist.", lnsInstanceNametoDelete);
            log.error(message);
            throw new InvalidInputDataException(message);
        }

        cachedLnsInstances.remove(lnsInstanceNametoDelete);

        flushCache();

        log.info("LNS instance named '{}' is deleted.", lnsInstanceNametoDelete);
    }

    @PostConstruct
    private void loadCache() throws LnsInstanceServiceException {
        this.tenantOptionKeyForLnsInstanceMap = new OptionPK(LnsInstanceDeserializer.agentName, LNS_INSTANCE_MAP_KEY);

        String lnsInstancesString = null;
        try {
            lnsInstancesString = tenantOptionApi.getOption(this.tenantOptionKeyForLnsInstanceMap).getValue();
        } catch (SDKException e) {
            if (e.getHttpStatus() != HttpStatus.NOT_FOUND.value()) {
                String message = String.format("Error while fetching the tenant option with key '%s'.", tenantOptionKeyForLnsInstanceMap);
                log.error(message);
                throw new LnsInstanceServiceException(message, e);
            } else {
                log.debug("No LNS instances found in the tenant options with key {}", LNS_INSTANCE_MAP_KEY);
            }
        }

        if (!StringUtils.isBlank(lnsInstancesString)) {
            try {
                cachedLnsInstances = JSON_MAPPER
                        .readerWithView(LnsInstance.InternalView.class)
                        .forType(ConcurrentHashMap.class)
                        .readValue(lnsInstancesString);
            } catch (JsonProcessingException e) {
                String message = String.format("Error unmarshalling the below JSON string containing LNS instance map stored as a tenant option with key '%s'. \n%s", tenantOptionKeyForLnsInstanceMap, lnsInstancesString);
                log.error(message);
                throw new LnsInstanceServiceException(message, e);
            }
        }

        log.debug("LNS instances loaded from the tenant options with key '{}'. Cached LNS instance count is {}.", tenantOptionKeyForLnsInstanceMap, cachedLnsInstances.size());
    }

    private void flushCache() throws LnsInstanceServiceException {
        String lnsInstancesString = null;
        try {
            lnsInstancesString = JSON_MAPPER
                    .writerWithView(LnsInstance.InternalView.class)
                    .writeValueAsString(cachedLnsInstances);
        } catch (JsonProcessingException e) {
            String message = String.format("Error unmarshalling the below JSON string containing LNS instance map stored as a tenant option with key '%s'. \n%s", tenantOptionKeyForLnsInstanceMap, lnsInstancesString);
            log.error(message);
            throw new LnsInstanceServiceException(message, e);
        }

        if (!StringUtils.isBlank(lnsInstancesString)) {
            try {
                tenantOptionApi.save(new OptionRepresentation());
            } catch (SDKException e) {
                String message = String.format("Error saving the below LNS instance map as a tenant option with key '%s'. \n%s", tenantOptionKeyForLnsInstanceMap, lnsInstancesString);
                log.error(message);
                throw new LnsInstanceServiceException(message, e);
            }
        }

        log.debug("LNS instances saved in the tenant options with key '{}'. Cached LNS instance count is {}.", tenantOptionKeyForLnsInstanceMap, cachedLnsInstances.size());
    }
}
