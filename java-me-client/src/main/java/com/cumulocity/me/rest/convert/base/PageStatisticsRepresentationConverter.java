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
