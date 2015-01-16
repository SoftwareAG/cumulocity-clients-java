package com.cumulocity.agent.server.servers.standalone;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.Server;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Service;

@Component
public class StandaloneServer implements Server {
    private static final Logger log = LoggerFactory.getLogger(StandaloneServer.class);

    @Value("${server.id}")
    private String applicationId;

    private final Service service = new AbstractExecutionThreadService() {

        private final ExecutorService executor = Executors.newCachedThreadPool();

        @Override
        protected void startUp() throws Exception {
            log.debug("staring {}", applicationId);
        }

        @Override
        protected void shutDown() throws Exception {
            log.debug("stopping {}", applicationId);
            executor.shutdownNow();
        }

        @Override
        protected void run() throws Exception {
            log.debug("started {}", applicationId);
        }

        @Override
        protected Executor executor() {
            return executor;
        }
    };

    public StandaloneServer() {
    }

    @Override
    public void start() {
        service.startAsync();
        service.awaitRunning();
    }

    @Override
    public void stop() {
        service.stopAsync();
        service.awaitTerminated();
    }

}
