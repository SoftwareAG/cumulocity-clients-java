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
