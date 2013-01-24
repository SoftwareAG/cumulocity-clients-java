/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.audit;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.audit.AuditMediaType;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

public class AuditRecordApiImpl implements AuditRecordApi {

    private static final String PARAMETER_APPLICATION = "application";

    private static final String PARAMETER_USER = "user";

    private static final String PARAMETER_TYPE = "type";

    private final String platformApiUrl;

    private final RestConnector restConnector;

    private final TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private AuditRecordsRepresentation auditRecordsRepresentation = null;

    public AuditRecordApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUrl = platformApiUrl;
        this.pageSize = pageSize;
    }

    private AuditRecordsRepresentation getAuditRecordsRepresentation() throws SDKException {
        if (null == auditRecordsRepresentation) {
            createApiRepresentation();
        }
        return auditRecordsRepresentation;
    }

    private void createApiRepresentation() throws SDKException {
        PlatformApiRepresentation platformApiRepresentation = (PlatformApiRepresentation) restConnector.get(platformApiUrl,
                PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        auditRecordsRepresentation = platformApiRepresentation.getAudit();
    }

    public AuditRecordRepresentation getAuditRecord(GId gid) throws SDKException {
        String url = getSelfUri() + "/" + gid.getValue();
        return (AuditRecordRepresentation) restConnector.get(url, AuditMediaType.AUDIT_RECORD, AuditRecordRepresentation.class);
    }

    public PagedCollectionResource getAuditRecords() throws SDKException {
        String url = getSelfUri();
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByType(String type) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForType();
        Map filter = new HashMap();
        filter.put(PARAMETER_TYPE, type);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByUser(String user) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForUser();
        Map filter = new HashMap();
        filter.put(PARAMETER_USER, user);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByApplication(String application) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForApplication();
        Map filter = new HashMap();
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByUserAndType(String user, String type) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForUserAndType();
        Map filter = new HashMap();
        filter.put(PARAMETER_USER, user);
        filter.put(PARAMETER_TYPE, type);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByUserAndApplication(String user, String application) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForUserAndApplication();
        Map filter = new HashMap();
        filter.put(PARAMETER_USER, user);
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByTypeAndApplication(String type, String application) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForTypeAndApplication();
        Map filter = new HashMap();
        filter.put(PARAMETER_TYPE, type);
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getAuditRecordsByTypeAndUserAndApplication(String user, String type, String application)
            throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForTypeAndUserAndApplication();
        Map filter = new HashMap();
        filter.put(PARAMETER_USER, user);
        filter.put(PARAMETER_TYPE, type);
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    public PagedCollectionResource getAuditRecordsByFilter(AuditRecordFilter filter) throws SDKException {

        String application = filter.getApplication();
        String type = filter.getType();
        String user = filter.getUser();
        if (user != null && type != null && application != null) {
            return getAuditRecordsByTypeAndUserAndApplication(user, type, application);
        } else if (user != null && application != null) {
            return getAuditRecordsByUserAndApplication(user, application);
        } else if (user != null && type != null) {
            return getAuditRecordsByUserAndType(user, type);
        } else if (application != null && type != null) {
            return getAuditRecordsByTypeAndApplication(type, application);
        } else if (user != null) {
            return getAuditRecordsByUser(user);
        } else if (type != null) {
            return getAuditRecordsByType(type);
        } else if (application != null) {
            return getAuditRecordsByApplication(application);
        } else {
            return getAuditRecords();
        }
    }

    public AuditRecordRepresentation create(AuditRecordRepresentation representation) throws SDKException {
        return (AuditRecordRepresentation) restConnector.post(getSelfUri(), AuditMediaType.AUDIT_RECORD, representation);
    }

    private String getSelfUri() {
        return getAuditRecordsRepresentation().getAuditRecords().getSelf();
    }

}
