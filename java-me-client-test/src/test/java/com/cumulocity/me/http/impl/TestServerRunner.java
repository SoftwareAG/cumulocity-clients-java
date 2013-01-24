/*
 * ATM Software MediaPortal 2.0
 *
 * Copyright 2010 - 2011 ATM Software
 * Author: D. Kaczyński
 */
package com.cumulocity.me.http.impl;

import org.eclipse.jetty.server.Server;

public class TestServerRunner implements Runnable {

	private Server server;
	private AfterStartCallback callback;
	
	public TestServerRunner(Server server) {
		this(server,  null);
	}
	
	public TestServerRunner(Server server, AfterStartCallback callback) {
		this.server = server;
		this.callback = callback;
	}
	
	/** {@inheritDoc} */
	@Override
	public void run() {
		try {
			server.start();
			if (callback != null) {
				callback.execute();
			}
			server.join();
		} catch (Exception e) {
			throw new RuntimeException("Error running server!", e);
		}
	}
	
	public Server getServer() {
		return server;
	}
	
	public static interface AfterStartCallback {
		
		void execute() throws Exception;
	}
}
