package com.cumulocity.me.agent.logger;

import net.sf.microlog.core.Appender;
import net.sf.microlog.core.Formatter;
import net.sf.microlog.core.Level;
import net.sf.microlog.core.appender.ConsoleAppender;

import java.io.IOException;

public class ProxyAppender implements Appender {

    // todo: is there better approach?
    private static Appender APPENDER = new ConsoleAppender();

    public static synchronized void start(Appender appender) {
        if (appender == null) {
            throw new NullPointerException("Invalid appender.");
        }
        final Formatter formatter = APPENDER.getFormatter();
        APPENDER = appender;
        APPENDER.setFormatter(formatter);
    }

    public static synchronized void stop() {
        start(new ConsoleAppender());
    }

    public ProxyAppender() {
    }

    public void doLog(String clientID, String name, long time, Level logLevel, Object message, Throwable t) {
        APPENDER.doLog(clientID, name, time, logLevel, message, t);
    }

    public void clear() {
        APPENDER.clear();
    }

    public void close() throws IOException {
        APPENDER.close();
    }

    public void open() throws IOException {
        APPENDER.open();
    }

    public boolean isLogOpen() {
        return APPENDER.isLogOpen();
    }

    public long getLogSize() {
        return APPENDER.getLogSize();
    }

    public void setFormatter(Formatter formatter) {
        APPENDER.setFormatter(formatter);
    }

    public Formatter getFormatter() {
        return APPENDER.getFormatter();
    }

    public String[] getPropertyNames() {
        return APPENDER.getPropertyNames();
    }

    public void setProperty(String name, String value) throws IllegalArgumentException {
        APPENDER.setProperty(name, value);
    }

    public Appender getAppender() {
        return APPENDER;
    }
}
