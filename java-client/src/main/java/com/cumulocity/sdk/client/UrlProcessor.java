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

package com.cumulocity.sdk.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UrlProcessor {

	public String removeQueryParam(String url, Collection<String> params) throws SDKException {
		URLParts urlParts = URLParts.asParts(url);
		Map<String, String> queryParams = urlParts.getQueryParams();
		
		for (String param : params) {
			queryParams.remove(param);
		}
		
		return buildUrl(urlParts.getPartOfUrlWithoutQueryParams(), queryParams);
	}
	
    public String replaceOrAddQueryParam(String url, Map<String, String> newParams) throws SDKException {
    	URLParts urlParts = URLParts.asParts(url);
    	Map<String, String> queryParams = urlParts.getQueryParams();
    	queryParams.putAll(newParams);
        
        return buildUrl(urlParts.getPartOfUrlWithoutQueryParams(), queryParams);
    }

    private String buildUrl(String urlBeginning, Map<String, String> queryParams) {
        StringBuilder builder = new StringBuilder(urlBeginning);
        if (queryParams.size() > 0) {
            builder.append("?"); //add ? only if query params exist
            boolean addingfirstPair = true; //this var is used only to make sure & after last pair is not added
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (!addingfirstPair) {
                    builder.append("&");
                }
                builder.append(entry.getKey()).append("=").append(entry.getValue());
                addingfirstPair = false;
            }
        }

        return builder.toString();
    }

	private static class URLParts {

		private final String partOfUrlWithoutQueryParams;
		private final Map<String, String> queryParams;

		static URLParts asParts(String url) {
			String[] urlParts = url.split("\\?");
			String partOfUrlWithoutQueryParams = urlParts[0];
			String queryParamsString = (urlParts.length == 2) ? urlParts[1] : "";

			Map<String, String> queryParams = parseQueryParams(queryParamsString);
			return new URLParts(partOfUrlWithoutQueryParams, queryParams);
		}

		private URLParts(String partOfUrlWithoutQueryParams, Map<String, String> queryParams) {
			this.partOfUrlWithoutQueryParams = partOfUrlWithoutQueryParams;
			this.queryParams = queryParams;
		}

		public String getPartOfUrlWithoutQueryParams() {
			return partOfUrlWithoutQueryParams;
		}

		public Map<String, String> getQueryParams() {
			return queryParams;
		}

		private static Map<String, String> parseQueryParams(String queryParams) {
			Map<String, String> result = new HashMap<String, String>();
			String[] pairs = queryParams.split("&");
			for (String pair : pairs) {
				String[] items = pair.split("=");
				if (items.length == 2) {
					result.put(items[0], items[1]);
				}
			}

			return result;
		}
	}
}
