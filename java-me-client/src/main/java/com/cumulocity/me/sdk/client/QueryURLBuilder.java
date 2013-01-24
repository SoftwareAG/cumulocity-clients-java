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

package com.cumulocity.me.sdk.client;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Collection;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.lang.Set;
import com.cumulocity.me.util.StringUtils;

public class QueryURLBuilder {

    private final Map filters;

    private final List uriTemplates;

    private final String[] optionalParameters;

    private final TemplateUrlParser templateUrlParser;

    public QueryURLBuilder(TemplateUrlParser templateUrlParser, Map filters, List uriTemplates, String[] optionalParameters) {
        this.filters = filters;
        this.uriTemplates = uriTemplates;
        this.optionalParameters = optionalParameters;
        this.templateUrlParser = templateUrlParser;
    }

    public String build() {
        String uri = findQueryURI(uriTemplates, filters.keySet());
        if (uri == null) {
            return null;
        } else {
            return templateUrlParser.replacePlaceholdersWithParams(uri, filters);
        }
    }

    private String findQueryURI(List uriTemplates, Set parameters) {
        Iterator i = uriTemplates.iterator();
        while (i.hasNext()) {
            String uri = (String) i.next();
            List queryParams = getQueryParams(uri);

            List queryDelta = findDelta(parameters, queryParams);
            List paramDelta = findDelta(queryParams, parameters);

            // if no extra parameters found then both are matching
            if (queryDelta.isEmpty() && paramDelta.isEmpty()) {
                return uri;
            }

            // if more parameters then definitely not matching
            if (!paramDelta.isEmpty()) {
                continue;
            }

            // if query has more parameters and the delta is optional we can use this uri.
            if (queryDelta.size() == getOptionalCount(queryDelta)) {
                return removeOptionals(uri, queryDelta);
            }
        }
        return null;
    }

    private String removeOptionals(String uri, List optionals) {
        Iterator i = optionals.iterator();
        while (i.hasNext()) {
            String param = (String) i.next();
            uri = StringUtils.replaceAll(uri, param + "={" + param + "}", "");
        }
        // remove duplicate &
        uri = StringUtils.replaceAll(uri, "&&", "&");
        uri = StringUtils.replaceAll(uri, "?&", "?");
        if (uri.endsWith("&")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        return uri;
    }

    private List findDelta(Collection in, Collection from) {
        List delta = new ArrayList();
        Iterator fromIterator = from.iterator();
        while (fromIterator.hasNext()) {
            String p = (String) fromIterator.next();
            boolean found = false;
            
            Iterator inIterator = in.iterator();
            while (!found && inIterator.hasNext()) {
                found = p.equals((String) inIterator.next());
            }
            
            if (!found) {
                delta.add(p);
            }
        }
        return delta;
    }

    private int getOptionalCount(Collection parameters) {
        int count = 0;
        Iterator i = parameters.iterator();
        while (i.hasNext()) {
            String param = (String) i.next();
            if (isOptional(param)) {
                count++;
            }
        }
        return count;
    }

    private List getQueryParams(String queryUri) {
        String tUri = queryUri.substring(queryUri.indexOf('?') + 1);
        String[] uriParams = StringUtils.split(tUri, "&");
        List queryParams = new ArrayList();
        for (int i = 0; i < uriParams.length; i++) {
            String up = uriParams[i];
            String[] params = StringUtils.split(up, "=");
            queryParams.add(params[0]);
        }
        return queryParams;
    }

    private boolean isOptional(String param) {
        boolean isOptional = false;
        for (int i = 0; i < optionalParameters.length; i++) {
            String optional = optionalParameters[i];
            if (param.equals(optional)) {
                isOptional = true;
                break;
            }
        }
        return isOptional;
    }
}
