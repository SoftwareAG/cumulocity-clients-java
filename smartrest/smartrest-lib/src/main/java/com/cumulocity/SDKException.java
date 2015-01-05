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

package com.cumulocity;

import com.cumulocity.smartrest.client.SmartRow;

public class SDKException extends RuntimeException {

    private static final long serialVersionUID = -7264072363017210113L;
    
    private int httpStatusCode = -1;
    
    public SDKException(String string) {
        super(string);
    }

    public SDKException(String string, Throwable t) {
        super(string + "\n" + t.getMessage());
    }

    public SDKException(int httpStatusCode, String string) {
        super(string);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatus() {
        return httpStatusCode;
    }
    
    public SDKException(SmartRow row) {
        super(row.toString());
    }
    
    public SDKException(SmartRow[] rows) {
        super(createErrorFromMultipleRows(rows));
    }
    
    private static String createErrorFromMultipleRows(SmartRow[] rows) {
        String exceptions = rows[0].toString();
        for (int i = 1; i < rows.length; i++) {
            exceptions = "\n" + rows[i].toString();
        }
        return exceptions;
    }

}
