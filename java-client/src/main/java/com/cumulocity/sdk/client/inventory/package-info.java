/*
 * Copyright 2012 Nokia Siemens Networks 
 */
/**
 * Provides the classes necessary to create, delete, update and query managed objects.
 * 
 * Managed object is represented by ManagedObjectRepresentation and a set of 
 * managed objects by ManagedObjectCollectionRepresentation.
 * 
 * Inventory api involves four main entities:
 * the inventory api operationCollection, the managed object operationCollection,
 * the managed object collection operationCollection and the managed object filter operationCollection.
 * 
 * <ul>
 * <li>The inventory api operationCollection is the entry point to the inventory api.</li>
 * <li>The managed object operationCollection can be used to get, update and delete a managed 
 * object. Also this operationCollection can be used for adding, deleting, getting all 
 * and individual child devices.</li>
 * <li>The managed object collection operationCollection can be used to create a managed object
 *  and get all the managed objects.</li>
 * <li>The managed object filter operationCollection can be used to query managed object
 * based on different conditions</li>
 * </ul>
 * 
 * @since 0.9
 * @see com.cumulocity.rest.representation.inventory
 */
package com.cumulocity.sdk.client.inventory;

