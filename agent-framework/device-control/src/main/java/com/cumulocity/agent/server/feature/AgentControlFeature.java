package com.cumulocity.agent.server.feature;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.devicecontrol.DeviceControlListener;
import com.cumulocity.agent.server.devicecontrol.OperationDispacherRegistry;
import com.cumulocity.agent.server.devicecontrol.OperationsDispatcher;
import com.cumulocity.agent.server.repository.AlarmRepository;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.agent.server.repository.InventoryRepository;

@Configuration
@ComponentScan(basePackages = "com.cumulocity.agent.server.agent")
@Import({ContextFeature.class, SchedulingFeature.class, RepositoryFeature.class})
public class AgentControlFeature {

    @Bean
    @Autowired
    public DeviceControlListener deviceControlListener(OperationsDispatcher dispatcher, AlarmRepository alarmRepository, InventoryRepository inventoryRepository,
            @Value("${application.id:defaultId}") String applicationId) {
        return new DeviceControlListener(dispatcher, alarmRepository, inventoryRepository, applicationId);
    }
    
    @Bean
    @Autowired
    public OperationDispacherRegistry operationDispatcherRegistry(Executor executor, DeviceContextService contextService, ListableBeanFactory beanFactory,
            DeviceControlRepository deviceControl) {
        return new OperationDispacherRegistry(executor, contextService, beanFactory, deviceControl);
    }
    
    @Bean
    @Autowired
    public OperationsDispatcher operationDispatcher(OperationDispacherRegistry queueRegistry) {
        return new OperationsDispatcher(queueRegistry);
    }

    
}
