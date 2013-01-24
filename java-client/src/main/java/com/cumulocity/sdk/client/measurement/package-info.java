/*
 * Copyright 2012 Nokia Siemens Networks 
 */
/**
 * Provides the classes necessary to create, delete and query measurements.
 * 
 * Measurement is represented by MeasurementRepresentation and a set of 
 * measurements by MeasurementCollectionRepresentation.
 * 
 * Measurement api involves four main entities:
 * the measurement api operationCollection, the measurement operationCollection,
 * the measurement collection operationCollection and the measurement filter operationCollection.
 * 
 * <ul>
 * <li>The measurement api operationCollection is the entry point to the measurement api.</li>
 * <li>The measurement operationCollection can be used to get and delete a measurement.</li>
 * <li>The measurement collection operationCollection can be used to create a measurement
 *  and get all the measurements.</li>
 * <li>The measurement filter operationCollection can be used to query measurements
 * based on different conditions</li>
 * </ul>
 * 
 * @since 0.9
 * @see com.cumulocity.rest.representation.measurement
 */
package com.cumulocity.sdk.client.measurement;

