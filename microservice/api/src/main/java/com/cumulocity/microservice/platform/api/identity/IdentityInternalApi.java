package com.cumulocity.microservice.platform.api.identity;

import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.identity.ExternalIDCollection;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class IdentityInternalApi {

    @Autowired(required = false)
    private IdentityApi identityApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public ExternalIDRepresentation create(final ExternalIDRepresentation externalId) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ExternalIDRepresentation>() {
            @Override
            public ExternalIDRepresentation call() throws Exception {
                return identityApi.create(externalId);
            }
        });
    }

    public ExternalIDRepresentation getExternalId(final ID extId) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ExternalIDRepresentation>() {
            @Override
            public ExternalIDRepresentation call() throws Exception {
                return identityApi.getExternalId(extId);
            }
        });
    }

    public ExternalIDCollection getExternalIdsOfGlobalId(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<ExternalIDCollection>() {
            @Override
            public ExternalIDCollection call() throws Exception {
                return identityApi.getExternalIdsOfGlobalId(gid);
            }
        });
    }

    public void deleteExternalId(final ExternalIDRepresentation externalId) throws SDKException {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                identityApi.deleteExternalId(externalId);
                return null;
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(identityApi, "Bean of type: " + IdentityApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
