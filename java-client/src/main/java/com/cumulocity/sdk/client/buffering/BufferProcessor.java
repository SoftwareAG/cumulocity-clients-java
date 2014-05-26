package com.cumulocity.sdk.client.buffering;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

public class BufferProcessor {

    private ExecutorService executor;

    private PersistentProvider persistentProvider;

    private RestConnector restConnector;

    private BufferRequestService service;

    public BufferProcessor(PersistentProvider persistentProvider, BufferRequestService service, RestConnector restConnector) {
        this.persistentProvider = persistentProvider;
        this.service = service;
        this.restConnector = restConnector;
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public void startProcessing() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ProcessingRequest processingRequest = persistentProvider.poll();
                    service.addResponse(processingRequest.getId(), sendRequest(processingRequest.getRequest()));
                }
            }

            private Result sendRequest(HTTPPostRequest httpPostRequest) {
                Result result = new Result();
                while (true) {
                    try {
                        Object response = restConnector.post(httpPostRequest.getPath(), httpPostRequest.getMediaType(), httpPostRequest.getRepresentation());
                        result.setResponse(response);
                        return result;
                    } catch (SDKException e) {
                        if (e.getHttpStatus() <= 500) {
                            result.setException(e);
                            return result;
                        }
                    } catch (Exception e) {
                        result.setException(new RuntimeException("Exception occured while processing buffered request: ", e));
                        return result;
                    }
                }
            }
        });
    }

}
