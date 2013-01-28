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
package com.cumulocity.me.rest.representation.measurement;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class MeasurementsApiRepresentation extends BaseCumulocityResourceRepresentation {

    private MeasurementCollectionRepresentation measurements;

    private String measurementsForSource;

    private String measurementsForDate;

    private String measurementsForFragmentType;

    private String measurementsForType;
    
    private String measurementsForSourceAndDate;

    private String measurementsForSourceAndFragmentType;

    private String measurementsForSourceAndType;

    private String measurementsForDateAndFragmentType;

    private String measurementsForDateAndType;

    private String measurementsForFragmentTypeAndType;

    private String measurementsForSourceAndDateAndFragmentType;

    private String measurementsForSourceAndDateAndType;

    private String measurementsForSourceAndFragmentTypeAndType;

    private String measurementsForDateAndFragmentTypeAndType;

    private String measurementsForSourceAndDateAndFragmentTypeAndType;

    public MeasurementCollectionRepresentation getMeasurements() {
        return measurements;
    }

    public void setMeasurements(MeasurementCollectionRepresentation measurements) {
        this.measurements = measurements;
    }

    public String getMeasurementsForSource() {
        return measurementsForSource;
    }

    public void setMeasurementsForSource(String measurementsForSource) {
        this.measurementsForSource = measurementsForSource;
    }

    public String getMeasurementsForDate() {
        return measurementsForDate;
    }

    public void setMeasurementsForDate(String measurementsForDate) {
        this.measurementsForDate = measurementsForDate;
    }

    public String getMeasurementsForFragmentType() {
        return measurementsForFragmentType;
    }

    public void setMeasurementsForFragmentType(String measurementsForFragmentType) {
        this.measurementsForFragmentType = measurementsForFragmentType;
    }

    public String getMeasurementsForType() {
        return measurementsForType;
    }

    public void setMeasurementsForType(String measurementsForType) {
        this.measurementsForType = measurementsForType;
    }

    public String getMeasurementsForSourceAndDate() {
        return measurementsForSourceAndDate;
    }

    public void setMeasurementsForSourceAndDate(String measurementsForSourceAndDate) {
        this.measurementsForSourceAndDate = measurementsForSourceAndDate;
    }

    public String getMeasurementsForSourceAndFragmentType() {
        return measurementsForSourceAndFragmentType;
    }

    public void setMeasurementsForSourceAndFragmentType(String measurementsForSourceAndFragmentType) {
        this.measurementsForSourceAndFragmentType = measurementsForSourceAndFragmentType;
    }

    public String getMeasurementsForSourceAndType() {
        return measurementsForSourceAndType;
    }

    public void setMeasurementsForSourceAndType(String measurementsForSourceAndType) {
        this.measurementsForSourceAndType = measurementsForSourceAndType;
    }

    public String getMeasurementsForDateAndFragmentType() {
        return measurementsForDateAndFragmentType;
    }

    public void setMeasurementsForDateAndFragmentType(String measurementsForDateAndFragmentType) {
        this.measurementsForDateAndFragmentType = measurementsForDateAndFragmentType;
    }

    public String getMeasurementsForDateAndType() {
        return measurementsForDateAndType;
    }

    public void setMeasurementsForDateAndType(String measurementsForDateAndType) {
        this.measurementsForDateAndType = measurementsForDateAndType;
    }

    public String getMeasurementsForFragmentTypeAndType() {
        return measurementsForFragmentTypeAndType;
    }

    public void setMeasurementsForFragmentTypeAndType(String measurementsForFragmentTypeAndType) {
        this.measurementsForFragmentTypeAndType = measurementsForFragmentTypeAndType;
    }

    public String getMeasurementsForSourceAndDateAndFragmentType() {
        return measurementsForSourceAndDateAndFragmentType;
    }

    public void setMeasurementsForSourceAndDateAndFragmentType(String measurementsForSourceAndDateAndFragmentType) {
        this.measurementsForSourceAndDateAndFragmentType = measurementsForSourceAndDateAndFragmentType;
    }

    public String getMeasurementsForSourceAndDateAndType() {
        return measurementsForSourceAndDateAndType;
    }

    public void setMeasurementsForSourceAndDateAndType(String measurementsForSourceAndDateAndType) {
        this.measurementsForSourceAndDateAndType = measurementsForSourceAndDateAndType;
    }

    public String getMeasurementsForSourceAndFragmentTypeAndType() {
        return measurementsForSourceAndFragmentTypeAndType;
    }

    public void setMeasurementsForSourceAndFragmentTypeAndType(String measurementsForSourceAndFragmentTypeAndType) {
        this.measurementsForSourceAndFragmentTypeAndType = measurementsForSourceAndFragmentTypeAndType;
    }

    public String getMeasurementsForDateAndFragmentTypeAndType() {
        return measurementsForDateAndFragmentTypeAndType;
    }

    public void setMeasurementsForDateAndFragmentTypeAndType(String measurementsForDateAndFragmentTypeAndType) {
        this.measurementsForDateAndFragmentTypeAndType = measurementsForDateAndFragmentTypeAndType;
    }

    public String getMeasurementsForSourceAndDateAndFragmentTypeAndType() {
        return measurementsForSourceAndDateAndFragmentTypeAndType;
    }

    public void setMeasurementsForSourceAndDateAndFragmentTypeAndType(String measurementsForSourceAndDateAndFragmentTypeAndType) {
        this.measurementsForSourceAndDateAndFragmentTypeAndType = measurementsForSourceAndDateAndFragmentTypeAndType;
    }

}
