package com.cumulocity.me.util;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.IOException;

public class ConnectorWrapper {

    public HttpConnection open(String url) throws IOException {
        return (HttpConnection) Connector.open(url);
    }

    public HttpConnection open(String url, int mode, boolean timeout) throws IOException {
        return (HttpConnection) Connector.open(url, mode, timeout);
    }
}
