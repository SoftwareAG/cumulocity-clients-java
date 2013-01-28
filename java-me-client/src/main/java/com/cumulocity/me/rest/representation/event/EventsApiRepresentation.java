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
package com.cumulocity.me.rest.representation.event;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class EventsApiRepresentation extends BaseCollectionRepresentation {

    private EventCollectionRepresentation events;

    private String eventsForType;

    private String eventsForSource;

    private String eventsForSourceAndType;

    private String eventsForTime;

    private String eventsForSourceAndTime;

    private String eventsForTimeAndType;

    private String eventsForSourceAndTimeAndType;

    private String eventsForFragmentType;

    private String eventsForSourceAndFragmentType;

    private String eventsForDateAndFragmentType;

    private String eventsForFragmentTypeAndType;

    private String eventsForSourceAndDateAndFragmentType;

    private String eventsForSourceAndFragmentTypeAndType;

    private String eventsForDateAndFragmentTypeAndType;

    private String eventsForSourceAndDateAndFragmentTypeAndType;

    public EventCollectionRepresentation getEvents() {
        return events;
    }

    public void setEvents(EventCollectionRepresentation events) {
        this.events = events;
    }

    public String getEventsForType() {
        return eventsForType;
    }

    public void setEventsForType(String eventsForType) {
        this.eventsForType = eventsForType;
    }

    public String getEventsForSource() {
        return eventsForSource;
    }

    public void setEventsForSource(String eventsForSource) {
        this.eventsForSource = eventsForSource;
    }

    public String getEventsForSourceAndType() {
        return eventsForSourceAndType;
    }

    public void setEventsForSourceAndType(String eventsForSourceAndType) {
        this.eventsForSourceAndType = eventsForSourceAndType;
    }

    public String getEventsForTime() {
        return eventsForTime;
    }

    public void setEventsForTime(String eventsForTime) {
        this.eventsForTime = eventsForTime;
    }

    public String getEventsForSourceAndTime() {
        return eventsForSourceAndTime;
    }

    public void setEventsForSourceAndTime(String eventsForSourceAndTime) {
        this.eventsForSourceAndTime = eventsForSourceAndTime;
    }

    public String getEventsForTimeAndType() {
        return eventsForTimeAndType;
    }

    public void setEventsForTimeAndType(String eventsForTimeAndType) {
        this.eventsForTimeAndType = eventsForTimeAndType;
    }

    public String getEventsForSourceAndTimeAndType() {
        return eventsForSourceAndTimeAndType;
    }

    public void setEventsForSourceAndTimeAndType(String eventsForSourceAndTimeAndType) {
        this.eventsForSourceAndTimeAndType = eventsForSourceAndTimeAndType;
    }

    public String getEventsForFragmentType() {
        return eventsForFragmentType;
    }

    public void setEventsForFragmentType(String eventsForFragmentType) {
        this.eventsForFragmentType = eventsForFragmentType;
    }

    public String getEventsForSourceAndFragmentType() {
        return eventsForSourceAndFragmentType;
    }

    public void setEventsForSourceAndFragmentType(String eventsForSourceAndFragmentType) {
        this.eventsForSourceAndFragmentType = eventsForSourceAndFragmentType;
    }

    public String getEventsForDateAndFragmentType() {
        return eventsForDateAndFragmentType;
    }

    public void setEventsForDateAndFragmentType(String eventsForDateAndFragmentType) {
        this.eventsForDateAndFragmentType = eventsForDateAndFragmentType;
    }

    public String getEventsForFragmentTypeAndType() {
        return eventsForFragmentTypeAndType;
    }

    public void setEventsForFragmentTypeAndType(String eventsForFragmentTypeAndType) {
        this.eventsForFragmentTypeAndType = eventsForFragmentTypeAndType;
    }

    public String getEventsForSourceAndDateAndFragmentType() {
        return eventsForSourceAndDateAndFragmentType;
    }

    public void setEventsForSourceAndDateAndFragmentType(String eventsForSourceAndDateAndFragmentType) {
        this.eventsForSourceAndDateAndFragmentType = eventsForSourceAndDateAndFragmentType;
    }

    public String getEventsForSourceAndFragmentTypeAndType() {
        return eventsForSourceAndFragmentTypeAndType;
    }

    public void setEventsForSourceAndFragmentTypeAndType(String eventsForSourceAndFragmentTypeAndType) {
        this.eventsForSourceAndFragmentTypeAndType = eventsForSourceAndFragmentTypeAndType;
    }

    public String getEventsForDateAndFragmentTypeAndType() {
        return eventsForDateAndFragmentTypeAndType;
    }

    public void setEventsForDateAndFragmentTypeAndType(String eventsForDateAndFragmentTypeAndType) {
        this.eventsForDateAndFragmentTypeAndType = eventsForDateAndFragmentTypeAndType;
    }

    public String getEventsForSourceAndDateAndFragmentTypeAndType() {
        return eventsForSourceAndDateAndFragmentTypeAndType;
    }

    public void setEventsForSourceAndDateAndFragmentTypeAndType(String eventsForSourceAndDateAndFragmentTypeAndType) {
        this.eventsForSourceAndDateAndFragmentTypeAndType = eventsForSourceAndDateAndFragmentTypeAndType;
    }

//    @JSONProperty(ignore = true)
    public List getURITemplates() {
        List uriTemplates = new ArrayList();
        uriTemplates.add(this.getEventsForDateAndFragmentType());
        uriTemplates.add(this.getEventsForDateAndFragmentTypeAndType());
        uriTemplates.add(this.getEventsForFragmentType());
        uriTemplates.add(this.getEventsForFragmentTypeAndType());
        uriTemplates.add(this.getEventsForSource());
        uriTemplates.add(this.getEventsForSourceAndDateAndFragmentType());
        uriTemplates.add(this.getEventsForSourceAndDateAndFragmentTypeAndType());
        uriTemplates.add(this.getEventsForSourceAndFragmentType());
        uriTemplates.add(this.getEventsForSourceAndFragmentTypeAndType());
        uriTemplates.add(this.getEventsForSourceAndTime());
        uriTemplates.add(this.getEventsForSourceAndTimeAndType());
        uriTemplates.add(this.getEventsForSourceAndType());
        uriTemplates.add(this.getEventsForTime());
        uriTemplates.add(this.getEventsForTimeAndType());
        uriTemplates.add(this.getEventsForType());
        return uriTemplates;
    }

}
