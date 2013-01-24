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
 * Provides the classes necessary to create, update and query audit records.
 * 
 * Audit record is represented by AuditRecordRepresentation and a set of 
 * audit records by AuditRecordCollectionRepresentation.
 * 
 * Audit record api involves four main entities:
 * the audit record api operationCollection, the audit record operationCollection,
 * the audit record collection operationCollection and the audit record filter operationCollection.
 * 
 * <ul>
 * <li>The audit record api operationCollection is the entry point to the audit record api.</li>
 * <li>The audit record operationCollection can be used to get an audit record.</li>
 * <li>The audit record collection operationCollection can be used to create an audit record
 *  and get all the audit records.</li>
 * <li>The audit record filter operationCollection can be used to query audit records
 * based on different conditions</li>
 * </ul>
 * 
 * @since 0.9
 * @see com.cumulocity.rest.representation.audit
 */
package com.cumulocity.sdk.client.audit;

