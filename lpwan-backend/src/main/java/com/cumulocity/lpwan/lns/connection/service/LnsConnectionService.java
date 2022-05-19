/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.connection.service;

import c8y.LpwanDevice;
import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.cumulocity.lpwan.exception.LpwanServiceException;
import com.cumulocity.lpwan.lns.connection.model.LnsConnection;
import com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.InventoryFilter;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@TenantScope
public class LnsConnectionService {

    private static final String LNS_CONNECTIONS_KEY = "credentials.lns.connections.map";

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final MapType mapType = JSON_MAPPER.getTypeFactory().constructMapType(ConcurrentHashMap.class, String.class, LnsConnection.class);

    @Autowired
    private TenantOptionApi tenantOptionApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    CsvService csvService;

    @Autowired
    private ContextService<Credentials> contextService;

    @Autowired
    private PlatformProperties platformProperties;

    @Autowired
    RestConnector restConnector;

    @Autowired
    private ApplicationApi applicationApi;


    private OptionPK lnsConnectionsTenantOptionKey;

    private volatile String contextPath;

    private LoadingCache<OptionPK, Map<String, LnsConnection>> lnsConnectionsCache = CacheBuilder.newBuilder()
            .maximumSize(1) // Only one key, as we are keeping the Map which is loaded from the tenant options
            .expireAfterAccess(10, TimeUnit.MINUTES) // Evict when not accessed for 10 mins
            .build(
                    new CacheLoader<OptionPK, Map<String, LnsConnection>>() {
                        @Override
                        public @Nonnull Map<String, LnsConnection> load(@Nonnull OptionPK key) throws Exception {
                            return loadLnsConnectionsFromTenantOptions(key);
                        }
                    }
            );

