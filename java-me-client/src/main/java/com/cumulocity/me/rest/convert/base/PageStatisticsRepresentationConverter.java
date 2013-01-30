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
package com.cumulocity.me.rest.convert.base;

import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.PageStatisticsRepresentation;

public class PageStatisticsRepresentationConverter extends BaseRepresentationConverter {

    private static final String PROP_TOTAL_PAGES = "totalPages";
    private static final String PROP_CURRENT_PAGE = "currentPage";
    private static final String PROP_PAGE_SIZE = "pageSize";

    protected Class supportedRepresentationType() {
        return PageStatisticsRepresentation.class;
    }

    public JSONObject toJson(Object representation) {
        JSONObject json = new JSONObject();
        putInt(json, PROP_TOTAL_PAGES, $(representation).getTotalPages());
        putInt(json, PROP_CURRENT_PAGE, $(representation).getCurrentPage());
        putInt(json, PROP_PAGE_SIZE, $(representation).getPageSize());
        return json;
    }

    public Object fromJson(JSONObject json) {
        PageStatisticsRepresentation representation = new PageStatisticsRepresentation();
        representation.setTotalPages(getInt(json, PROP_TOTAL_PAGES));
        representation.setCurrentPage(getInt(json, PROP_CURRENT_PAGE));
        representation.setPageSize(getInt(json, PROP_PAGE_SIZE));
        return representation;
    }
    
    private PageStatisticsRepresentation $(Object representation) {
        return (PageStatisticsRepresentation) representation;
    }
}
