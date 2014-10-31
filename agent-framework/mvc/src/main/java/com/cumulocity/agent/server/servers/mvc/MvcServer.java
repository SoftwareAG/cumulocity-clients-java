package com.cumulocity.agent.server.servers.mvc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.cumulocity.agent.server.Server;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;

@Component("mvcServer")
public class MvcServer implements Server {

    private final String host;

    private final int port;

    private final String applicationId;

    private final WebApplicationContext applicationContext;

    private final Service service = new AbstractService() {

        private HttpServer server;

        @Override
        protected void doStart() {
            server = HttpServer.createSimpleServer(null, new InetSocketAddress(host, port));

            server.getListener("grizzly")
                    .getTransport()
                    .setWorkerThreadPoolConfig(
                            ThreadPoolConfig.defaultConfig().setPoolName("grizzly-" + applicationId).setCorePoolSize(10)
                                    .setMaxPoolSize(100));

            WebappContext context = new WebappContext(applicationId, "/" + applicationId);
            context.addServlet("dispatcher-servlet", new DispatcherServlet(applicationContext)).addMapping("/*");
            context.addListener(new ContextLoaderListener(applicationContext));
            context.deploy(server);
            try {
                server.start();
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }

        @Override
        protected void doStop() {
            server.shutdownNow();
        }
    };

    @Autowired
    public MvcServer(@Value("${server.host:0.0.0.0}") String host, @Value("${server.port:80}") int port,
            @Value("${server.id}") String contextPath, WebApplicationContext context) {
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
    public void stop() {
        service.stopAsync();
        service.awaitTerminated();
    }
}
