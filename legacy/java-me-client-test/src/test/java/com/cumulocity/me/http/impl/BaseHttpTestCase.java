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
package com.cumulocity.me.http.impl;

import java.util.concurrent.Semaphore;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestHandler;

public abstract class BaseHttpTestCase {

    Server server;
    Semaphore serverStartupSemaphore;
    
    protected String serverURI() {
        return "http://localhost:58888";
    }
    
    @Before
    public void startServer() throws Exception {
        server = new Server(58888);
        ServletContextHandler servletContext = new ServletContextHandler(server, "/", false, false);
        setUpServletContext(servletContext);
        
        serverStartupSemaphore = new Semaphore(1);
        serverStartupSemaphore.drainPermits();
        
        new Thread(new TestServerRunner(
                server,
                new TestServerRunner.AfterStartCallback() {
                    @Override
                    public void execute() throws Exception {
                        serverStartupSemaphore.release();
                    }
                }
        )).start();
        serverStartupSemaphore.acquire();
        
        setUpAfterServerStart();
    }
    
    protected abstract void setUpServletContext(ServletContextHandler servletContext) throws Exception;
    
    protected void setUpAfterServerStart() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        server.stop();
        server.join();
    }

    protected void addServlet(ServletContextHandler servletContext, String contextPath, HttpRequestHandler handler, HttpMethod...methods) {
        servletContext.addServlet(new ServletHolder(new TestServlet(handler, methods)), contextPath);
    }
}
