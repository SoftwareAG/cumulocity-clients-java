package com.cumulocity.me.agent.restart;

public interface DeviceRestarter {
    void restart() throws RestartException;
}
