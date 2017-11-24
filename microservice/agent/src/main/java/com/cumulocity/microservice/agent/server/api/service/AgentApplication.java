package com.cumulocity.microservice.agent.server.api.service;

import static com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation.microserviceMetadataRepresentation;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.cumulocity.rest.representation.application.ApplicationRepresentation;

public class AgentApplication implements InitializingBean {
    
    private ApplicationRepresentation application;
    private MicroserviceRepository microserviceRepository;
    private String applicationName;
    private List<String> requiredRoles;

    public AgentApplication(String applicationName, List<String> requiredRoles,
            MicroserviceRepository microserviceRepository) {
        this.applicationName = applicationName;
        this.requiredRoles = requiredRoles;
        this.microserviceRepository = microserviceRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        application = microserviceRepository.register(applicationName, microserviceMetadataRepresentation()
                .requiredRoles(requiredRoles)
                .build());
        
    }
    
    public String getId() {
        return application.getId();
    }

}
