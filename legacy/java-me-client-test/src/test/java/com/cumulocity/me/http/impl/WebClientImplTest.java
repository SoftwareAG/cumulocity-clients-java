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

import static javax.microedition.io.HttpConnection.HTTP_CREATED;
import static javax.microedition.io.HttpConnection.HTTP_INTERNAL_ERROR;
import static javax.microedition.io.HttpConnection.HTTP_NO_CONTENT;
import static javax.microedition.io.HttpConnection.HTTP_OK;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestHandler;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebException;
import com.cumulocity.me.http.WebExceptionHandler;
import com.cumulocity.me.http.WebMethod;
import com.cumulocity.me.http.WebRequestWriter;
import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.http.WebResponseReader;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.rest.representation.ResourceRepresentation;
import com.cumulocity.me.rest.representation.ErrorMessageRepresentation;

public class WebClientImplTest extends BaseHttpTestCase {

    private static final String TEST_CONTENT = "Hello world!";
    private static final String TEST_HEADER = "X-Test-Header";
    private static final String TEST_HEADER_VALUE = "test-header-value";
    private static final String TEST_RESPONSE = "Hello world back!";

    WebRequestWriter requestWriter = new TestWriter();
    WebResponseReader responseReader = new TestReader();
    WebExceptionHandler exceptionHandler = new TestWebExceptionHandler();

    WebClient client = new WebClientImpl(requestWriter, responseReader, exceptionHandler);
    
    WebRequestImpl request = null;
    Map requestHeaders = new HashMap();
    Class<?> responseEntityType = ErrorMessageRepresentation.class;
    
    @Override
    protected void setUpServletContext(ServletContextHandler servletContext) throws Exception {
        requestHeaders.put(TEST_HEADER, TEST_HEADER_VALUE);
        
        addServlet(servletContext, "/testGet", new TestHandler(HTTP_OK), HttpMethod.GET);
        addServlet(servletContext, "/testPost", new TestHandlerWithBody(HTTP_CREATED), HttpMethod.POST);
        addServlet(servletContext, "/testPut", new TestHandlerWithBody(HTTP_OK), HttpMethod.POST);
        addServlet(servletContext, "/testDel", new TestHandler(HTTP_NO_CONTENT), HttpMethod.POST);
    }
    
    @Test
    public void shouldGet() throws Exception {
        request = new WebRequestImpl(WebMethod.GET, serverURI() + "/testGet", requestHeaders, null);
        
        TestRepresentation representation = (TestRepresentation) client.handle(request, HTTP_OK, responseEntityType);

        assertThat(representation.getValue()).isEqualTo(TEST_RESPONSE);
    }
    
    @Test
    public void shouldPost() throws Exception {
        request = new WebRequestImpl(WebMethod.POST, serverURI() + "/testPost", requestHeaders, TEST_CONTENT.getBytes());
        
        TestRepresentation representation = (TestRepresentation) client.handle(request, HTTP_CREATED, responseEntityType);

        assertThat(representation.getValue()).isEqualTo(TEST_RESPONSE);
    }
    
    @Test
    public void shouldPut() throws Exception {
        request = new WebRequestImpl(WebMethod.PUT, serverURI() + "/testPut", requestHeaders, TEST_CONTENT.getBytes());
        
        TestRepresentation representation = (TestRepresentation) client.handle(request, HTTP_OK, responseEntityType);

        assertThat(representation.getValue()).isEqualTo(TEST_RESPONSE);
    }
    
    @Test
    public void shouldDelete() throws Exception {
        request = new WebRequestImpl(WebMethod.DELETE, serverURI() + "/testDel", requestHeaders, null);
       
        WebResponse response = client.handle(request);

        assertThat(response.getStatus()).isEqualTo(HTTP_NO_CONTENT);
    }
    
    private static class TestRepresentation implements ResourceRepresentation {
        
        private String value;

        public TestRepresentation(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    private static class TestWriter implements WebRequestWriter {
        public byte[] write(WebMethod method, ResourceRepresentation entity) {
            return ((TestRepresentation) entity).getValue().getBytes();
        }
    }
    
    private static class TestReader implements WebResponseReader {
        @SuppressWarnings("rawtypes")
        public ResourceRepresentation read(WebResponse response, int expectedStatus, Class expectedEntityType) {
            byte[] data = response.getData();
            return new TestRepresentation(data == null ? null : new String(data));
        }
    }
    
    private static class TestHandler implements HttpRequestHandler {
        
        private final int responseStatus;

        public TestHandler(int responseStatus) {
            this.responseStatus = responseStatus;
        }
        
        @Override
        public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            if (isValid(request)) {
                response.setStatus(responseStatus);
                response.getWriter().write(TEST_RESPONSE);
            } else {
                response.setStatus(HTTP_INTERNAL_ERROR);
            }
        }

        protected boolean isValid(HttpServletRequest request) throws IOException {
            boolean valid = true;
            if (!"GET".equals(request.getMethod()) & !"POST".equals(request.getMethod())) {
                valid = request.getMethod().equals(request.getHeader("X-HTTP-METHOD"));
            }
            return valid && is(equalTo(TEST_HEADER_VALUE)).matches(request.getHeader(TEST_HEADER));
        }
    }
    
    private static class TestHandlerWithBody extends TestHandler {

        public TestHandlerWithBody(int responseStatus) {
            super(responseStatus);
        }
        
        @Override
        protected boolean isValid(HttpServletRequest request) throws IOException {
            return super.isValid(request) &&
                    is(equalTo(TEST_CONTENT)).matches(getRequestBody(request));
        }
        
        private String getRequestBody(HttpServletRequest request) throws IOException {
            InputStreamReader reader = new InputStreamReader(request.getInputStream());
            char[] buff = new char[TEST_CONTENT.length()];
            reader.read(buff);
            return new String(buff);
        }
    }
    
    private static class TestWebExceptionHandler implements WebExceptionHandler {

        @Override
        public WebResponse handle(WebException exception) {
            throw exception;
        }
        
    }
}
