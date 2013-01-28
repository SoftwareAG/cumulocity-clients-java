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

