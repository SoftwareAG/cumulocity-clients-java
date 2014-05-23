package com.cumulocity.sdk.client.buffering;


public class ProcessingRequest {

    private final long id;

    private final HTTPPostRequest request;

    public ProcessingRequest(long id, HTTPPostRequest request) {
        this.id = id;
        this.request = request;
    }

    public long getId() {
        return id;
    }

    public HTTPPostRequest getEntity() {
        return request;
    }

    public static ProcessingRequest parse(String name, String content) {
        return new ProcessingRequest(Long.parseLong(name), HTTPPostRequest.parseCsvString(content));
    }
}
