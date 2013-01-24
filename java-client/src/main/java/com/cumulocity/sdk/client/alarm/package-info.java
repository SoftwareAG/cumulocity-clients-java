/*
 * Copyright 2012 Nokia Siemens Networks 
 */
/**
 * Provides the classes necessary to create, update and query alarms.
 * 
 * Alarm is represented by AlarmRepresentation and a set of 
 * alarms by AlarmCollectionRepresentation.
 * 
 * Alarm api involves four main entities:
 * the alarm api operationCollection, the alarm operationCollection,
 * the alarm collection operationCollection and the alarm filter operationCollection.
 * 
 * <ul>
 * <li>The alarm api operationCollection is the entry point to the alarm api.</li>
 * <li>The alarm operationCollection can be used to get and update an alarm.</li>
 * <li>The alarm collection operationCollection can be used to create an alarm
 *  and get all the alarm.</li>
 * <li>The alarm filter operationCollection can be used to query alarms
 * based on different conditions</li>
 * </ul>
 * 
 * @since 0.9
 * @see com.cumulocity.rest.representation.alarm
 */
package com.cumulocity.sdk.client.alarm;

