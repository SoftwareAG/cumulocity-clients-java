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
package com.cumulocity.me.rest.representation.alarm;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class AlarmMediaType extends BaseCumulocityMediaType {
    
    public static final AlarmMediaType ALARM = new AlarmMediaType("alarm");

    public static final String ALARM_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "alarm+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final AlarmMediaType ALARM_COLLECTION = new AlarmMediaType("alarmCollection");

    public static final String ALARM_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "alarmCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final AlarmMediaType ALARM_API = new AlarmMediaType("alarmApi");

    public static final String ALARM_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "alarmApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public AlarmMediaType(String string) {
        super(string);
    }
}
