package com.cumulocity.rest.representation;

import org.svenson.JSONProperty;

public class PageStatisticsRepresentation extends AbstractExtensibleRepresentation {

    private Integer totalPages;

    private int pageSize;

    private int currentPage;

    private Long totalElements;

    public PageStatisticsRepresentation() {
    }

    public PageStatisticsRepresentation(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @JSONProperty(ignoreIfNull = true)
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @JSONProperty(ignoreIfNull = true)
    public int getPageSize() {
        return pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @JSONProperty(ignoreIfNull = true)
    public int getCurrentPage() {
        return currentPage;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    @JSONProperty(ignoreIfNull = true)
    public Long getTotalElements() {
        return totalElements;
    }

}