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
package com.cumulocity.me.rest.convert;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.Mockito;

import com.cumulocity.me.rest.json.JSONObject;

public class JSONMatcher extends TypeSafeMatcher<JSONObject> {
    
    public static final JSONMatcher json(String jsonString) {
        return new JSONMatcher(jsonString);
    }
    
    public static final JSONObject jsonObject(String jsonString) {
        return Mockito.argThat(json(jsonString));
    }
    
    private final String jsonString;

    private JSONMatcher(String jsonString) {
        this.jsonString = jsonString;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("a json ").appendText(jsonString);
    }
    
    @Override
    public boolean matchesSafely(JSONObject json) {
        return json.toString().equals(jsonString);
    }
}