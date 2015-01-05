package com.cumulocity.smartrest.util;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class DateUtilsTest {

    String encoded = "1970-01-02T03:46:40.0Z";
    long dateLong = 100000000;
    
    @Test
    public void encodeDate() {
        Date date = new Date(dateLong);
        String dateString = DateUtils.format(date);
        
        Assert.assertEquals(encoded, dateString);
    }
    
    @Test
    public void decodeDate() {
        Date date = DateUtils.parse(encoded);
        
        Assert.assertEquals(dateLong, date.getTime());
    }
}
