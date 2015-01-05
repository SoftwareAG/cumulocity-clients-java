package com.cumulocity.smartrest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpConnection {
	void setRequestMethod(String string) throws IOException;
	void setRequestProperty(String string, String authorization) throws IOException;
	OutputStream openOutputStream() throws IOException;

	int getResponseCode() throws IOException;
	String getResponseMessage() throws IOException;
	InputStream openInputStream() throws IOException;

	void close() throws IOException;
}
