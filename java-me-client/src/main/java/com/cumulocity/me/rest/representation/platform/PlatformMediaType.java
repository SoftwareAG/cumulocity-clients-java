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
package com.cumulocity.me.rest.representation.platform;

//import javax.ws.rs.core.MediaType;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

/**
 * We follow here convention from {@link MediaType} class, where we have both {@link MediaType}
 * instances, and string representations (with '_TYPE' suffix in name). 
 */
public class PlatformMediaType extends BaseCumulocityMediaType {

    public static final PlatformMediaType PLATFORM_API = new PlatformMediaType("platformApi");

    public static final String PLATFORM_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "platformApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public PlatformMediaType(String string) {
        super(string);
    }
}

