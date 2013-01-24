/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public abstract class PagedCollectionResourceImpl<T extends BaseCollectionRepresentation> extends GenericResourceImpl<T> implements
        PagedCollectionResource<T> {

    private int pageSize = 5;

    private static String PAGE_SIZE_KEY = "pageSize";

    private static String PAGE_NUMBER_KEY = "currentPage";

    private static final Logger LOG = LoggerFactory.getLogger(PagedCollectionResourceImpl.class);

    @Deprecated
    public PagedCollectionResourceImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public PagedCollectionResourceImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url);
        this.pageSize = pageSize;
    }

    @Override
    public T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) throws SDKException {
        if (collectionRepresentation == null || collectionRepresentation.getSelf() == null) {
            throw new SDKException("Unable to determin the Resource URL. ");
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put(PAGE_SIZE_KEY, String.valueOf(pageSize));
        params.put(PAGE_NUMBER_KEY, String.valueOf(pageNumber));

        String url = replaceOrAddQueryParam(collectionRepresentation.getSelf(), params);

        LOG.debug(" URL : " + url);
        return getCollection(url);
    }

    private T getCollection(String url) throws SDKException {
        return restConnector.get(url, getMediaType(), getResponseClass());
    }

    @Override
    public T getNextPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
        if (collectionRepresentation == null) {
            throw new SDKException("Unable to determin the Resource URL. ");
        }
        if (collectionRepresentation.getNext() == null) {
            return null;
        }
        return getCollection(collectionRepresentation.getNext());
    }

    @Override
    public T getPreviousPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
        if (collectionRepresentation == null) {
            throw new SDKException("Unable to determin the Resource URL. ");
        }
        if (collectionRepresentation.getPrev() == null) {
            return null;
        }
        return getCollection(collectionRepresentation.getPrev());
    }

    @SuppressWarnings("deprecation")
    @Override
    public T get() throws SDKException {
        String urlToCall = replaceOrAddQueryParam(url, Collections.singletonMap(PAGE_SIZE_KEY, String.valueOf(pageSize)));
        return restConnector.get(urlToCall, getMediaType(), getResponseClass());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PagedCollectionResourceImpl)) {
            return false;
        }

        try {
            @SuppressWarnings("rawtypes")
            PagedCollectionResourceImpl another = (PagedCollectionResourceImpl) obj;
            return getMediaType().equals(another.getMediaType()) && getResponseClass().equals(another.getResponseClass())
                    && pageSize == another.pageSize && url.equals(another.url);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public int hashCode() {
        return getMediaType().hashCode() ^ getResponseClass().hashCode() ^ url.hashCode() ^ pageSize;
    }

    private String replaceOrAddQueryParam(String url, Map<String, String> newParams) throws SDKException {
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
