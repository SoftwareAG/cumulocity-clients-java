package com.cumulocity.sdk.client.buffering;

import java.net.ConnectException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.HttpMethod;

import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.sun.jersey.api.client.ClientHandlerException;

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
                    service.addResponse(processingRequest.getId(), sendRequest(processingRequest.getEntity()));
                }
            }

            private Result sendRequest(BufferedRequest httpPostRequest) {
                Result result = new Result();
                while (true) {
                    try {
                        Object response = doSendRequest(httpPostRequest);
                        result.setResponse(response);
                        return result;
                    } catch (SDKException e) {
                        if (e.getHttpStatus() <= 500) {
                            result.setException(e);
                            return result;
                        }
                        //platform is down
                        waitForPlatform();
                    } catch (ClientHandlerException e) {
                        if (e.getCause() != null && e.getCause() instanceof ConnectException) {
                            //lack of connection
                            waitForConnection();
                        } else {
                            result.setException(new RuntimeException("Exception occured while processing buffered request: ", e));
                            return result;
                        }
                    } catch (Exception e) {
                        result.setException(new RuntimeException("Exception occured while processing buffered request: ", e));
                        return result;
                    }
                }
            }
            
            private void waitForPlatform() {
                try {
                    Thread.sleep(5*60*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("", e);
                }
            }

            private void waitForConnection() {
                try {
                    Thread.sleep(30*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("", e);
                }
            }

            private Object doSendRequest(BufferedRequest httpPostRequest) {
                String method = httpPostRequest.getMethod();
                if (method == HttpMethod.POST) {
                    return restConnector.post(httpPostRequest.getPath(), httpPostRequest.getMediaType(), httpPostRequest.getRepresentation());
                } else if (method == HttpMethod.PUT) {
                    return restConnector.put(httpPostRequest.getPath(), httpPostRequest.getMediaType(), httpPostRequest.getRepresentation());
                } else {
                    throw new IllegalArgumentException("This method is not supported in buffering processor: " + method);
                }
            }
        });
    }

}
