package com.cumulocity.microservice.platform.api.audit;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.audit.AuditRecordApi;
import com.cumulocity.sdk.client.audit.AuditRecordCollection;
import com.cumulocity.sdk.client.audit.AuditRecordFilter;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class AuditRecordInternalApi {

    @Autowired(required = false)
    private AuditRecordApi auditRecordApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public AuditRecordRepresentation getAuditRecord(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AuditRecordRepresentation>() {
            @Override
            public AuditRecordRepresentation call() throws Exception {
                return auditRecordApi.getAuditRecord(gid);
            }
        });
    }

    public AuditRecordRepresentation create(final AuditRecordRepresentation auditRecord) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AuditRecordRepresentation>() {
            @Override
            public AuditRecordRepresentation call() throws Exception {
                return auditRecordApi.create(auditRecord);
            }
        });
    }

    public AuditRecordCollection getAuditRecords() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AuditRecordCollection>() {
            @Override
            public AuditRecordCollection call() throws Exception {
                return auditRecordApi.getAuditRecords();
            }
        });
    }

    public AuditRecordCollection getAuditRecordsByFilter(final AuditRecordFilter filter) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AuditRecordCollection>() {
            @Override
            public AuditRecordCollection call() throws Exception {
                return auditRecordApi.getAuditRecordsByFilter(filter);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(auditRecordApi, "Bean of type: " + AuditRecordApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
