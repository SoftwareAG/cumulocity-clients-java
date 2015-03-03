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

package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.ParamSource;

import static com.cumulocity.model.util.ExtensibilityConverter.classToStringRepresentation;

/**
 * A filter to be used in operation queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code OperationFilter filter = new OperationFilter().byStatus(status).byDevice(deviceId);}
 */
public class OperationFilter extends Filter {

    @ParamSource
    private String fragmentType;

    @ParamSource
    private String status;

    @ParamSource
    private String deviceId;

    @ParamSource
    private String agentId;

    /**
     * Specifies the {@code fragmentType} query parameter
     *
     * @param fragmentClass the class representation of the type of the operation(s)
     * @return the operation filter with {@code fragmentType} set
     */
    public OperationFilter byFragmentType(Class<?> fragmentClass) {
        this.fragmentType = classToStringRepresentation(fragmentClass);
        return this;
    }

    public OperationFilter byFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
        return this;
    }

    /**
     * Specifies the {@code status} query parameter
     *
     * @param status status of the operation(s)
     * @return the operation filter with {@code status} set
     */
    public OperationFilter byStatus(OperationStatus status) {
        this.status = status.toString();
        return this;
    }

    /**
     * Specifies the {@code deviceId} query parameter
     *
     * @param deviceId id of the device associated with the the operations(s)
     * @return the operation filter with {@code deviceId} set
     */
    public OperationFilter byDevice(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    /**
     * Specifies the {@code agentId} query parameter
     *
     * @param agentId id of the agent associated with the the operations(s)
     * @return the operation filter with {@code agentId} set
     */
    public OperationFilter byAgent(String agentId) {
        this.agentId = agentId;
        return this;
    }

    /**
     * @return the {@code status} parameter of the query
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the {@code deviceId} parameter of the query
     */
    public String getDevice() {
        return deviceId;
    }

    /**
     * @return the {@code agentId} parameter of the query
     */
    public String getAgent() {
        return agentId;
    }

    /**
     * @return the {@code fragmentType} parameter of the query
     */
    public String getFragmentType() {
        return fragmentType;
    }
}
