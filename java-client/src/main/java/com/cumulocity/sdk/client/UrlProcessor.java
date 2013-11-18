package com.cumulocity.sdk.client;

import java.util.HashMap;
import java.util.Map;

public class UrlProcessor {

    public String replaceOrAddQueryParam(String url, Map<String, String> newParams) throws SDKException {
        String[] urlParts = url.split("\\?");
        String partOfUrlWithoutQueryParams = urlParts[0];
        String queryParamsString = (urlParts.length == 2) ? urlParts[1] : "";

        Map<String, String> queryParams = parseQueryParams(queryParamsString);
        queryParams.putAll(newParams);
        return buildUrl(partOfUrlWithoutQueryParams, queryParams);
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

    private Map<String, String> parseQueryParams(String queryParams) {
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
