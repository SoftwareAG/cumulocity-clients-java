package com.cumulocity.me.concurrent.model;

import com.cumulocity.me.concurrent.exception.ExecutionException;
import com.cumulocity.me.concurrent.exception.TimeoutException;

public interface Future {

    boolean isDone();

    Object get() throws ExecutionException, InterruptedException;

    Object get(int timeout) throws ExecutionException, TimeoutException, InterruptedException;
}
