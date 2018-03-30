package com.cumulocity.agent.packaging.platform.client.impl;

import com.cumulocity.agent.packaging.platform.client.Executor;
import com.cumulocity.agent.packaging.platform.client.Request;
import com.cumulocity.agent.packaging.platform.model.CredentialsConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jersey.repackaged.com.google.common.base.Throwables;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static com.cumulocity.agent.packaging.platform.client.impl.UrlUtils.concat;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class ApacheHttpClientExecutor implements Executor {

    private final ObjectMapper mapper = new ObjectMapper(){{
        setSerializationInclusion(NON_NULL);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

    private final CloseableHttpClient client;

    @Getter
    private final String baseUrl;

    public ApacheHttpClientExecutor(final CredentialsConfiguration credentials) {
        baseUrl = UrlUtils.ensureHttpSchema(credentials.getBaseUrl());
        client = HttpClients.custom()
                .setDefaultCredentialsProvider(new BasicCredentialsProvider() {
                    public Credentials getCredentials(AuthScope authscope) {
                        return new UsernamePasswordCredentials(credentials.getTenant() + "/" + credentials.getUsername(), credentials.getPassword());
                    }
                })
                .build();
    }

    @Override
    public <T> T execute(Request<T> request) {
        try {
            final HttpUriRequest httpRequest = build(request);
            final CloseableHttpResponse response = client.execute(httpRequest);

            if (response.getStatusLine().getStatusCode() >= 400) {
                throw new RuntimeException(String.valueOf(response.getStatusLine()) + " - " + result(String.class, response));
            }

            return result(request.getResult(), response);
        } catch (final IOException ex) {
            throw Throwables.propagate(ex);
        }
    }

    private <T> T result(Class<T> result, CloseableHttpResponse response) throws IOException {
        if (response.getEntity() != null) {
            final String value = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            if (String.class.equals(result)) {
                return (T) value;
            }
            if (StringUtils.isNotBlank(value)) {
                return mapper.readValue(value, result);
            }
        }
        return null;
    }

    private HttpUriRequest build(Request request) throws JsonProcessingException, UnsupportedEncodingException {
        if (Request.Method.GET.equals(request.getMethod())) {
            final HttpGet result = new HttpGet(concat(getBaseUrl(), request.getPath()));
            result.setHeader("Accept", "application/json");
            return result;
        }
        if (Request.Method.DELETE.equals(request.getMethod())) {
            return new HttpDelete(concat(getBaseUrl(), request.getPath()));
        }
        if (Request.Method.POST.equals(request.getMethod())) {
            final HttpPost result = new HttpPost(concat(getBaseUrl(), request.getPath()));
            result.setHeader("Accept", "application/json");

            if (request.getMultipartBody() != null) {
                final HttpEntity entity = MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .addBinaryBody("file", request.getMultipartBody())
                        .build();
                result.setEntity(entity);
            }
            if (request.getJsonBody() != null) {
                final String string = mapper.writeValueAsString(request.getJsonBody());
                result.setHeader("Content-Type", "application/json");
                final StringEntity entity = new StringEntity(string);
                result.setEntity(entity);
            }
            return result;
        }

        throw new IllegalArgumentException("Unsupported method " + request.getMethod());
    }
}
