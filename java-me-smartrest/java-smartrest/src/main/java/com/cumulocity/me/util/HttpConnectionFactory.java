package com.cumulocity.me.util;

import java.io.IOException;

public interface HttpConnectionFactory {
	HttpConnection open(String url) throws IOException;
	HttpConnection open(String url, int mode, boolean timeout) throws IOException;
}
