/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.identity;

import com.cumulocity.me.model.ID;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

/**
 * API for creating, deleting and retrieving external ids from the platform.
 */
public interface IdentityApi {

    /**
     * Creates an association between the external id and its global id in the platform.
     *
     * @param externalId the external to be created
     * @return the created external id
     * @throws SDKException if the external id could not be created
     */
    ExternalIDRepresentation create(ExternalIDRepresentation externalId) throws SDKException;

    /**
     * Gets external id representation from the platform by the given external id. The returned external id contains the managed object.
     * The global id associated with the given external id can be extracted from that managed object
     *
     * @param extId id of the event to search for
     * @return the external id representation including the managed object
     * @throws SDKException if the external id is not found or if the query failed
     */
    ExternalIDRepresentation getExternalId(ID extId) throws SDKException;

    /**
     * Gets the external ids associated with the given global id
     *
     * @param gid the global of the external ids to search for
     * @return a collection of external ids with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getExternalIdsOfGlobalId(GId gid) throws SDKException;

    /**
     * Deletes between the external id and its global id in the platform.
     *
     * @param externalId the external id to be deleted
     * @throws SDKException if the external could not be deleted
     */
    void deleteExternalId(ExternalIDRepresentation externalId) throws SDKException;
}
