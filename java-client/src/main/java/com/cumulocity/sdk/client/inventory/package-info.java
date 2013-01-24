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

