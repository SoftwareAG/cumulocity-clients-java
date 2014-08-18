package com.cumulocity.agent.server.servers.binary;

import static com.google.common.base.Throwables.propagate;

import java.io.IOException;
import java.util.List;

import org.glassfish.grizzly.nio.NIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import com.cumulocity.agent.server.Server;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;

public class BinaryServer implements Server, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(BinaryServer.class);

    @Value("${server.host:0.0.0.0}")
    private String host;

    @Value("${server.port}")
    private int port;

    @Value("${server.id}")
    private String applicationId;

    private ApplicationContext context;

    private final List<BinaryServerConfigurator> configurators;

    private final Service service = new AbstractService() {

        private NIOTransport server;

        @Override
        protected void doStart() {
            final TCPNIOTransportBuilder builder = TCPNIOTransportBuilder.newInstance();

            builder.setName(applicationId);

            builder.setWorkerThreadPoolConfig(ThreadPoolConfig.defaultConfig().setPoolName("Grizzly-worker").setCorePoolSize(10)
                    .setMaxPoolSize(100));
            for (BinaryServerConfigurator configurator : configurators) {
                configurator.configure(builder);
            }

            server = builder.build();
            try {
                log.debug("staring server on {}:{}", host, port);
                server.bind(host, port);
                server.start();
                log.info("stared server on {}:{}", host, port);
            } catch (IOException e) {
                ((AbstractApplicationContext) context).close();
                server.shutdown();
                throw propagate(e);
            }
        }

        @Override
        protected void doStop() {
            try {
                server.shutdownNow();
            } catch (IOException e) {
                throw propagate(e);
            }
        }
    };

    @Autowired
    public BinaryServer(List<BinaryServerConfigurator> configurators) {
        this.configurators = configurators;
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
