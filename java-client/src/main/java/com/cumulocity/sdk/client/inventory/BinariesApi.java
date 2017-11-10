package com.cumulocity.sdk.client.inventory;

import java.io.InputStream;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;

public interface BinariesApi {
    
    ManagedObjectRepresentation uploadFile(ManagedObjectRepresentation container, byte[] bytes) throws SDKException;
    
    ManagedObjectRepresentation replaceFile(GId containerId, String contentType, InputStream fileStream) throws SDKException;

    InputStream downloadFile(GId id);
    
    void deleteFile(GId containerId) throws SDKException;

}
