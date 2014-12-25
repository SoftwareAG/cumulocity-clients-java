package com.cumulocity.se.util;

import java.io.IOException;

import com.cumulocity.me.util.HttpConnection;
import com.cumulocity.me.util.HttpConnectionFactory;

public class SeHttpConnectionFactory implements HttpConnectionFactory {

	public HttpConnection open(String url) throws IOException {
		return new SeHttpConnection(url);
	}

	public HttpConnection open(String url, int mode, boolean timeout) throws IOException {
		return new SeHttpConnection(url, mode, timeout);
	}
}
