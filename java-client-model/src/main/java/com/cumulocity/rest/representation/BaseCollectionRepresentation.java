package com.cumulocity.rest.representation;

import org.svenson.JSONProperty;

/**
 * Common Base Class for all Collection Representations. The actual items are intentionally not part  of this class because
 * the (json) name varies from Collection to Collection and the Mapping has to be defined for each Collection Type separately because
 * the a generic Type Parameter would not be available at runtime.
 */
public abstract class BaseCollectionRepresentation<T> extends BaseResourceWithExplainRepresentation implements Iterable<T> {

    private PageStatisticsRepresentation pageStatistics;
    private String prev;
    private String next;
    
    public BaseCollectionRepresentation() {
        super();
    }

    @JSONProperty(value = "statistics", ignoreIfNull = true)
    public PageStatisticsRepresentation getPageStatistics() {
        return pageStatistics;
    }

    public void setPageStatistics(PageStatisticsRepresentation pageStatistics) {
        this.pageStatistics = pageStatistics;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
