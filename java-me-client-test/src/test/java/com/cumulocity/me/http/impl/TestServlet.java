/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
