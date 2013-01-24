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

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class EventMediaType extends BaseCumulocityMediaType {

    public static final EventMediaType EVENT = new EventMediaType("event");

    public static final String EVENT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "event+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final EventMediaType EVENT_COLLECTION = new EventMediaType("eventCollection");

    public static final String EVENT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "eventCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final EventMediaType EVENT_API = new EventMediaType("eventApi");

    public static final String EVENTS_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "eventApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public EventMediaType(String string) {
        super(string);
    }
}

