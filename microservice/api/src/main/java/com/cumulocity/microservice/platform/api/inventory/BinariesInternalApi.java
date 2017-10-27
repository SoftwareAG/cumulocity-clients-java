package com.cumulocity.microservice.platform.api.inventory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.BinariesApi;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class BinariesInternalApi {

    @Autowired(required = false)
    private BinariesApi binariesApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public ManagedObjectRepresentation uploadFile(final ManagedObjectRepresentation container, final byte[] bytes) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return binariesApi.uploadFile(container, bytes);
            }
        });
    }

    public ManagedObjectRepresentation replaceFile(final GId containerId, final String contentType, final InputStream fileStream) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return binariesApi.replaceFile(containerId, contentType, fileStream);
            }
        });
    }

    public void deleteFile(final GId containerId) throws SDKException {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                binariesApi.deleteFile(containerId);
                return null;
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(binariesApi, "Bean of type: " + BinariesApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }
}
