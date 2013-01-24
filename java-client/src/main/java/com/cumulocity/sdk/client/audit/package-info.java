/*
 * Copyright 2012 Nokia Siemens Networks 
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

