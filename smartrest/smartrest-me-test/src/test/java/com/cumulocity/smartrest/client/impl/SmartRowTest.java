package com.cumulocity.smartrest.client.impl;

import org.junit.Assert;
import org.junit.Test;

public class SmartRowTest {

    @Test
    public void createSmartRow() {
        SmartRow row = SmartRow.create("200,1,2,3");
        
        Assert.assertEquals(200, row.getMessageId());
        Assert.assertEquals(1, row.getRowNumber());
        Assert.assertEquals(2, row.getData().length);
    }
    
    @Test
    public void createSmartRowWithoutRowNumber() {
        SmartRow row = SmartRow.create("200,1");
        
        Assert.assertEquals(200, row.getMessageId());
        Assert.assertEquals(-1, row.getRowNumber());
        Assert.assertEquals(1, row.getData().length);
    }
    
    @Test
    public void notCreateEmptyRow() {
        SmartRow row = SmartRow.create("");
        
        Assert.assertNull(row);
    }
    
    @Test
    public void createRowWithOnlyOneValue() {
        SmartRow row = SmartRow.create("12345");
        
        Assert.assertEquals(0, row.getMessageId());
        Assert.assertEquals(-1, row.getRowNumber());
        Assert.assertEquals(1, row.getData().length);
    }
    
    @Test
    public void parseRowToString() {
        String response = "200,1,2,3";
        SmartRow row = SmartRow.create(response);
        
        Assert.assertEquals(response,row.toString());
    }
}
