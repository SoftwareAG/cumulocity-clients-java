package com.cumulocity.agent.server.servers;

import static com.google.common.base.Throwables.propagate;

import java.io.IOException;
import java.util.List;

import org.glassfish.grizzly.Context;
import org.glassfish.grizzly.Processor;
import org.glassfish.grizzly.nio.NIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cumulocity.agent.server.Server;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;

public class BinaryServer implements Server {
    @Value("${server.host:0.0.0.0}")
    private String host;

    @Value("${server.port:80}")
    private  int port;
    
    @Value("${server.id}")
    private  String applicationId;
    
    private final List<BinaryServerConfigurator> configurators;

    private final Service service = new AbstractService() {

        private NIOTransport server;

        @Override
        protected void doStart() {
            final TCPNIOTransportBuilder builder = TCPNIOTransportBuilder.newInstance();
            
            builder.setName(applicationId);
            
            for(BinaryServerConfigurator configurator : configurators) {
                configurator.configure(builder);
            }

            
            server = builder.build();
            try {
                server.bind(host, port);
                server.start();
            } catch (IOException e) {
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
    public BinaryServer( List<BinaryServerConfigurator> configurators) {
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

}
