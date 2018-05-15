package com.cumulocity.agent.packaging.uploadMojo.platform.client;

/**
 * Executes http request.
 */
public interface Executor {
    <T> T execute(Request<T> request);
}