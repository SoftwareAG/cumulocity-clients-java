package com.cumulocity.sdk.client.inventory;

import java.io.InputStream;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

public class BinariesApiImpl implements BinariesApi {

    private final RestConnector restConnector;

    private InventoryRepresentation inventoryRepresentation;
    
    public BinariesApiImpl(RestConnector restConnector, InventoryRepresentation inventoryRepresentation) {
        this.restConnector = restConnector;
        this.inventoryRepresentation = inventoryRepresentation;
    }

    @Override
    public ManagedObjectRepresentation uploadFile(ManagedObjectRepresentation container, byte[] bytes) throws SDKException {
        return restConnector.postFile(getBinariesUrl(), container, bytes, ManagedObjectRepresentation.class);
    }

    @Override
    public ManagedObjectRepresentation replaceFile(GId containerId, String contentType, InputStream fileStream) throws SDKException {
        return restConnector.putStream(getBinariesUrl() + "/" + containerId.getValue(), contentType, fileStream, ManagedObjectRepresentation.class);
    }

    @Override
    public void deleteFile(GId containerId) throws SDKException {
        restConnector.delete(getBinariesUrl() + "/" + containerId.getValue());
    }
    
    private String getBinariesUrl() throws SDKException {
        String url = inventoryRepresentation.getManagedObjects().getSelf();
        return url.replace("managedObjects", "binaries");
    }
}
