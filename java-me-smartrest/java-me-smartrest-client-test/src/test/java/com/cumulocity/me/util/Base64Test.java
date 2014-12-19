package com.cumulocity.me.util;

import junit.framework.Assert;

import org.junit.Test;

public class Base64Test {
    
    
    String auth = "tenant/username:password";
    String encoded = "dGVuYW50L3VzZXJuYW1lOnBhc3N3b3Jk";
    
    @Test
    public void testEncoding() {
        String encoded = Base64.encode(auth.getBytes());
        
        Assert.assertEquals(this.encoded, encoded);
    }
    
    @Test
    public void testDecoding() {
        String decoded = new String(Base64.decode(encoded));
        
        Assert.assertEquals(auth, decoded);
    }
    
    @Test
    public void testBoth() {
        String encoded = Base64.encode(auth.getBytes());
        String decoded = new String(Base64.decode(encoded));
        
        Assert.assertEquals(auth, decoded);
    }
}
