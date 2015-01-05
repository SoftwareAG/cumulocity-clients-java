package com.cumulocity.smartrest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;

public class MeHttpConnection implements HttpConnection {
	private javax.microedition.io.HttpConnection connection;
	
	 MeHttpConnection(String url) throws IOException {
		this.connection = (javax.microedition.io.HttpConnection) Connector.open(url);
	}
	 
	MeHttpConnection(String url, int mode, boolean timeout) throws IOException {
		this.connection = (javax.microedition.io.HttpConnection) Connector.open(url, mode, timeout);
	}
	
	public void setRequestMethod(String method) throws IOException {
		connection.setRequestMethod(method);
	}

	public void setRequestProperty(String string, String authorization) throws IOException {
		connection.setRequestProperty(string, authorization);
	}

	public OutputStream openOutputStream() throws IOException {
		return connection.openOutputStream();
	}

	public int getResponseCode() throws IOException {
		return connection.getResponseCode();
	}

	public String getResponseMessage() throws IOException {
		return connection.getResponseMessage();
	}

	public InputStream openInputStream() throws IOException {
		return connection.openInputStream();
	}

	public void close() throws IOException {
		connection.close();
	}
}