    private LoadingCache<String, List<ManagedObjectRepresentation>> associatedDeviceMoList = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, List<ManagedObjectRepresentation>>() {
                        @Override
                        public List<ManagedObjectRepresentation> load(String lnsConnectionName) throws Exception {
                            return getDeviceMoListAssociatedWithLnsConnection(lnsConnectionName);
                        }
                    }
            );

    public @NotNull LnsConnection getByName(@NotBlank String lnsConnectionName) throws LpwanServiceException {
        if (StringUtils.isBlank(lnsConnectionName)) {
            String message = "LNS connection name can't be null or blank.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        final String lnsConnectionNameLowerCase = lnsConnectionName.toLowerCase();

        Map<String, LnsConnection> lnsConnections = getLnsConnections();
        if (!lnsConnections.containsKey(lnsConnectionNameLowerCase)) {
            String message = String.format("LNS connection named '%s' doesn't exist.", lnsConnectionNameLowerCase);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        return lnsConnections.get(lnsConnectionNameLowerCase);
    }

    public Collection<LnsConnection> getAll() throws LpwanServiceException {
        return getLnsConnections().values();
    }

    public synchronized @NotNull LnsConnection create(@NotNull LnsConnection newLnsConnection) throws LpwanServiceException {
        if (newLnsConnection == null) {
            String message = "New LNS connection can't be null.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        // Validate new LnsConnection
        newLnsConnection.isValid();

        Map<String, LnsConnection> lnsConnections = getLnsConnections();
        String newLnsConnectionName = newLnsConnection.getName();
        if (lnsConnections.containsKey(newLnsConnectionName)) {
            String message = String.format("LNS connection named '%s' already exists.", newLnsConnectionName);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        lnsConnections.put(newLnsConnectionName, newLnsConnection);

        flushCache();

        log.info("New LNS connection named '{}' is created.", newLnsConnectionName);

        return newLnsConnection;
    }

    public synchronized @NotNull LnsConnection update(@NotBlank String existingLnsConnectionName, @NotNull LnsConnection lnsConnectionToUpdate) throws LpwanServiceException {
        if (StringUtils.isBlank(existingLnsConnectionName)) {
            String message = "Existing LNS connection name can't be null or blank.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        final String existingLnsConnectionNameLowerCase = existingLnsConnectionName.toLowerCase();

        Map<String, LnsConnection> lnsConnections = getLnsConnections();
        if (!lnsConnections.containsKey(existingLnsConnectionNameLowerCase)) {
            String message = String.format("LNS connection named '%s' doesn't exist.", existingLnsConnectionNameLowerCase);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        if (lnsConnectionToUpdate == null) {
            String message = "LNS connection to update can't be null.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        LnsConnection existingLnsConnection = lnsConnections.get(existingLnsConnectionNameLowerCase);
        existingLnsConnection.initializeWith(lnsConnectionToUpdate);

        // Validate LnsConnection after update
        existingLnsConnection.isValid();

        String updatedLnsConnectionName = lnsConnectionToUpdate.getName();
        if (!existingLnsConnectionNameLowerCase.equals(updatedLnsConnectionName)) {
            if (lnsConnections.containsKey(updatedLnsConnectionName)) {
                String message = String.format("LNS connection named '%s' already exists.", updatedLnsConnectionName);
                log.error(message);
                throw new InputDataValidationException(message);
            }
        }

        if(!existingLnsConnectionNameLowerCase.equals(lnsConnectionToUpdate.getName().toLowerCase())) {
            List<ManagedObjectRepresentation> managedObjectRepresentationList = getAssociatedDeviceMoList(existingLnsConnectionNameLowerCase);
            if (!managedObjectRepresentationList.isEmpty()) {
                String url = getURI(restConnector.getPlatformParameters().getHost() + "/service/" + contextPath + "/associated-device/" + existingLnsConnectionNameLowerCase);
                String errorMessage = String.format("Can not update the LNS connection with name '%s' as it's associated with '%s' device(s). \nVisit the following URL to download the list of devices. \nURL : %s",
                        existingLnsConnectionNameLowerCase, managedObjectRepresentationList.size(), url);
                log.info(errorMessage);
                throw new LpwanServiceException(errorMessage);
            }
        }

        lnsConnections.remove(existingLnsConnectionNameLowerCase);
        lnsConnections.put(updatedLnsConnectionName, existingLnsConnection);

        flushCache();

        if (!existingLnsConnectionNameLowerCase.equals(updatedLnsConnectionName)) {
            log.info("LNS connection named '{}', is renamed to '{}' and updated.", existingLnsConnectionNameLowerCase, updatedLnsConnectionName);
        } else {
            log.info("LNS connection named '{}' is updated.", existingLnsConnectionNameLowerCase);
        }

        return existingLnsConnection;
    }

    public synchronized void delete(@NotBlank String lnsConnectionNametoDelete) throws LpwanServiceException {
        if (StringUtils.isBlank(lnsConnectionNametoDelete)) {
            String message = "LNS connection name to delete can't be null or blank.";
            log.error(message);
            throw new InputDataValidationException(message);
        }

        final String lnsConnectionNametoDeleteLowerCase = lnsConnectionNametoDelete.toLowerCase();

        Map<String, LnsConnection> lnsConnections = getLnsConnections();
        if (!lnsConnections.containsKey(lnsConnectionNametoDeleteLowerCase)) {
            String message = String.format("LNS connection named '%s' doesn't exist.", lnsConnectionNametoDeleteLowerCase);
            log.error(message);
            throw new InputDataValidationException(message);
        }

        List<ManagedObjectRepresentation> managedObjectRepresentationList = getAssociatedDeviceMoList(lnsConnectionNametoDeleteLowerCase);

        if(!managedObjectRepresentationList.isEmpty()) {
            String url = getURI(restConnector.getPlatformParameters().getHost() + "/service/" + contextPath + "/associated-device/" + lnsConnectionNametoDelete);
            String errorMessage = String.format("Can not delete the LNS connection with name '%s' as it's associated with '%s' device(s). \nVisit the following URL to download the list of devices. \nURL : %s",
                    lnsConnectionNametoDelete, managedObjectRepresentationList.size(), url);
            log.info(errorMessage);
            throw new LpwanServiceException(errorMessage);
        }

        lnsConnections.remove(lnsConnectionNametoDeleteLowerCase);

        flushCache();

        log.info("LNS connection named '{}' is deleted.", lnsConnectionNametoDeleteLowerCase);
    }

    public synchronized InputStreamResource getDeviceManagedObjectsInCsv(@NotBlank String lnsConnectionName) throws LpwanServiceException {
        try {
            return new InputStreamResource(csvService.writeDataToCsv(associatedDeviceMoList.get(lnsConnectionName)));
        } catch (ExecutionException e) {
            String message = String.format("Unexpected error occurred while accessing the cached device managed objects map with key '%s'.", lnsConnectionName);
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }
    }

    private Map<String, LnsConnection> loadLnsConnectionsFromTenantOptions(OptionPK tenantOptionKeyForLnsConnectionMap) throws LpwanServiceException {
        String lnsConnectionsString = null;
        try {
            lnsConnectionsString = tenantOptionApi.getOption(getLnsConnectionsTenantOptionKey()).getValue();
        } catch (SDKException e) {
            if (e.getHttpStatus() != HttpStatus.NOT_FOUND.value()) {
                String message = String.format("Error while fetching the tenant option with key '%s'.", tenantOptionKeyForLnsConnectionMap);
                log.error(message, e);
                throw new LpwanServiceException(message, e);
            } else {
                log.debug("No LNS connections found in the tenant options with key {}", LNS_CONNECTIONS_KEY);
            }
        }

        Map<String, LnsConnection> lnsConnectionsMap = new ConcurrentHashMap<>();
        if (!StringUtils.isBlank(lnsConnectionsString)) {
            try {
                lnsConnectionsMap = JSON_MAPPER
                        .readerWithView(LnsConnection.InternalView.class)
                        .forType(mapType)
                        .readValue(lnsConnectionsString);
            } catch (JsonProcessingException e) {
                String message = String.format("Error unmarshalling the below JSON string containing LNS connection map stored as a tenant option with key '%s'. \n%s", tenantOptionKeyForLnsConnectionMap, lnsConnectionsString);
                log.error(message, e);
                throw new LpwanServiceException(message, e);
            }
        }

        log.debug("LNS connections loaded from the tenant options with key '{}'. Cached LNS connection count is {}.", tenantOptionKeyForLnsConnectionMap, lnsConnectionsMap.size());

        return lnsConnectionsMap;
    }

    private void flushCache() throws LpwanServiceException {
        OptionPK lnsConnectionsTenantOptionKey = getLnsConnectionsTenantOptionKey();
        Map<String, LnsConnection> lnsConnections = getLnsConnections();
        String lnsConnectionsString;
        try {
            lnsConnectionsString = JSON_MAPPER
                    .writerWithView(LnsConnection.InternalView.class)
                    .writeValueAsString(lnsConnections);
        } catch (JsonProcessingException e) {
            String message = String.format("Error marshaling the LNS connections map and store as a tenant option with key '%s'.", lnsConnectionsTenantOptionKey);
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }

        if (!StringUtils.isBlank(lnsConnectionsString)) {
            try {
                OptionRepresentation tenantOption = OptionRepresentation.asOptionRepresentation(
                                                                    lnsConnectionsTenantOptionKey.getCategory(),
                                                                    lnsConnectionsTenantOptionKey.getKey(),
                                                                    lnsConnectionsString);

                tenantOptionApi.save(tenantOption);
            } catch (SDKException e) {
                String message = String.format("Error saving the below LNS connection map as a tenant option with key '%s'. \n%s", lnsConnectionsTenantOptionKey, lnsConnectionsString);
                log.error(message, e);
                throw new LpwanServiceException(message, e);
            }
        }

        log.debug("LNS connections saved in the tenant options with key '{}'. Cached LNS connection count is {}.", lnsConnectionsTenantOptionKey, lnsConnections.size());
    }

    private Map<String, LnsConnection> getLnsConnections() throws LpwanServiceException {
        try {
            return lnsConnectionsCache.get(getLnsConnectionsTenantOptionKey());
        } catch (ExecutionException e) {
            String message = String.format("Unexpected error occurred while accessing the cached LNS connections map with key '%s'.", getLnsConnectionsTenantOptionKey());
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }
    }

    private List<ManagedObjectRepresentation> getAssociatedDeviceMoList(String lnsConnectionNameLowerCase) throws LpwanServiceException {
        try {
            return associatedDeviceMoList.get(lnsConnectionNameLowerCase);
        } catch (ExecutionException e) {
            String message = String.format("Unexpected error occurred while accessing the cached device managed objects map with key '%s'.", lnsConnectionNameLowerCase);
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }
    }

    private OptionPK getLnsConnectionsTenantOptionKey() {
        if (lnsConnectionsTenantOptionKey == null) {
            lnsConnectionsTenantOptionKey = new OptionPK(LnsConnectionDeserializer.getRegisteredAgentName(), LNS_CONNECTIONS_KEY);
        }

        return lnsConnectionsTenantOptionKey;
    }

    private List<ManagedObjectRepresentation> getDeviceMoListAssociatedWithLnsConnection(String lnsConnectionNametoDeleteLowerCase) throws LpwanServiceException {
        InventoryFilter inventoryFilter = InventoryFilter.searchInventory().byFragmentType(LpwanDevice.class);
        Iterable<ManagedObjectRepresentation> managedObjectRepresentations = null;
        try {
            managedObjectRepresentations = inventoryApi.getManagedObjectsByFilter(inventoryFilter).get().allPages();
        } catch (SDKException e) {
            String message = String.format("Error in getting device managed objects with fragment type 'c8y_LpwanDevice'");
            log.error(message, e);
            throw new LpwanServiceException(message, e);
        }
        List<ManagedObjectRepresentation> managedObjectRepresentationList = Lists.newArrayList(managedObjectRepresentations);
        managedObjectRepresentationList.removeIf(mo -> !lnsConnectionNametoDeleteLowerCase.equals(mo.get(LpwanDevice.class).getLnsConnectionName()));
        return managedObjectRepresentationList;
    }

    public String getURI(String url) throws LpwanServiceException {
        try {
            return UriComponentsBuilder
                    .fromHttpUrl(url)
                    .build()
                    .toUri()
                    .toString();
        } catch (IllegalStateException e){
            log.error("Invalid URL '{}'", url, e);
            throw new LpwanServiceException(e.getMessage(), e);
        }
    }

    @EventListener
    public void registerDeviceTypes(MicroserviceSubscriptionAddedEvent event) {
        if (Objects.isNull(contextPath)) {
            contextPath =
                    contextService.callWithinContext(platformProperties.getMicroserviceBoostrapUser(),
                            () -> {
                                try {
                                    return applicationApi.currentApplication().get().getContextPath();
                                } catch (Exception e) {
                                    log.warn("Error while determining the microservice context path. Defaulting to the application name.", e);
                                    return platformProperties.getApplicationName();
                                }
                            });
        }
    }
}
