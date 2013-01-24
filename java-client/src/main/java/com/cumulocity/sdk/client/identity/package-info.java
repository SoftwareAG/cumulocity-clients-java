/*
 * Copyright 2012 Nokia Siemens Networks 
 */
/**
 * Provides the classes necessary to bind, unbind and query external identifiers.
 * 
 * An external identifier is represented by ExternalIDRepresentation and a set of 
 * external identifiers by ExternalIDCollectionRepresentation.
 * 
 * Identity api involves three main entities:
 * the identity api operationCollection, the global id operationCollection,
 * the external id operationCollection.
 * 
 * <ul>
 * <li>The identity api operationCollection is the entry point to the identity api.</li>
 * <li>The global id operationCollection can be used to bind and get all external ids for a 
 * global id.</li>
 * <li>The external id operationCollection can be used to get and delete an external identifier.</li>
 * </ul>
 * 
 * @since 0.9
 * @see com.cumulocity.rest.representation.identity
 */
package com.cumulocity.sdk.client.identity;

