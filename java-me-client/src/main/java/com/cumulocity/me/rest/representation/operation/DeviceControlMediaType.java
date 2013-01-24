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
package com.cumulocity.me.rest.representation.operation;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class DeviceControlMediaType extends BaseCumulocityMediaType {
    
    public static final DeviceControlMediaType OPERATION = new DeviceControlMediaType("operation");

    public static final String OPERATION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "operation+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType OPERATION_COLLECTION = new DeviceControlMediaType("operationCollection");

    public static final String OPERATION_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "operationCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType DEVICE_CONTROL_API = new DeviceControlMediaType("devicecontrolApi");

    public static final String DEVICE_CONTROL_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "devicecontrolApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public DeviceControlMediaType(String string) {
        super(string);
    }
}
