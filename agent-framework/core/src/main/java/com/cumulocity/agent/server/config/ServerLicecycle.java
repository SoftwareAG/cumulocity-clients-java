package com.cumulocity.agent.server.config;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cumulocity.agent.server.Server;

@Configuration
public class ServerLicecycle implements InitializingBean, DisposableBean {

    @Autowired
    private List<Server> servers;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Server server : servers) {
            server.start();
        }

    }

    @Override
    public void destroy() throws Exception {
        for (Server server : servers) {
            server.stop();
        }
    }

}
