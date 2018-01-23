package com.cumulocity.microservice.platform.api.inventory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.BinariesApi;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service("binariesInternalApi")
public class BinariesInternalApi implements BinariesApi{

    @Autowired(required = false)
    @Qualifier("binariesApi")
    private BinariesApi binariesApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    @Override
    public ManagedObjectRepresentation uploadFile(final ManagedObjectRepresentation container, final byte[] bytes) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return binariesApi.uploadFile(container, bytes);
            }
        });
    }

    @Override
    public InputStream downloadFile(final GId id) throws SDKException {checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<InputStream>() {
            @Override
            public InputStream call() throws Exception {
                return binariesApi.downloadFile(id);
            }
        });
    }

    @Override
    public ManagedObjectRepresentation replaceFile(final GId containerId, final String contentType, final InputStream fileStream) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation call() throws Exception {
                return binariesApi.replaceFile(containerId, contentType, fileStream);
            }
        });
    }

    @Override
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
