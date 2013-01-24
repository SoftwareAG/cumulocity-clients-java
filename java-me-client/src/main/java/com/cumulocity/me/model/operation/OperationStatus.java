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
package com.cumulocity.me.model.operation;


public class OperationStatus {

    public static final OperationStatus PENDING = new OperationStatus("PENDING");

    public static final OperationStatus SUCCESSFUL = new OperationStatus("SUCCESSFUL");

    public static final OperationStatus FAILED = new OperationStatus("FAILED");

    public static final OperationStatus EXECUTING = new OperationStatus("EXECUTING");

    private String name;

    private OperationStatus(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static OperationStatus valueOf(String status) {
        if (status.equals(PENDING.name)) {
            return PENDING;
        }
        else if (status.equals(SUCCESSFUL.name)) {
            return SUCCESSFUL;
        }
        else if (status.equals(FAILED.name)) {
            return FAILED;
        }
        else if (status.equals(EXECUTING.name)) {
            return EXECUTING;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    public String toString() {
        return name;
    }

}
