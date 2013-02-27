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

import com.cumulocity.me.rest.representation.BaseResourceRepresentation;

public class DeviceControlRepresentation extends BaseResourceRepresentation {

    private OperationCollectionRepresentation operations;

    private String operationsByStatus;

    private String operationsByAgentId;
    
    private String operationsByAgentIdAndStatus;
    
    private String operationsByDeviceId;
    
    private String operationsByDeviceIdAndStatus;

    public OperationCollectionRepresentation getOperations() {
        return operations;
    }

    public void setOperations(OperationCollectionRepresentation operations) {
        this.operations = operations;
    }
    
    public String getOperationsByStatus() {
        return operationsByStatus;
    }

    public void setOperationsByStatus(String operationsByStatus) {
        this.operationsByStatus = operationsByStatus;
    }

    public String getOperationsByAgentId() {
        return operationsByAgentId;
    }

    public void setOperationsByAgentId(String operationsByAgentId) {
        this.operationsByAgentId = operationsByAgentId;
    }

    public String getOperationsByAgentIdAndStatus() {
        return operationsByAgentIdAndStatus;
    }

    public void setOperationsByAgentIdAndStatus(String operationsByAgentIdAndStatus) {
        this.operationsByAgentIdAndStatus = operationsByAgentIdAndStatus;
    }
    
    public String getOperationsByDeviceId() {
        return operationsByDeviceId;
    }

    public void setOperationsByDeviceId(String operationsByDeviceId) {
        this.operationsByDeviceId = operationsByDeviceId;
    }
    
    public String getOperationsByDeviceIdAndStatus() {
        return operationsByDeviceIdAndStatus;
    }

    public void setOperationsByDeviceIdAndStatus(String operationsByDeviceIdAndStatus) {
        this.operationsByDeviceIdAndStatus = operationsByDeviceIdAndStatus;
    }
}
