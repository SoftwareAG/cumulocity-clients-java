package com.cumulocity.agent.server.feature;

import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.EndpointWebMvcAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.ManagementServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

@Configuration
@Import({ ErrorMvcAutoConfiguration.class, ServerPropertiesAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class,
		DispatcherServletAutoConfiguration.class, WebMvcAutoConfiguration.class, MultipartAutoConfiguration.class,
		ManagementServerPropertiesAutoConfiguration.class, EndpointAutoConfiguration.class, EndpointWebMvcAutoConfiguration.class,
		HealthIndicatorAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
		HttpEncodingAutoConfiguration.class })
@AutoConfigureBefore(HttpMessageConvertersAutoConfiguration.class)
public class MvcServer {

    @Bean
    public Jackson2ObjectMapperFactoryBean mapper() {
        return new Jackson2ObjectMapperFactoryBean();
    }

}
