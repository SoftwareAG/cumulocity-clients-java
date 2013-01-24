/*
 * ATM Software MediaPortal 2.0
 *
 * Copyright 2010 - 2011 ATM Software
 * Author: D. Kaczyński
 */
package com.cumulocity.me.http.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestHandler;

public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = -9211568531085450433L;
	
	private HttpRequestHandler handler;
	private Set<HttpMethod> supportedMethods;
	
	public TestServlet(HttpRequestHandler handler) {
		this(handler, HttpMethod.values());
	}
	
	public TestServlet(HttpRequestHandler handler, HttpMethod...supportedMethods) {
		this.handler = handler;
		this.supportedMethods = new HashSet<HttpMethod>(Arrays.asList(supportedMethods));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.GET)) {
			handler.handleRequest(req, resp);
		} else {
			super.doGet(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.POST)) {
			handler.handleRequest(req, resp);
		} else {
			super.doPost(req, resp);
		}
	}
	
	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.HEAD)) {
			handler.handleRequest(req, resp);
		} else {
			super.doHead(req, resp);
		}
	}
	
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.OPTIONS)) {
			handler.handleRequest(req, resp);
		} else {
			super.doOptions(req, resp);
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.PUT)) {
			handler.handleRequest(req, resp);
		} else {
			super.doPut(req, resp);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.DELETE)) {
			handler.handleRequest(req, resp);
		} else {
			super.doDelete(req, resp);
		}
	}
	
	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (supportedMethods.contains(HttpMethod.TRACE)) {
			handler.handleRequest(req, resp);
		} else {
			super.doTrace(req, resp);
		}
	}
}
