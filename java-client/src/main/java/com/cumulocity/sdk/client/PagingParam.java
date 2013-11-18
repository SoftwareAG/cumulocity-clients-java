package com.cumulocity.sdk.client;

public enum PagingParam implements Param {
    WITH_TOTAL_PAGES("withTotalPages");
    
    private String paramName;

    PagingParam(String paramName) {
        this.paramName = paramName;
    }

    public String getName() {
        return paramName;
    }
}
