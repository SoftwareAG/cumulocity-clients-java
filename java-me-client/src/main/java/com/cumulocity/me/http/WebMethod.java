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
package com.cumulocity.me.http;

public final class WebMethod {
    
    public static final WebMethod GET = new WebMethod("GET", true);
    public static final WebMethod POST = new WebMethod("POST", true);
    public static final WebMethod PUT = new WebMethod("PUT", false);
    public static final WebMethod DELETE = new WebMethod("DELETE", false);
    
    private final String name;
    private final boolean supportedByJ2me;

    private WebMethod(String name, boolean supportedByJ2me) {
        this.name = name;
        this.supportedByJ2me = supportedByJ2me;
    }
    
    public String getName() {
        return name;
    }

    public boolean isSupportedByJ2me() {
        return supportedByJ2me;
    }
}