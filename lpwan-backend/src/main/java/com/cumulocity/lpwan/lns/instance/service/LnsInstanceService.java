/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.service;

import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.cumulocity.lpwan.exception.LpwanServiceException;
import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.lpwan.lns.instance.model.LnsInstanceDeserializer;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@TenantScope
public class LnsInstanceService {

    private static final String LNS_INSTANCES_KEY = "credentials.lns.instances.map";

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Autowired
    private TenantOptionApi tenantOptionApi;

    private OptionPK lnsInstancesTenantOptionKey;

    private LoadingCache<OptionPK, Map<String, LnsInstance>> lnsInstancesCache = CacheBuilder.newBuilder()
            .maximumSize(1) // Only one key, as we are keeping the Map which is loaded from the tenant options
            .expireAfterAccess(10, TimeUnit.MINUTES) // Evict when not accessed for 10 mins
            .build(
                    new CacheLoader<OptionPK, Map<String, LnsInstance>>() {
                        @Override
                        public @Nonnull Map<String, LnsInstance> load(@Nonnull OptionPK key) throws Exception {
                            return loadLnsInstancesFromTenantOptions(key);
                        }
                    }
            );

    public @NotNull LnsInstance getByName(@NotBlank String lnsInstanceName) throws LpwanServiceException {
        if (StringUtils.isBlank(lnsInstanceName)) {
            String message = "LNS instance name can't be null or blank.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        Map<String, LnsInstance> lnsInstances = getLnsInstances();
        if (!lnsInstances.containsKey(lnsInstanceName)) {
            String message = String.format("LNS instance named '%s' doesn't exist.", lnsInstanceName);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        return lnsInstances.get(lnsInstanceName);
    }

    public Collection<LnsInstance> getAll() throws LpwanServiceException {
        return getLnsInstances().values();
    }

    public synchronized @NotNull LnsInstance create(@NotNull LnsInstance newLnsInstance) throws LpwanServiceException {
        if (newLnsInstance == null) {
            String message = "New LNS instance can't be null.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        // Validate new LnsInstance
        newLnsInstance.isValid();

        Map<String, LnsInstance> lnsInstances = getLnsInstances();
        String newLnsInstanceName = newLnsInstance.getName();
        if (lnsInstances.containsKey(newLnsInstanceName)) {
            String message = String.format("LNS instance named '%s' already exists.", newLnsInstanceName);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        lnsInstances.put(newLnsInstanceName, newLnsInstance);

        flushCache();

        log.info("New LNS instance named '{}' is created.", newLnsInstanceName);

        return newLnsInstance;
    }

    public synchronized @NotNull LnsInstance update(@NotBlank String existingLnsInstanceName, @NotNull LnsInstance lnsInstanceToUpdate) throws LpwanServiceException {
        if (StringUtils.isBlank(existingLnsInstanceName)) {
            String message = "Existing LNS instance name can't be null or blank.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        Map<String, LnsInstance> lnsInstances = getLnsInstances();
        if (!lnsInstances.containsKey(existingLnsInstanceName)) {
            String message = String.format("LNS instance named '%s' doesn't exist.", existingLnsInstanceName);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        if (lnsInstanceToUpdate == null) {
            String message = "LNS instance to update can't be null.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        // Validate LnsInstance to update
        lnsInstanceToUpdate.isValid();

        String updatedLnsInstanceName = lnsInstanceToUpdate.getName();
        if (!existingLnsInstanceName.equals(updatedLnsInstanceName)) {
            if (lnsInstances.containsKey(updatedLnsInstanceName)) {
                String message = String.format("LNS instance named '%s' already exists.", updatedLnsInstanceName);
                log.error(message);
                throw new InputDataValidationException(message);
            }
        }

        LnsInstance existingLnsInstance = lnsInstances.remove(existingLnsInstanceName);
        existingLnsInstance.initializeWith(lnsInstanceToUpdate);

        lnsInstances.put(updatedLnsInstanceName, existingLnsInstance);

        flushCache();

        if (!existingLnsInstanceName.equals(updatedLnsInstanceName)) {
            log.info("LNS instance named '{}', is renamed to '{}' and updated.", existingLnsInstanceName, updatedLnsInstanceName);
        } else {
            log.info("LNS instance named '{}' is updated.", existingLnsInstanceName);
        }

        return existingLnsInstance;
    }

    public synchronized void delete(@NotBlank String lnsInstanceNametoDelete) throws LpwanServiceException {
        if (StringUtils.isBlank(lnsInstanceNametoDelete)) {
            String message = "LNS instance name to delete can't be null or blank.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        Map<String, LnsInstance> lnsInstances = getLnsInstances();
        if (!lnsInstances.containsKey(lnsInstanceNametoDelete)) {
            String message = String.format("LNS instance named '%s' doesn't exist.", lnsInstanceNametoDelete);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        lnsInstances.remove(lnsInstanceNametoDelete);

        flushCache();

        log.info("LNS instance named '{}' is deleted.", lnsInstanceNametoDelete);
    }

    private Map<String, LnsInstance> loadLnsInstancesFromTenantOptions(OptionPK tenantOptionKeyForLnsInstanceMap) throws LpwanServiceException {
        String lnsInstancesString = null;
        try {
            lnsInstancesString = tenantOptionApi.getOption(getLnsInstancesTenantOptionKey()).getValue();
        } catch (SDKException e) {
            if (e.getHttpStatus() != HttpStatus.NOT_FOUND.value()) {
                String message = String.format("Error while fetching the tenant option with key '%s'.", tenantOptionKeyForLnsInstanceMap);
                log.error(message, e);
                throw new LpwanServiceException(message, e);
            } else {
                log.debug("No LNS instances found in the tenant options with key {}", LNS_INSTANCES_KEY);
            }
        }

        Map<String, LnsInstance> lnsInstancesMap = new ConcurrentHashMap<>();
        if (!StringUtils.isBlank(lnsInstancesString)) {
            try {
                lnsInstancesMap = JSON_MAPPER
                        .readerWithView(LnsInstance.InternalView.class)
                        .forType(ConcurrentHashMap.class)
                        .readValue(lnsInstancesString);
            } catch (JsonProcessingException e) {
                String message = String.format("Error unmarshalling the below JSON string containing LNS instance map stored as a tenant option with key '%s'. \n%s", tenantOptionKeyForLnsInstanceMap, lnsInstancesString);
                log.error(message, e);
                throw new LpwanServiceException(message, e);
            }
        }

        log.debug("LNS instances loaded from the tenant options with key '{}'. Cached LNS instance count is {}.", tenantOptionKeyForLnsInstanceMap, lnsInstancesMap.size());

        return lnsInstancesMap;
    }

    private void flushCache() throws LpwanServiceException {
        OptionPK lnsInstancesTenantOptionKey = getLnsInstancesTenantOptionKey();
        Map<String, LnsInstance> lnsInstances = getLnsInstances();
        String lnsInstancesString;
        try {
            lnsInstancesString = JSON_MAPPER
                    .writerWithView(LnsInstance.InternalView.class)
                    .writeValueAsString(lnsInstances);
        } catch (JsonProcessingException e) {
            String message = String.format("Error marshaling the LNS instances map and store as a tenant option with key '%s'.", lnsInstancesTenantOptionKey);
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }

        if (!StringUtils.isBlank(lnsInstancesString)) {
            try {
                OptionRepresentation tenantOption = OptionRepresentation.asOptionRepresentation(
                        lnsInstancesTenantOptionKey.getCategory(),
                        lnsInstancesTenantOptionKey.getKey(),
                        lnsInstancesString);

                tenantOptionApi.save(tenantOption);
            } catch (SDKException e) {
                String message = String.format("Error saving the below LNS instance map as a tenant option with key '%s'. \n%s", lnsInstancesTenantOptionKey, lnsInstancesString);
                log.error(message, e);
                throw new LpwanServiceException(message, e);
            }
        }

        log.debug("LNS instances saved in the tenant options with key '{}'. Cached LNS instance count is {}.", lnsInstancesTenantOptionKey, lnsInstances.size());
    }

    private Map<String, LnsInstance> getLnsInstances() throws LpwanServiceException {
        try {
            return lnsInstancesCache.get(getLnsInstancesTenantOptionKey());
        } catch (ExecutionException e) {
            String message = String.format("Unexpected error occurred while accessing the cached LNS instances map with key '%s'.", getLnsInstancesTenantOptionKey());
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }
    }

    private OptionPK getLnsInstancesTenantOptionKey() {
        if (lnsInstancesTenantOptionKey == null) {
            lnsInstancesTenantOptionKey = new OptionPK(LnsInstanceDeserializer.lnsInstanceClass.getSimpleName(), LNS_INSTANCES_KEY);
        }

        return lnsInstancesTenantOptionKey;
    }
}
