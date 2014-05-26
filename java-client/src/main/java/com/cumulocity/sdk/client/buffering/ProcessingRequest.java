package com.cumulocity.sdk.client.buffering;


public class ProcessingRequest {

    private final long id;

    private final BufferedRequest request;

    public ProcessingRequest(long id, BufferedRequest request) {
        this.id = id;
        this.request = request;
    }

    public long getId() {
        return id;
    }

    public BufferedRequest getRequest() {
        return request;
    }

    public static ProcessingRequest parse(String name, String content) {
        return new ProcessingRequest(Long.parseLong(name), BufferedRequest.parseCsvString(content));
    }
}
