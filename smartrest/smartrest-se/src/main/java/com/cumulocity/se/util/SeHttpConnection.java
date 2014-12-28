package com.cumulocity.se.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.cumulocity.me.util.HttpConnection;

public class SeHttpConnection implements HttpConnection {
	private HttpURLConnection connection;
	
	SeHttpConnection(String url) throws IOException {
        this.connection = (HttpURLConnection) new URL(url).openConnection();
	}
	 
	SeHttpConnection(String url, int mode, boolean timeout) throws IOException {
		this(url);
	}
	
	public void setRequestMethod(String method) throws IOException {
		connection.setRequestMethod(method);
	}

	public void setRequestProperty(String string, String authorization) throws IOException {
		connection.setRequestProperty(string, authorization);
	}

	public OutputStream openOutputStream() throws IOException {
		return connection.getOutputStream();
	}

	public int getResponseCode() throws IOException {
		return connection.getResponseCode();
	}

	public String getResponseMessage() throws IOException {
		return connection.getResponseMessage();
	}

	public InputStream openInputStream() throws IOException {
		return connection.getInputStream();
	}

	public void close() throws IOException {
		connection.disconnect(); // Close not existing
	}
}
