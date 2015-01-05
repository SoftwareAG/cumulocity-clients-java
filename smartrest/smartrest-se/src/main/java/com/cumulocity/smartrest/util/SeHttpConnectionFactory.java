package com.cumulocity.smartrest.util;

import java.io.IOException;

public class SeHttpConnectionFactory implements HttpConnectionFactory {

	public HttpConnection open(String url) throws IOException {
		return new SeHttpConnection(url);
	}

	public HttpConnection open(String url, int mode, boolean timeout) throws IOException {
		return new SeHttpConnection(url, mode, timeout);
	}
}
