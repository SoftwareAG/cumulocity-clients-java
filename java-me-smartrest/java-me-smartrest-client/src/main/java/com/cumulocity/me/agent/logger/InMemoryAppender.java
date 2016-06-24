package com.cumulocity.me.agent.logger;

import net.sf.microlog.core.Level;

import java.util.Vector;

public class InMemoryAppender extends AbstractAppender {
    private final Vector vector = new Vector();

    public synchronized void doLog(String clientID, String name, long time, Level level, Object message, Throwable t) {
        vector.addElement(getFormatter().format(clientID, name, time, level, message, t));
    }

    public String getMessage(int index) {
        return (String) vector.elementAt(index);
    }
}
