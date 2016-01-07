package com.cumulocity.agent.server.repository;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.BinariesApi;

@Component
public class BinariesRepository {

    private final BinariesApi binariesApi;

    @Autowired
    public BinariesRepository(BinariesApi binariesApi) {
        this.binariesApi = binariesApi;
    }

    public ManagedObjectRepresentation uploadFile(ManagedObjectRepresentation container, byte[] bytes) throws SDKException {
        return binariesApi.uploadFile(container, bytes);
    }

    public ManagedObjectRepresentation replaceFile(GId containerId, String contentType, InputStream fileStream) throws SDKException {
        return binariesApi.replaceFile(containerId, contentType, fileStream);
    }

    public void deleteFile(GId containerId) throws SDKException {
        binariesApi.deleteFile(containerId);
    }
}
