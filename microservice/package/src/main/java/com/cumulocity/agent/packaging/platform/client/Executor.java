package com.cumulocity.agent.packaging.platform.client;

/**
 * Executes http request.
 *
 * todo I provided only apache http client implementation (does it make sense to create wagon based impl???)
 */
public interface Executor {
    <T> T execute(Request<T> request);
}