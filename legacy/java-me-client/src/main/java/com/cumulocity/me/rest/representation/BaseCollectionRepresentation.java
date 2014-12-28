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
package com.cumulocity.me.rest.representation;

/**
 * Common Base Class for all Collection Representations. The actual items are intentionally not part  of this class because
 * the (json) name varies from Collection to Collection and the Mapping has to be defined for each Collection Type separately because
 * the a generic Type Parameter would not be available at runtime.
 */
public class BaseCollectionRepresentation  extends BaseResourceRepresentation {

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
