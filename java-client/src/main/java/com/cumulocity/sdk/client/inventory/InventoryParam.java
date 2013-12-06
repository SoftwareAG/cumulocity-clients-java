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

package com.cumulocity.sdk.client.inventory;

import com.cumulocity.sdk.client.Param;
import com.cumulocity.sdk.client.QueryParam;

public enum InventoryParam implements Param {

    WITH_PARENTS("withParents"),

    SKIP_CHILDREN_NAMES("skipChildrenNames");

    private String paramName;

    private InventoryParam(String paramName) {
        this.paramName = paramName;
    }

    public String getName() {
        return paramName;
    }

    public static QueryParam withParents() {
        return new QueryParam(WITH_PARENTS, Boolean.TRUE.toString());
    }

    public static QueryParam withoutParents() {
        return new QueryParam(WITH_PARENTS, Boolean.FALSE.toString());
    }

    public static QueryParam withChildrenNames() {
        return new QueryParam(SKIP_CHILDREN_NAMES, Boolean.FALSE.toString());
    }

    public static QueryParam withoutChildrenNames() {
        return new QueryParam(SKIP_CHILDREN_NAMES, Boolean.TRUE.toString());
    }
}
