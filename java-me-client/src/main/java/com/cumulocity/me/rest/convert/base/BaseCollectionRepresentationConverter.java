package com.cumulocity.me.rest.convert.base;

import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.PageStatisticsRepresentation;

public abstract class BaseCollectionRepresentationConverter extends BaseResourceRepresentationConverter {

    private static final String PROP_PAGE_STATISTICS = "statistics";
    private static final String PROP_PREV = "prev";
    private static final String PROP_NEXT = "next";

    protected void basePropertiesToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        super.basePropertiesToJson(representation, json);
        putObject(json, PROP_PAGE_STATISTICS, $(representation).getPageStatistics());
        putString(json, PROP_PREV, $(representation).getPrev());
        putString(json, PROP_NEXT, $(representation).getNext());
    }
    
    protected void basePropertiesFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        super.basePropertiesFromJson(json, representation);
        $(representation).setPageStatistics((PageStatisticsRepresentation) getObject(json, PROP_PAGE_STATISTICS, PageStatisticsRepresentation.class));
        $(representation).setPrev(getString(json, PROP_PREV));
        $(representation).setNext(getString(json, PROP_NEXT));
    }

    private BaseCollectionRepresentation $(BaseCumulocityResourceRepresentation representation) {
        return (BaseCollectionRepresentation) representation;
    }
}
