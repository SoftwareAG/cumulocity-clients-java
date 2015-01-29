package com.cumulocity.agent.server.servers.mvc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.cumulocity.agent.server.Server;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;

@Component("mvcServer")
public class MvcServer implements Server {

    private final Logger log = LoggerFactory.getLogger(MvcServer.class);

    private final String host;

    private final int port;

    private final String applicationId;

    private final ApplicationContext applicationContext;

    private final Service service = new AbstractIdleService() {

        private HttpServer server;

        @Override
        protected void startUp() throws Exception {
            server = HttpServer.createSimpleServer(null, new InetSocketAddress(host, port));

            server.getListener("grizzly")
                    .getTransport()
                    .setWorkerThreadPoolConfig(
                            ThreadPoolConfig.defaultConfig().setPoolName("grizzly-" + applicationId).setCorePoolSize(10)
                                    .setMaxPoolSize(100));

            WebappContext context = new WebappContext(applicationId, "/" + applicationId);
            final AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
            webApplicationContext.register(MvcServerConfiguration.class);
            context.addServlet("dispatcher-servlet", new DispatcherServlet(webApplicationContext)).addMapping("/*");
            context.addListener(new ContextLoaderListener(webApplicationContext));
            context.deploy(server);
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

    @Autowired
    public MvcServer(@Value("${server.host:0.0.0.0}") String host, @Value("${server.port:80}") int port,
            @Value("${server.id}") String contextPath, ApplicationContext context) {
        this.host = host;
        this.port = port;
        this.applicationId = contextPath;
        this.applicationContext = context;
    }

    @Override
    public void start() {
        service.startAsync();
        service.awaitRunning();
    }

    @Override
    public void awaitTerminated() {
        service.awaitTerminated();
    }

    @Override
    public void stop() {
        service.stopAsync();
        service.awaitTerminated();
    }
}
