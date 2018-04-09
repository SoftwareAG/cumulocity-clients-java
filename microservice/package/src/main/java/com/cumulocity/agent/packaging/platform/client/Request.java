package com.cumulocity.agent.packaging.platform.client;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;
import org.apache.maven.shared.utils.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Request data transfer object without any logic specific to client.
 *
 * Note: No accept header is configurable because by default I use application/json.
 *
 * Note: No content-type header is configurable because by default I use application/json if you provide jsonBody or multipart if you provide mulitpartBody.
 */
@Data
@Wither
@Builder
public class Request<T> {
//    todo no put method implemented
    public enum Method {
        GET,POST,DELETE
    }

    private final Method method;
    private final String path;
    private final Class<T> result;
    private final Object jsonBody;
    private final File multipartBody;
    private final String multipartName;

    public static Request<Map> Get(final String path) {
        return Request.<Map>builder()
                .method(Method.GET)
                .path(path)
                .result(Map.class)
                .build();
    }

    public static Request<Map> Delete(final String path) {
        return Request.<Map>builder()
                .method(Method.DELETE)
                .path(path)
                .result(Map.class)
                .build();
    }

    public static Request<Map> Post(final String path) {
        return Request.<Map>builder()
                .method(Method.POST)
                .path(path)
                .result(Map.class)
                .build();
    }

    public Request<T> withUrlParam(String key, Object value) {
        return withPath(StringUtils.replace(path, key, String.valueOf(value)));
    }

    public <S> Request<S> withResponse(Class<S> clazz) {
        return new Request<S>(method, path, clazz, jsonBody, multipartBody, multipartName);
    }
}