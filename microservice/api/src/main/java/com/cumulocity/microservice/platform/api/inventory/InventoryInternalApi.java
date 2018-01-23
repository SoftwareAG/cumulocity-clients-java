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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service("inventoryInternalApi")
public class InventoryInternalApi implements InventoryApi{

    @Autowired(required = false)
    @Qualifier("inventoryApi")
    private InventoryApi inventoryApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    @Override
    public ManagedObjectRepresentation get(final GId id) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return inventoryApi.get(id);
            }
        });
    }

    @Override
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

    @Override
    public ManagedObjectRepresentation update(final ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return inventoryApi.update(managedObjectRepresentation);
            }
        });
    }


    @Override
    public ManagedObject getManagedObjectApi(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObject>() {
            @Override
            public ManagedObject call() throws Exception {
                return inventoryApi.getManagedObjectApi(gid);
            }
        });
    }

    @Override
    public ManagedObjectRepresentation create(final ManagedObjectRepresentation managedObject) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return inventoryApi.create(managedObject);
            }
        });
    }

    @Override
    public ManagedObjectCollection getManagedObjects() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectCollection>() {
            @Override
            public ManagedObjectCollection call() throws Exception {
                return inventoryApi.getManagedObjects();
            }
        });
    }

    @Override
    public ManagedObjectCollection getManagedObjectsByFilter(InventoryFilter filter) throws SDKException {
        checkBeansNotNull();
        return inventoryApi.getManagedObjectsByFilter(filter);
    }

    @Override
    public ManagedObjectCollection getManagedObjectsByListOfIds(final List<GId> ids) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectCollection>() {
            @Override
            public ManagedObjectCollection call() throws Exception {
                return inventoryApi.getManagedObjectsByListOfIds(ids);
            }
        });
    }

    @Override
    public ManagedObject getManagedObject(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObject>() {
            @Override
            public ManagedObject call() throws Exception {
                return inventoryApi.getManagedObject(gid);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(inventoryApi, "Bean of type: " + InventoryApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
