/*
 * Copyright 2012 Nokia Siemens Networks 
 */
/**
 * Provides the classes necessary to create, delete, update and query events.
 * 
 * Event is represented by EventRepresentation and a set of 
 * events by EventCollectionRepresentation.
 * 
 * Event api involves four main entities:
 * the event api operationCollection, the event operationCollection,
 * the event collection operationCollection and the event filter operationCollection.
 * 
 * <ul>
 * <li>The event api operationCollection is the entry point to the event api.</li>
 * <li>The event operationCollection can be used to get and delete an event.</li>
 * <li>The event collection operationCollection can be used to create an event
 *  and get all the events.</li>
 * <li>The event filter operationCollection can be used to query events
 * based on different conditions</li>
 * </ul>
 * 
 * @since 0.9
 * @see com.cumulocity.rest.representation.event
 */
package com.cumulocity.sdk.client.event;

