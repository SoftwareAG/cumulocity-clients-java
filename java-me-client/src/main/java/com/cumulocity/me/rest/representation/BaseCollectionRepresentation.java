package com.cumulocity.me.rest.representation;

//import org.svenson.JSONProperty;



/**
 * Common Base Class for all Collection Representations. The actual items are intentionally not part  of this class because
 * the (json) name varies from Collection to Collection and the Mapping has to be defined for each Collection Type separately because
 * the a generic Type Parameter would not be available at runtime.
 */
public class BaseCollectionRepresentation  extends BaseCumulocityResourceRepresentation {

    private PageStatisticsRepresentation pageStatistics;
    private String prev;
    private String next;
    
    public BaseCollectionRepresentation() {
        super();
    }

    public PageStatisticsRepresentation getPageStatistics() {
        return pageStatistics;
    }

    public void setPageStatistics(PageStatisticsRepresentation pageStatistics) {
        this.pageStatistics = pageStatistics;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
 
}
