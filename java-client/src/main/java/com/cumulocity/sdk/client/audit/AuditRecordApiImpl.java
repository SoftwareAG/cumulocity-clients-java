/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.audit;

import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.audit.AuditMediaType;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class AuditRecordApiImpl implements AuditRecordApi {

    private static final String PARAMETER_APPLICATION = "application";

    private static final String PARAMETER_USER = "user";

    private static final String PARAMETER_TYPE = "type";

    private final String platformApiUrl;

    private final RestConnector restConnector;

    private final TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private AuditRecordsRepresentation auditRecordsRepresentation = null;

    @Deprecated
    public AuditRecordApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUrl) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUrl = platformApiUrl;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

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
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformApiUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        auditRecordsRepresentation = platformApiRepresentation.getAudit();
    }

    @Override
    public AuditRecordRepresentation getAuditRecord(GId gid) throws SDKException {
        String url = getAuditRecordsRepresentation().getAuditRecords().getSelf() + "/" + gid.getValue();
        return restConnector.get(url, AuditMediaType.AUDIT_RECORD, AuditRecordRepresentation.class);
    }

    @Override
    public PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecords() throws SDKException {
        String url = getAuditRecordsRepresentation().getAuditRecords().getSelf();
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByType(String type) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForType();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_TYPE, type);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByUser(String user) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForUser();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_USER, user);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByApplication(String application)
            throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForApplication();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByUserAndType(String user, String type)
            throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForUserAndType();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_USER, user);
        filter.put(PARAMETER_TYPE, type);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByUserAndApplication(String user, String application)
            throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForUserAndApplication();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_USER, user);
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByTypeAndApplication(String type, String application)
            throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForTypeAndApplication();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_TYPE, type);
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByTypeAndUserAndApplication(String user,
            String type, String application) throws SDKException {
        String urlTemplate = getAuditRecordsRepresentation().getAuditRecordsForTypeAndUserAndApplication();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(PARAMETER_USER, user);
        filter.put(PARAMETER_TYPE, type);
        filter.put(PARAMETER_APPLICATION, application);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public AuditRecordRepresentation create(AuditRecordRepresentation representation) throws SDKException {
        return restConnector.post(getAuditRecordsRepresentation().getAuditRecords().getSelf(), AuditMediaType.AUDIT_RECORD, representation);
    }

    @Override
    public PagedCollectionResource<AuditRecordCollectionRepresentation> getAuditRecordsByFilter(AuditRecordFilter filter)
            throws SDKException {

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

}
