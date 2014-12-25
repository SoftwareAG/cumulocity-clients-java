package com.cumulocity.me.util;

import java.io.IOException;

public class MeHttpConnectionFactory implements HttpConnectionFactory {

	public HttpConnection open(String url) throws IOException {
		return new MeHttpConnection(url);
	}

	public HttpConnection open(String url, int mode, boolean timeout) throws IOException {
		return new MeHttpConnection(url, mode, timeout);
	}
}
