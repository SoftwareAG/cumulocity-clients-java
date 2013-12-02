/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cumulocity.sdk.client.identity;

import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.sdk.client.SDKException;

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
    ExternalIDCollection getExternalIdsOfGlobalId(GId gid) throws SDKException;

    /**
     * Deletes between the external id and its global id in the platform.
     *
     * @param externalId the external id to be deleted
     * @throws SDKException if the external could not be deleted
     */
    void deleteExternalId(ExternalIDRepresentation externalId) throws SDKException;
}
