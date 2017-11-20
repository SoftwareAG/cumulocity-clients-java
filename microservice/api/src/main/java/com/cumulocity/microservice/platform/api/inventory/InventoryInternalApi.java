package com.cumulocity.microservice.platform.api.inventory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.InventoryFilter;
import com.cumulocity.sdk.client.inventory.ManagedObject;
import com.cumulocity.sdk.client.inventory.ManagedObjectCollection;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class InventoryInternalApi {

    @Autowired(required = false)
    private InventoryApi inventoryApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public ManagedObjectRepresentation get(final GId id) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return inventoryApi.get(id);
            }
        });
    }

    public void delete(final GId id) throws SDKException {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                inventoryApi.delete(id);
                return null;
            }
        });
    }

    public ManagedObjectRepresentation update(final ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return inventoryApi.update(managedObjectRepresentation);
            }
        });
    }


    public ManagedObject getManagedObjectApi(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObject>() {
            @Override
            public ManagedObject call() throws Exception {
                return inventoryApi.getManagedObjectApi(gid);
            }
        });
    }

    public ManagedObjectRepresentation create(final ManagedObjectRepresentation managedObject) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return inventoryApi.create(managedObject);
            }
        });
    }

    public ManagedObjectCollection getManagedObjects() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectCollection>() {
            @Override
            public ManagedObjectCollection call() throws Exception {
                return inventoryApi.getManagedObjects();
            }
        });
    }

    public ManagedObjectCollection getManagedObjectsByFilter(InventoryFilter filter) throws SDKException {
        checkBeansNotNull();
        return inventoryApi.getManagedObjectsByFilter(filter);
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(inventoryApi, "Bean of type: " + InventoryApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
