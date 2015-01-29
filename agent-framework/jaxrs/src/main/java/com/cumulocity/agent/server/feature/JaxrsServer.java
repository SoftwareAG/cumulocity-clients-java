package com.cumulocity.agent.server.feature;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.servers.jaxrs.JaxrsServerContainer;

@Configuration
@Import({ CommonConfiguration.class, JaxrsServerContainer.class, EmbeddedServletContainerAutoConfiguration.class,
        JerseyAutoConfiguration.class })
@Order(LOWEST_PRECEDENCE)
public class JaxrsServer {
}
