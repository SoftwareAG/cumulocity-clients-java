package com.cumulocity.me.agent.logger;

import java.io.IOException;

public abstract class AbstractAppender extends net.sf.microlog.core.appender.AbstractAppender {

    public void clear() {
    }

    public void close() throws IOException {
    }

    public void open() throws IOException {
    }

    public long getLogSize() {
        return 0;
    }
}
