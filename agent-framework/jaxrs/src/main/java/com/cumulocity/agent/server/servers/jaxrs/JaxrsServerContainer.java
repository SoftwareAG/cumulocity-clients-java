package com.cumulocity.agent.server.servers.jaxrs;

import static org.glassfish.grizzly.threadpool.ThreadPoolConfig.defaultConfig;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;

@Component("jaxrsServerContainer")
public class JaxrsServerContainer implements EmbeddedServletContainerFactory {

    private final Logger log = LoggerFactory.getLogger(JaxrsServerContainer.class);

    private final String host;

    private final int port;

    private final String applicationId;

    @Autowired
    public JaxrsServerContainer(@Value("${server.host:0.0.0.0}") String host, @Value("${server.port:80}") int port,
            @Value("${server.contextPath:${application.id}}") String contextPath) {
        this.host = host;
        this.port = port;
        this.applicationId = contextPath;
    }

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
        final HttpServer server = HttpServer.createSimpleServer(null, new InetSocketAddress(host, port));
        //@formatter:off
        server.getListener("grizzly")
                .getTransport()
                .setWorkerThreadPoolConfig(
                        defaultConfig()
                        .setPoolName("grizzly-" + applicationId)
                        .setCorePoolSize(10)
                        .setMaxPoolSize(100)
                        .setDaemon(false));
        //@formatter:on

        WebappContext context = new WebappContext(applicationId, "/" + applicationId);
        for (ServletContextInitializer initializer : initializers) {
            try {
                initializer.onStartup(context);
            } catch (ServletException e) {
                Throwables.propagate(e);
            }
        }

        context.deploy(server);

        return new EmbeddedServletContainer() {
            final Service service = new AbstractIdleService() {

                @Override
                protected void startUp() throws Exception {

                    try {
                        server.start();
                    } catch (IOException e) {
                        throw Throwables.propagate(e);
                    }
                    log.info("mvc server started ");

                }

                @Override
                protected void shutDown() throws Exception {
                    server.shutdownNow();
                    log.info("mvc server shutdown ");

                }

            };

            public void start() {
                service.startAsync();
                service.awaitRunning();
            }

            public void stop() {
                service.stopAsync();
                service.awaitTerminated();
            }

            public int getPort() {
                return port;
            }
        };
    }
}
