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

