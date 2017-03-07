package com.cumulocity.me.concurrent;

import com.cumulocity.me.concurrent.model.Callable;
import com.cumulocity.me.concurrent.model.Future;

public interface SmartExecutorService {
    Future execute(Runnable runnable);
    Future execute(Callable callable);
}
