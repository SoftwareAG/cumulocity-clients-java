package com.cumulocity.me.http;

public final class WebMethod {
    
    public static final WebMethod GET = new WebMethod("GET", true);
    public static final WebMethod POST = new WebMethod("POST", true);
    public static final WebMethod PUT = new WebMethod("PUT", false);
    public static final WebMethod DELETE = new WebMethod("DELETE", false);
    
    private final String name;
    private final boolean supportedByJ2me;

    private WebMethod(String name, boolean supportedByJ2me) {
        this.name = name;
        this.supportedByJ2me = supportedByJ2me;
    }
    
    public String getName() {
        return name;
    }

    public boolean isSupportedByJ2me() {
        return supportedByJ2me;
    }
}